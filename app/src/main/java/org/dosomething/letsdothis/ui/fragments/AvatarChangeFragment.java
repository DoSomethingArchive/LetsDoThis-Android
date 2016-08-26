package org.dosomething.letsdothis.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Fragment for the user to select and save a new profile photo.
 *
 * Created by juy on 8/18/15.
 */
public class AvatarChangeFragment extends Fragment implements View.OnClickListener
{
    // Activity request code
    public static final int SELECT_PHOTO = 47539;

    // UI references
    private ImageView photoView;
    private ProgressBar progressBar;

    // File path for photo
    private File mPhotoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_avatar_change, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoView = (ImageView)view.findViewById(R.id.avatar);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);
		Button saveButton = (Button) view.findViewById(R.id.save);

        photoView.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        // Need to wait for photoView's size to be set before displaying a photo in it
        photoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Display current photo if one exists
                String photoPath = AppPrefs.getInstance(getActivity()).getAvatarPath();
                if (photoPath != null) {
                    displayPicture(photoPath);
                }

                // Remove observer
                photoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

		((SetTitleListener) getActivity()).setTitle(getResources().getString(R.string.change_photo));
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.avatar) {
            choosePicture();
        } else if (id == R.id.save) {
            savePicture();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
					String action = data.getAction();
					isCamera = (action != null) && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
				}

                Uri selectedImageUri;
				Uri targetUri = Uri.fromFile(mPhotoFile);
                if (isCamera) {
                    selectedImageUri = targetUri;
                } else {
                    selectedImageUri = data.getData();
                }

                Crop.of(selectedImageUri, targetUri).asSquare().start(getActivity(), AvatarChangeFragment.this);
            }
            else if (requestCode == Crop.REQUEST_CROP) {
                String filePath = mPhotoFile.getAbsolutePath();

                // Display cropped photo to screen
                displayPicture(filePath);
            }
        }
    }

    /**
     * Start Intent to select photo from gallery or camera.
     */
    private void choosePicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoFile = new File(
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "user_profile.jpg");
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_PHOTO);
    }

    /**
     * Save the picture path locally and to Northstar.
     */
    private void savePicture() {
        if (mPhotoFile == null) {
            Toast.makeText(getActivity(), getString(R.string.error_photo_save), Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to AppPrefs
        AppPrefs.getInstance(getActivity()).setAvatarPath(mPhotoFile.getAbsolutePath());

        // Upload to Northstar
        String id = AppPrefs.getInstance(getActivity()).getCurrentUserId();
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new UploadAvatarTask(id, mPhotoFile.getAbsolutePath()));

        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Display a picture to the view.
     *
     * @param path Photo's local file path
     */
    private void displayPicture(String path) {
        String filePath = "file://" + path;

        // First invalidate in order to clear any picture that's already loaded
        Picasso.with(getActivity()).invalidate(filePath);
        Picasso.with(getActivity()).load(filePath)
                .resize(photoView.getWidth(), photoView.getHeight()).into(photoView);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UploadAvatarTask task) {
        progressBar.setVisibility(View.GONE);

        if (!task.hasError()) {
            // Return to the previous fragment on success
            getActivity().onBackPressed();
        }
    }
}

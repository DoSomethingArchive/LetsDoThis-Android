package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.RegisterTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String FB_PROFILE     = "FB_PROFILE";
    public static final int    SELECT_PICTURE = 321;

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText  phoneEmail;
    private EditText  password;
    private EditText  firstName;
    private EditText  lastName;
    private EditText  birthday;
    private ImageView avatar;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private Uri imageUri;
    private File externalFile;

    public static Intent getLaunchIntent(Context context, Profile fbProfile)
    {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(FB_PROFILE, fbProfile);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Profile profile = getIntent().getParcelableExtra(FB_PROFILE);

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                choosePicture();
            }
        });

        initRegisterListener();
        initUI(profile);
    }

    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                     "avatar.jpg");
        imageUri = Uri.fromFile(externalFile);
        if(BuildConfig.DEBUG)
        {
            Log.d("photo location", imageUri.toString());
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_PICTURE)
            {
                final boolean isCamera;
                if(data == null)
                {
                    isCamera = true;
                }
                else
                {
                    final String action = data.getAction();
                    if(action == null)
                    {
                        isCamera = false;
                    }
                    else
                    {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if(isCamera)
                {
                    selectedImageUri = imageUri;
                }
                else
                {
                    selectedImageUri = data.getData();
                }
                if(BuildConfig.DEBUG)
                {
                    Log.d("test-----------", selectedImageUri.toString());
                }

                Picasso.with(this).load(selectedImageUri).resize(avatar.getWidth(), avatar.getHeight()).into(avatar);

                File externalFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                             "avatar.jpg");
                saveFile(selectedImageUri, externalFile);
                //FIXME: this should be loaded into Hub
                AppPrefs.getInstance(this).setAvatarPath(externalFile.getAbsolutePath());
            }
        }
    }

    private void saveFile(Uri sourceUri, File externalFile)
    {

        final int chunkSize = 1024;
        byte[] imageData = new byte[chunkSize];

        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = getContentResolver().openInputStream(sourceUri);
            out = new FileOutputStream(externalFile);

            int bytesRead;
            while((bytesRead = in.read(imageData)) > 0)
            {
                out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
            }

        }
        catch(IOException ex)
        {
            Toast.makeText(this, "Saving picture failed.", Toast.LENGTH_SHORT).show();
        }
        finally
        {
                try
                {
                    if(in != null)
                    {
                        in.close();
                    }
                    if(out != null)
                    {
                        out.close();
                    }
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
        }
    }

    private void initRegisterListener()
    {
        phoneEmail = (EditText) findViewById(R.id.phone_email);
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthday = (EditText) findViewById(R.id.birthday);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String phoneEmailtext = phoneEmail.getText().toString();
                String passtext = password.getText().toString();
                String firsttext = firstName.getText().toString();
                String lasttext = lastName.getText().toString();
                String birthtext = birthday.getText().toString();

                TaskQueue.loadQueueDefault(RegisterActivity.this).execute(
                        new RegisterTask(phoneEmailtext, passtext, firsttext, lasttext, birthtext));
            }
        });
    }

    private void initUI(Profile profile)
    {
        if(profile != null)
        {
            firstName.setText(profile.getFirstName());
            lastName.setText(profile.getLastName());
        }
    }

    @Override
    public void onBackPressed()
    {
        LDTApplication.loginManager.logOut();
        super.onBackPressed();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RegisterTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            Toast.makeText(this, "success register", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            Toast.makeText(this, "failed register", Toast.LENGTH_SHORT).show();
        }
    }

}

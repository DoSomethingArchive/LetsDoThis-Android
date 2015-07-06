package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Log;
import android.preference.SwitchPreference;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.ui.ChangeEmailActivity;
import org.dosomething.letsdothis.ui.ChangeNumberActivity;
import org.dosomething.letsdothis.ui.ChangePasswordActivity;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsFragment extends PreferenceFragment implements ConfirmDialog.ConfirmListener
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int SELECT_PICTURE = 47539;
    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private Uri imageUri;


    private SetTitleListener setTitleListener;

    public static SettingsFragment newInstance()
    {
        return new SettingsFragment();
    }


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.preference_general);

        initRate();
        initLogout();
        initChangeEmail();
        initChangeNotifications();
        initChangePassword();
        initChangeEmail();
        initChangeNotifications();
        initChangePassword();
        initChangePhone();
        initNotificationsPrefs();
        initChangePhoto();
        initChangePhone();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        setTitleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitleListener.setTitle(getResources().getString(R.string.settings));
    }

    private void initLogout()
    {
        findPreference(getString(R.string.logout))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        ConfirmDialog confirmDialog = ConfirmDialog
                                .newInstance(getString(R.string.prompt_logout));
                        confirmDialog.setListener(SettingsFragment.this);
                        confirmDialog.show(getFragmentManager(), ConfirmDialog.TAG);
                        return true;
                    }
                });
    }
    
    private void initChangePhoto()
    {
        findPreference(getString(R.string.change_photo))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {

                        choosePicture();
                        //                        startActivity(ChangeNumberActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }
    
    private void initChangePhone()
    {
        findPreference(getString(R.string.change_number))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangeNumberActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }

    private void initChangeEmail()
    {
        findPreference(getString(R.string.change_email))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangeEmailActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }

    private void initChangePassword()
    {
        findPreference(getString(R.string.change_password))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangePasswordActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }

    private void initNotificationsPrefs()
    {
        Preference receiveNotifs = findPreference(getString(R.string.receive_notifications));
        updateChangeNotifsPref((SwitchPreference) receiveNotifs);
        receiveNotifs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                updateChangeNotifsPref((SwitchPreference) preference);
                return true;
            }
        });

        findPreference(getString(R.string.change_notifications_prefs))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        getFragmentManager().beginTransaction().replace(R.id.container,
                                                                        new NotificationSettingsFragment())
                                .addToBackStack(null).commit();
                        return true;
                    }
                });
    }


    private void updateChangeNotifsPref(SwitchPreference preference)
    {
        boolean checked = preference.isChecked();
        findPreference(getString(R.string.change_notifications_prefs)).setEnabled(checked);
    }


    private void initChangeNotifications()
    {
        findPreference(getString(R.string.change_notifications_prefs))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotificationSettingsFragment())
                                .addToBackStack(null).commit();
                        return true;
                    }
                });
    }

    private void initRate()
    {
        findPreference(getString(R.string.rate))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=org.dosomething.letsdothis"));
                        startActivity(intent);
                        return true;
                    }
                });
    }

    @Override
    public void onConfirmClick()
    {
        BaseActivity.logOutUser(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
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
                String realPathFromURI;
                if(isCamera)
                {
                    selectedImageUri = imageUri;
                    realPathFromURI = selectedImageUri.toString().replaceFirst("file://", "");
                }
                else
                {
                    selectedImageUri = data.getData();
                    realPathFromURI = getRealPathFromURI(getActivity(), selectedImageUri);
                }

                String id = AppPrefs.getInstance(getActivity()).getCurrentUserId();
                TaskQueue.loadQueueDefault(getActivity())
                        .execute(new UploadAvatarTask(id, realPathFromURI));
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally
        {
            if(cursor != null)
            {
                cursor.close();
            }
        }
    }

    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "user_profile" + ".jpg");
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
}

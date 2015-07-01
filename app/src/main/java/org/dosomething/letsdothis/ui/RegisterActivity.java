package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.FbUser;
import org.dosomething.letsdothis.tasks.RegisterTask;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.dosomething.letsdothis.utils.ViewUtils;

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
    public static final  String FB_USER        = "FB_USER";
    public static final  int    SELECT_PICTURE = 321;
    private static final String TAG            = RegisterActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText  email;
    private EditText  password;
    private EditText  firstName;
    private EditText  lastName;
    private EditText  birthday;
    private ImageView avatar;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private Uri imageUri;

    public static Intent getLaunchIntent(Context context, FbUser user)
    {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(FB_USER, user);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initLightning();

        FbUser fbUser = (FbUser) getIntent().getSerializableExtra(FB_USER);

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                choosePicture();
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(LoginActivity.getLaunchIntent(RegisterActivity.this));
                finish();
            }
        });

        initRegisterListener();
        initUI(fbUser);
    }

    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = ViewUtils.getAvatarFile(this);
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
                    Log.d("drawer_text-----------", selectedImageUri.toString());
                }

                Picasso.with(this).load(selectedImageUri)
                        .resize(avatar.getWidth(), avatar.getHeight()).into(avatar);

                File externalFile = ViewUtils.getAvatarFile(this);
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
            File temp = new File(getFilesDir(), "temp" + System.currentTimeMillis() + ".jpg");

            in = getContentResolver().openInputStream(sourceUri);
            out = new FileOutputStream(temp);

            int bytesRead;
            while((bytesRead = in.read(imageData)) > 0)
            {
                out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
            }

            temp.renameTo(externalFile);

        }
        catch(IOException ex)
        {
            Log.e(TAG, ex.getMessage());
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
                Log.e(TAG, "error closing input and output stream");
            }
        }
    }

    private void initRegisterListener()
    {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthday = (EditText) findViewById(R.id.birthday);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String emailText = email.getText().toString();
                String passtext = password.getText().toString();
                String firsttext = firstName.getText().toString();
                String lasttext = lastName.getText().toString();
                String birthtext = birthday.getText().toString();

                TaskQueue.loadQueueDefault(RegisterActivity.this).execute(
                        new RegisterTask(emailText, passtext, firsttext, lasttext, birthtext));
            }
        });
    }

    private void initUI(FbUser fbUser)
    {
        if(fbUser != null)
        {
            firstName.setText(fbUser.first_name);
            lastName.setText(fbUser.last_name);
            email.setText(fbUser.email);
            birthday.setText(fbUser.birthday);
        }
    }

    @Override
    public void onBackPressed()
    {
        LoginManager.getInstance().logOut();
        super.onBackPressed();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RegisterTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            broadcastLogInSuccess(this);
            Toast.makeText(this, "success register", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
        }
        else
        {
            Toast.makeText(this, "failed register", Toast.LENGTH_SHORT).show();
        }
    }

}

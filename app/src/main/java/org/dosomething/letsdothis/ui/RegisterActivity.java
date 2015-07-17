package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.FbUser;
import org.dosomething.letsdothis.tasks.RegisterTask;
import org.dosomething.letsdothis.ui.adapters.InvitesAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.dosomething.letsdothis.utils.Hashery;
import org.dosomething.letsdothis.utils.ViewUtils;

import java.io.File;

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
    private EditText  invite1;
    private EditText  invite2;
    private EditText  invite3;
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
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(externalFile));
        imageUri = Uri.parse(externalFile.getAbsolutePath());
        if(BuildConfig.DEBUG)
        {
            Log.d("photo location", imageUri.toString());
        }

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
                if(data.getData() == null)
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

                startActivityForResult(PhotoCropActivity
                                               .getResultIntent(this, selectedImageUri.toString(),
                                                                "User Photo", null),
                                       PhotoCropActivity.RESULT_CODE);
            }
            else if(requestCode == PhotoCropActivity.RESULT_CODE)
            {
                String filePath = data.getStringExtra(PhotoCropActivity.RESULT_FILE_PATH);
                Picasso.with(this).load("file://" + filePath)
                        .resize(avatar.getWidth(), avatar.getHeight()).into(avatar);
                AppPrefs.getInstance(this).setAvatarPath(filePath);
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
        invite1 = (EditText) findViewById(R.id.invite1);
        invite2 = (EditText) findViewById(R.id.invite2);
        invite3 = (EditText) findViewById(R.id.invite3);
        if(BuildConfig.DEBUG)
        {
            invite1.setText("Red");
            invite2.setText("Blunt");
            invite3.setText("Crane");
        }

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

            findViewById(R.id.fbContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.avatarContainer).setVisibility(View.GONE);
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
            Integer groupId = null;
            boolean allFilled = ! TextUtils.isEmpty(invite1.getText()) && ! TextUtils
                    .isEmpty(invite2.getText()) && ! TextUtils.isEmpty(invite3.getText());
            if(allFilled)
            {
                String code = InvitesAdapter.getCode(invite1, invite2, invite3);
                groupId = Hashery.getInstance(this).decode(code);
            }
            broadcastLogInSuccess(this);
            startActivity(MainActivity.getLaunchIntent(this, groupId, allFilled));
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.snack), R.string.fail_register, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.snack_error));
            snackbar.show();
        }
    }

}

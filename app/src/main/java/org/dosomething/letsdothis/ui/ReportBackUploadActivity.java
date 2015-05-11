package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;

import java.io.File;

/**
 * Created by toidiu on 5/8/15.
 */
public class ReportBackUploadActivity extends AppCompatActivity
{
    public static final String CROPPED_SQUARE = "CROPPED_SQUARE";

    public static Intent getLaunchIntent(Context context, String cropedSquare)
    {
        Intent intent = new Intent(context, ReportBackUploadActivity.class);
        intent.putExtra(CROPPED_SQUARE, cropedSquare);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_upload);

        String croppedImage = getIntent().getStringExtra(CROPPED_SQUARE);

        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView imageHero = (ImageView) findViewById(R.id.image_hero);
        Picasso.with(this).load(new File(croppedImage)).into(image);
        Picasso.with(this).load(new File(croppedImage)).into(imageHero);

        View wrapper = findViewById(R.id.image_wrapper);
        //        wrapper.setBackground(new ShadowBackgroundDrawable());
        wrapper.setBackground(new SlantedBackgroundDrawable(true));
        wrapper.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        EditText caption = (EditText) findViewById(R.id.caption);
        EditText number = (EditText) findViewById(R.id.number);
        //FIXME-get actual data
        number.setHint("this is the hint");


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //FIXME do the upload
                finish();
            }
        });
    }
}

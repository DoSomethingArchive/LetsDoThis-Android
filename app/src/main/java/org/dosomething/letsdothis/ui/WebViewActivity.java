package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * A basic Activity to display content via a WebView.
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "url";

    // Google Analytics tracker
    private Tracker mTracker;

    // Progress bar indicating loading progress
    ProgressBar mProgress;

    public static Intent getLaunchIntent(Context context, String url) {
        return new Intent(context, WebViewActivity.class)
                .putExtra(EXTRA_URL, url)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgress.setProgress(progress);
                if (progress >= 100) {
                    mProgress.setVisibility(View.GONE);
                }
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                mProgress.setVisibility(View.GONE);
            }
        });

        webview.loadUrl(getIntent().getStringExtra(EXTRA_URL));

        LDTApplication application = (LDTApplication)getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Submit screen view to Google Analytics
        AnalyticsUtils.sendScreen(mTracker, AnalyticsUtils.SCREEN_WEBVIEW);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

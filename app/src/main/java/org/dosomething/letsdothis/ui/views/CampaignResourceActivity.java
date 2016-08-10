package org.dosomething.letsdothis.ui.views;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Displays a resource associated with a campaign.
 */
public class CampaignResourceActivity extends AppCompatActivity {

	/** Uniquely identifies our messages in the log. */
	private static final String TAG = "CampaignResource";

	/** Identifies the resource display name in our intent. */
	private static final String EXTRA_RESOURCE_NAME = "rscname";

	/** Identifies the resource URI in our intent. */
	private static final String EXTRA_RESOURCE_URI = "rscuri";

	/** Current downloader or null if not downloading. */
	private Downloader downloader;


	/**
	 * Gets an intent to launch this activity with a remote resource.
	 * @param context Application context
	 * @param name Name of the resource to display
	 * @param uri Location of resource to show
	 * @return Intent to launch activity
	 */
	public static Intent getLaunchIntent(Context context, String name, String uri) {
		Intent intent = new Intent(context, CampaignResourceActivity.class);
		intent.putExtra(EXTRA_RESOURCE_NAME, name);
		intent.putExtra(EXTRA_RESOURCE_URI, uri);
		return intent;
	}


	/**
	 * Initializes the activity's vew on creation.
	 * @param savedInstanceState Instance state to restore, if any
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize with our layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_campaign_resource);

		// Set the title
		CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
		String name = getIntent().getStringExtra(EXTRA_RESOURCE_NAME);
		if (name == null) {
			name = getString(R.string.done);
		}
		toolbar.setTitle(name);
		setSupportActionBar(toolbar);
		//noinspection ConstantConditions
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get our contents
		// TODO Consider internal PDF viewer
		// https://github.com/barteksc/AndroidPdfViewer
		Button openInButton = (Button) findViewById(R.id.openInButton);
		String uriExtra = getIntent().getStringExtra(EXTRA_RESOURCE_URI);
		if (uriExtra == null) {
			// Missing resource URI
			Log.e(TAG, "Missing Resource URI");
			openInButton.setEnabled(false);
		} else {
			// Load our contents
			WebView webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(
						WebView view, String url) {
					return false;
				}
			});
			webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + uriExtra);
		}

		// Determine if we can open this file in an external reader
		String dir;
		if (Build.VERSION.SDK_INT >= 19) {
			dir = Environment.DIRECTORY_DOCUMENTS;
		} else {
			dir = Environment.DIRECTORY_DOWNLOADS;
		}
		final Uri uri = Uri.parse(uriExtra);
		final File localFile = new File(
				Environment.getExternalStoragePublicDirectory(dir),
				uri.getLastPathSegment());

		// Create an external app chooser for the resource
		final Intent intentOpen = new Intent(Intent.ACTION_VIEW);
		intentOpen.setDataAndType(Uri.fromFile(localFile), "application/pdf");
		intentOpen.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if (getPackageManager().queryIntentActivities(intentOpen, 0).size() == 0) {
			// No external app can handle this intent
			openInButton.setVisibility(View.GONE);
		} else {
			// Attach the open in button
			openInButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openResourceExternally(uri, localFile, intentOpen);
				}
			});
		}
	}


	/**
	 * Handles toolbar interaction.
	 * @param item Item selected from toolbar
	 * @return Handled flag
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				if (downloader != null) {
					// Cancel the download in progress
					downloader.cancel(true);
				} else {
					// We can leave now
					onBackPressed();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Opens a selector that allows the user to pick an external application for the resource.
	 * @param remote Remote location of resource
	 * @param local Local location of resource
	 * @param intentOpen intent needed to open the resource
	 */
	private void openResourceExternally(Uri remote, File local, Intent intentOpen) {
		// Does the file already exist locally
		if (!local.exists()) {
			// We need to download the file now
			if (downloader != null) {
				// Already downloading
				return;
			}

			// Download now
			downloader = new Downloader();
			downloader.execute(remote, Uri.fromFile(local));
			return;
		}
		try {
			// Launch the activity chooser
			startActivity(Intent.createChooser(intentOpen, null));
		} catch (ActivityNotFoundException e) {
			// Failed to launch chooser, should never happen
			Log.e(TAG, String.format("Failed to launch chooser for %s of type %s",
					intentOpen.getData().toString(), intentOpen.getType()), e);
			showError(R.string.campaign_error_resource_no_activity);
		}
	}


	/**
	 * Shows a popup error message.
	 * @param errorMsg Message to show
	 */
	public void showError(String errorMsg) {
		int backgroundColor;
		if (Build.VERSION.SDK_INT >= 23) {
			backgroundColor = getResources().getColor(R.color.snack_error, null);
		} else {
			//noinspection deprecation
			backgroundColor = getResources().getColor(R.color.snack_error);
		}
		Snackbar snackbar = Snackbar.make(findViewById(R.id.snack), errorMsg, Snackbar.LENGTH_LONG);
		snackbar.getView().setBackgroundColor(backgroundColor);
		snackbar.show();
	}


	/**
	 * Shows a popup error message from the resources.
	 * @param resourceId String resource id
	 */
	public void showError(int resourceId) {
		showError(getString(resourceId));
	}


	// TODO: Mind lifecycle events vs download
	private class Downloader extends AsyncTask<Uri, Integer, String> {

		private ProgressBar progressBar;

		@Override
		protected String doInBackground(Uri... params) {
			String errorMsg = null;
			InputStream inps = null;
			OutputStream outs = null;
			File localFile = new File(params[1].getPath());
			int lengthOfFile;
			try {
				try {
					// Start fetching the file from the remote source
					URLConnection connection = new URL(params[0].toString()).openConnection();
					connection.connect();
					inps = new BufferedInputStream(connection.getInputStream(), 8192);

					// Get the file length for progress
					lengthOfFile = connection.getContentLength();
					publishProgress(0, lengthOfFile);
				} catch (IOException exc) {
					// Failed to begin the remote fetch
					errorMsg = getString(R.string.campaign_error_dl_connect_remote);
					throw exc;
				}
				try {
					// Start writing the file locally
					localFile.getParentFile().mkdirs();
					outs = new FileOutputStream(localFile);
				} catch (IOException exc) {
					// Failed to open the file
					errorMsg = getString(R.string.campaign_error_dl_create_local);
					throw exc;
				}

				// Download the file
				int count;
				int total = 0;
				byte data[] = new byte[1024];
				while (true) {
					try {
						// Read a buffer's worth of data
						if ((count = inps.read(data)) < 0) {
							break;
						}
					} catch (IOException exc) {
						// Failed to read from the web server
						errorMsg = getString(R.string.campaign_error_dl_read_remote);
						throw exc;
					}

					// Publish current read progress
					total += count;
					publishProgress(total, lengthOfFile);
					try {
						// Write the read data
						outs.write(data, 0, count);
					} catch (IOException exc) {
						// Failed to write to the local file
						errorMsg = getString(R.string.campaign_error_dl_write_local);
						throw exc;
					}
				}
				try {
					// Make sure the output file is completely written
					outs.flush();
				} catch (IOException exc) {
					// Failed to complete the write
					errorMsg = getString(R.string.campaign_error_dl_write_local);
					throw exc;
				}
			} catch (Exception excAny) {
				// Log the exception for debugging
				Log.e(TAG, "Failed to download attachment", excAny);
			} finally {
				if (inps != null) {
					// Close the input stream
					try {
						inps.close();
					} catch (IOException exc) {
						Log.d(TAG, "Unexpected exception closing download input stream", exc);
					}
				}
				if (outs != null) {
					// Close the output stream
					try {
						outs.close();
					} catch (IOException exc) {
						Log.d(TAG, "Unexpected exception closing download output stream", exc);
					}
					if (errorMsg != null) {
						// Cleanup
						//noinspection ResultOfMethodCallIgnored
						localFile.delete();
					}
				}
			}

			// Return the error message if any
			return errorMsg;
		}

		@Override
		protected void onCancelled(String s) {
			super.onCancelled(s);
			onBackPressed();
		}

		@Override
		protected void onPostExecute(String s) {
			// Cleanup download
			super.onPostExecute(s);
			progressBar.setVisibility(View.GONE);
			downloader = null;
			if (s != null) {
				// Show the error message
				showError(s);
			} else {
				// Show the downloaded file
				findViewById(R.id.openInButton).performClick();
			}
		}

		@Override
		protected void onPreExecute() {
			// Show the progress bar in a reasonable initial state
			super.onPreExecute();
			progressBar = (ProgressBar) findViewById(R.id.progress);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(0);
			progressBar.setMax(100);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			int length = values[1];
			if (length > 0) {
				progressBar.setMax(length);
				progressBar.setProgress(values[0]);
			}
		}
	}
}

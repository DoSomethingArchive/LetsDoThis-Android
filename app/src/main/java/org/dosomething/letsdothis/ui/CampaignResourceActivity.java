package org.dosomething.letsdothis.ui;

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
import android.widget.Button;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.CampaignActionGuide;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


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

	/** Identifies the action guide parcel in our intent. */
	private static final String EXTRA_ACTION_GUIDES = "rscactionguide";

	/** Current downloader or null if not downloading. */
	private Downloader downloader;


	/**
	 * Gets an intent to launch this activity with a remote resource attachment.
	 * @param context Application context
	 * @param name Name of the attachment to display
	 * @param uri Location of resource to show
	 * @return Intent to launch activity
	 */
	public static Intent getIntentForAttachment(Context context, String name, String uri) {
		Intent intent = new Intent(context, CampaignResourceActivity.class);
		intent.putExtra(EXTRA_RESOURCE_NAME, name);
		intent.putExtra(EXTRA_RESOURCE_URI, uri);
		return intent;
	}

	/**
	 * Gets an intent to launch this activity to display an action guide.
	 * @param context Application context
	 * @param actionGuides Action guides to show
	 * @return Intent to launch activity
	 */
	public static Intent getIntentForActionGuides(Context context, ArrayList<CampaignActionGuide> actionGuides) {
		Intent intent = new Intent(context, CampaignResourceActivity.class);
		intent.putExtra(EXTRA_ACTION_GUIDES, actionGuides);
		return intent;
	}

	/**
	 * Initializes the activity's vew on creation.
	 * @param savedInstanceState Instance state to restore, if any
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize with our layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_campaign_resource);

		// Set the title
		ArrayList<CampaignActionGuide> actionGuides = null;
		CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
		String name = getIntent().getStringExtra(EXTRA_RESOURCE_NAME);
		if (name == null) {
			// Are we showing an action guides instead?
			actionGuides = getIntent().getParcelableArrayListExtra(EXTRA_ACTION_GUIDES);
			name = getString((actionGuides != null) ? R.string.action_guides : R.string.done);
		}
		toolbar.setTitle(name);
		setSupportActionBar(toolbar);
		//noinspection ConstantConditions
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Which contents are we displaying?
		if (actionGuides == null) {
			// Display an attachments
			createAttachment();
		} else {
			// Show the action guides
			createActionGuides(actionGuides);
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
					downloader.userRequestCancel();
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
	 * If there is a download in progress, stop it.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if (downloader != null) {
			downloader.cancel(true);
		}
	}

	/**
	 * Opens a selector that allows the user to pick an external application for the resource.
	 * @param intentOpen intent needed to open the resource
	 */
	private void openResourceExternally(Intent intentOpen) {
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
	 * Shows the local attachment embedded in our application.  Also sets up the open button
	 * to show the local file in an external viewer.
	 * @param local Location of attachment locally (on the phone)
	 */
	private void showLocal(File local) {
		// Show the file in the PDF viewer
		PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
		pdfView.setVisibility(View.VISIBLE);
		pdfView.fromFile(local)
				.enableSwipe(true)
				.enableDoubletap(true)
				.swipeVertical(false)
				.defaultPage(1)
				.showMinimap(false)
				.enableAnnotationRendering(false)
				.password(null)
				.showPageWithAnimation(true)
				.load();

		// Determine if we can open this file in an external reader
		// Create an external app chooser for the resource
		final Intent intentOpen = new Intent(Intent.ACTION_VIEW);
		Button openInButton = (Button) findViewById(R.id.openInButton);
		intentOpen.setDataAndType(Uri.fromFile(local), "application/pdf");
		intentOpen.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if (getPackageManager().queryIntentActivities(intentOpen, 0).size() > 0) {
			// Attach the open in button
			openInButton.setVisibility(View.VISIBLE);
			openInButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openResourceExternally(intentOpen);
				}
			});
		}
	}

	/**
	 * Shows a popup error message.
	 * @param errorMsg Message to show
	 */
	private void showError(String errorMsg) {
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

	/**
	 * Initializes the created view with the attachment to display.
	 */
	private void createAttachment() {
		// Get our contents
		Button openInButton = (Button) findViewById(R.id.openInButton);
		String uriExtra = getIntent().getStringExtra(EXTRA_RESOURCE_URI);
		if (uriExtra == null) {
			// Missing resource URI
			Log.e(TAG, "Missing Resource URI");
			openInButton.setEnabled(false);
			openInButton.setVisibility(View.VISIBLE);
			return;
		}

		// Have we already downloaded the file?
		String dir;
		if (Build.VERSION.SDK_INT >= 19) {
			dir = Environment.DIRECTORY_DOCUMENTS;
		} else {
			dir = Environment.DIRECTORY_DOWNLOADS;
		}
		Uri uri = Uri.parse(uriExtra);
		File localFile = new File(
				Environment.getExternalStoragePublicDirectory(dir),
				uri.getLastPathSegment());
		if (localFile.exists()) {
			// Show embedded PDF
			showLocal(localFile);
		} else {
			// Start download
			downloader = new Downloader();
			downloader.execute(uri, Uri.fromFile(localFile));
		}
	}

	/**
	 * Initializes the created view with the passed action guides.
	 * @param actionGuides Action guides to show
	 */
	private void createActionGuides(ArrayList<CampaignActionGuide> actionGuides) {
		// Render the action guides as HTML
		StringBuilder doc = new StringBuilder("<html><body style='color:#4A4A4A;font-size:10pt;'>");
		for(CampaignActionGuide guide : actionGuides) {
			// Render the current guide
			if (guide.hasTitle()) {
				doc.append(renderActionGuideTitle(guide.getTitle()));
			}
			if (guide.hasSubtitle()) {
				doc.append(renderActionGuideCopy(guide.getSubtitle()));
			}
			if (guide.hasIntroTitle()) {
				doc.append(renderActionGuideHeading(guide.getIntroTitle()));
			}
			if (guide.hasIntroCopy()) {
				doc.append(renderActionGuideCopy(guide.getIntroCopy()));
			}
			if (guide.hasAdditionalTitle()) {
				doc.append(renderActionGuideHeading(guide.getAdditionalTitle()));
			}
			if (guide.hasAdditionalCopy()) {
				doc.append(renderActionGuideCopy(guide.getAdditionalCopy()));
			}
		}
		doc.append("</body></html>");

		// Display the action guides
		WebView webview = (WebView) findViewById(R.id.webview);
		webview.setVisibility(View.VISIBLE);
		webview.loadData(doc.toString(), "text/html", null);
	}

	/**
	 * Renders copy inside of an action guide.
	 * @param copy Copy to render
	 * @return HTML rendered copy
	 */
	private String renderActionGuideCopy(String copy) {
		return "<div style='font-family:BrandonGrotesque-Regular;font-size:11pt;padding:4pt 8pt'>" + copy + "</div>";
	}

	/**
	 * Renders a heading inside of an action guide.
	 * @param heading Heading to render
	 * @return HTML rendered header
	 */
	private String renderActionGuideHeading(String heading) {
		return "<div style='color:#9C9C9C;font-family:BrandonGrotesque-Bold;font-size:9pt;padding:8pt 8pt 0 8pt;'>" + heading.toUpperCase() + "</div>";
	}

	/**
	 * Renders a title inside of an action guide.
	 * @param title Title to render
	 * @return HTML rendered title
	 */
	private String renderActionGuideTitle(String title) {
		return "<div align='center' style='background:#EEEEEE;font-family:BrandonGrotesque-Bold;font-size:12pt;padding:8pt'>" + title.toUpperCase() + "</div>";
	}


	/**
	 * Downloads an attachment on a worker thread, with event integration back to the UI.
	 */
	private class Downloader extends AsyncTask<Uri, Integer, String> {

		private ProgressBar progressBar;
		private File localFile;
		private boolean userCancel;

		/**
		 * Cancels the download based on user action, which ends this activity (since we can't
		 * do much without the download).
		 */
		public void userRequestCancel() {
			userCancel = true;
			cancel(true);
		}

		/**
		 * Downloads the attachment in the background.
		 * @param params Remote and local locations for the download
		 * @return Error message or null on success
		 */
		@Override
		protected String doInBackground(Uri... params) {
			String errorMsg = null;
			InputStream inps = null;
			OutputStream outs = null;
			int lengthOfFile;
			localFile = new File(params[1].getPath());
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
					//noinspection ResultOfMethodCallIgnored
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
				while (!isCancelled()) {
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
				if (isCancelled()) {
					// Make sure we don't keep an incomplete file
					//noinspection ResultOfMethodCallIgnored
					localFile.delete();
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

		/**
		 * Cleans up after the download was canceled.
		 * @param s Thread cancel result ignored
		 */
		@Override
		protected void onCancelled(String s) {
			super.onCancelled(s);
			//noinspection ResultOfMethodCallIgnored
			localFile.delete();
			if (userCancel) {
				// Only back press if the user requested the cancel
				onBackPressed();
			}
		}

		/**
		 * Updates the UI for the completed (or failed) download.
		 * @param errorMsg Error message or null on success
		 */
		@Override
		protected void onPostExecute(String errorMsg) {
			// Cleanup download
			super.onPostExecute(errorMsg);
			progressBar.setVisibility(View.GONE);
			downloader = null;
			if (errorMsg != null) {
				// Show the error message
				showError(errorMsg);
			} else {
				// Show the downloaded file
				showLocal(localFile);
			}
		}

		/**
		 * Initializes the progress bar for display.
		 */
		@Override
		protected void onPreExecute() {
			// Show the progress bar in a reasonable initial state
			super.onPreExecute();
			progressBar = (ProgressBar) findViewById(R.id.progress);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(0);
			progressBar.setMax(100);
		}

		/**
		 * Updates the progress bar in the user interface.
		 * @param values Current progress state
		 */
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

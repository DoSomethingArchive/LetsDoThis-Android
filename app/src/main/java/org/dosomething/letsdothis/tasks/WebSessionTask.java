package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;


/**
 * Interfaces the client for the Phoenix web session request with the user interface.
 * @author NearChaos
 */
public class WebSessionTask extends BaseNetworkErrorHandlerTask {
	/** Provides error details or null if none (successful or incomplete). */
	private String errorDetails;

	/** Result URL from the service or null if none. */
	private String magicUrl;


	/**
	 * Explicit constructor because we have good programming style.
	 */
	public WebSessionTask() {
	}


	/**
	 * Indicates if we've received an error result.
	 * @return Error result flag
	 */
	public boolean hasError() {
		return errorDetails != null;
	}


	/**
	 * Gets the magic link result on success or the error details on failure.
	 * Query {@link #hasError()} to determine which result you're getting.  Returns null if the
	 * task us incomplete.
	 * @return Task result
	 */
	public String getResult() {
		return hasError() ? errorDetails : magicUrl;
	}


	/**
	 * Gets a magic url from Phoenix on a worker thread.
	 * @param context Application context
	 * @throws Throwable Failed to transact with Phoenix, or invalid response
	 */
	@Override
	protected void run(Context context) throws Throwable {
		AppPrefs prefs = AppPrefs.getInstance(context);
		String sessionToken = prefs.getSessionToken();
		magicUrl = NetworkHelper.getNorthstarAPIService().createAuthenticatedWebSession(sessionToken, "").url;
	}


	/**
	 * Posts the result to the event bus for handling by all listeners.
	 * @param context Application context
	 */
	@Override
	protected void onComplete(Context context) {
		EventBusExt.getDefault().post(this);
		super.onComplete(context);
	}


	/**
	 * Records the error for processing on the event bus.
	 * @param context Application context
	 * @param throwable Reason for failure
	 * @return Handled flag
	 */
	@Override
	protected boolean handleError(Context context, Throwable throwable) {
		errorDetails = (throwable == null)
				? "Failed to perform web session task"
				: throwable.getMessage();
		return true;
	}
}

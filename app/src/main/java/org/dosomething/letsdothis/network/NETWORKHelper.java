package org.dosomething.letsdothis.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.apache.commons.io.IOUtils;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * Created by kgalligan on 10/8/14.
 */
public class NetworkHelper
{
    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final int    CONNECT_TIMEOUT  = 45;
    public static final int    READ_TIMEOUT     = 30;

    public static RestAdapter makeRequestAdapter()
    {
        return makeRequestAdapterBuilder(new RetrofitErrorHandler(), JSON_DATE_FORMAT).build();
    }

    @NotNull
    public static RestAdapter.Builder makeRequestAdapterBuilder(ErrorHandler errorHandler, String dateTimeFormat)
    {
        RequestInterceptor requestInterceptor = new RequestInterceptor()
        {
            @Override
            public void intercept(RequestFacade request)
            {
                request.addHeader("X-DS-Application-Id", "android");
                request.addHeader("X-DS-REST-API-Key",
                                  LDTApplication.getContext().getString(R.string.api_key));
            }
        };

        Gson gson = new GsonBuilder().setDateFormat(dateTimeFormat).create();

        GsonConverter gsonConverter = new GsonConverter(gson);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor).setConverter(gsonConverter)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("LDTHttp"))
                .setEndpoint(NorthstarAPI.BASE_URL);

        if(errorHandler != null)
        {
            builder.setErrorHandler(errorHandler);
        }

        builder.setClient(new OkClient(makeTimeoutClient(READ_TIMEOUT, CONNECT_TIMEOUT)));

        return builder;
    }

    public static class RetrofitErrorHandler implements ErrorHandler
    {
        @Override
        public Throwable handleError(RetrofitError cause)
        {
            if(cause.isNetworkError())
            {
                return new NetworkException(cause.getCause());
            }

            return cause;
        }
    }

    private static OkHttpClient makeTimeoutClient(int readTimeout, int connectTimeout)
    {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(readTimeout, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(connectTimeout, TimeUnit.SECONDS);

        return okHttpClient;
    }

    public static String debugOut(Response response) throws IOException
    {
        return IOUtils.toString(response.getBody().in());
    }

}
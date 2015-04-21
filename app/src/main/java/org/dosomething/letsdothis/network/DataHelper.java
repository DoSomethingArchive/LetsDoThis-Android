package org.dosomething.letsdothis.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;

import java.util.concurrent.TimeUnit;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by kgalligan on 10/8/14.
 */
public class DataHelper
{
    public static final String JSON_DATE_FORMAT_NORTHSTAR = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String JSON_DATE_FORMAT_DO_SOMETHING = "yyyy-MM-dd HH:mm:ss";
    public static final int    CONNECT_TIMEOUT            = 45;
    public static final int    READ_TIMEOUT               = 30;

    public static RestAdapter.Builder getRequestAdapterBuilder()
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

        return new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                                        .setErrorHandler(new RetrofitErrorHandler()).setClient(
                        new OkClient(makeTimeoutClient(READ_TIMEOUT, CONNECT_TIMEOUT)))
                                        .setRequestInterceptor(requestInterceptor)
                                        .setLogLevel(RestAdapter.LogLevel.FULL)
                                        .setLog(new AndroidLog("LDTHttp"));
    }

    public static class RetrofitErrorHandler implements ErrorHandler
    {
        @Override
        public Throwable handleError(RetrofitError cause)
        {
            if(cause.getKind() == RetrofitError.Kind.NETWORK)
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

    public static DoSomethingAPI getDoSomethingAPIService()
    {
        Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT_DO_SOMETHING).create();
        GsonConverter gsonConverter = new GsonConverter(gson);
        return getRequestAdapterBuilder().setConverter(gsonConverter)
                                         .setEndpoint(DoSomethingAPI.BASE_URL).build()
                                         .create(DoSomethingAPI.class);
    }

    public static NorthstarAPI getNorthstarAPIService()
    {
        Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT_NORTHSTAR).create();
        GsonConverter gsonConverter = new GsonConverter(gson);
        return getRequestAdapterBuilder().setConverter(gsonConverter)
                                         .setEndpoint(NorthstarAPI.BASE_URL).build()
                                         .create(NorthstarAPI.class);
    }
}
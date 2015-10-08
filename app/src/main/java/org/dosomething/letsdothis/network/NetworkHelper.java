package org.dosomething.letsdothis.network;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.network.deserializers.ResponseCampaignDeserializer;
import org.dosomething.letsdothis.network.models.ResponseCampaign;

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
public class NetworkHelper
{
    public static final String JSON_DATE_FORMAT_NORTHSTAR    = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String JSON_DATE_FORMAT_DO_SOMETHING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final int    CONNECT_TIMEOUT               = 45;
    public static final int    READ_TIMEOUT                  = 30;

    public static RestAdapter.Builder getRequestAdapterBuilder()
    {

        RequestInterceptor requestInterceptor = new RequestInterceptor()
        {
            @Override
            public void intercept(RequestFacade request)
            {
                Context context = LDTApplication.getContext();
                String northstarAppId = context.getString(R.string.northstar_app_id);
                String northstarApiKey = context.getString(R.string.northstar_api_key);
                request.addHeader("X-DS-Application-Id", northstarAppId);
                request.addHeader("X-DS-REST-API-Key", northstarApiKey);
            }
        };

        return new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new RetrofitErrorHandler())
                .setClient(new OkClient(makeTimeoutClient(READ_TIMEOUT, CONNECT_TIMEOUT)))
                .setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL)
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

    public static DoSomethingAPI getDoSomethingAPIService() {
        String baseUrl;
        if (BuildConfig.BUILD_TYPE.equals("release")) {
            baseUrl = DoSomethingAPI.PRODUCTION_URL;
        }
        else if (BuildConfig.BUILD_TYPE.equals("internal")) {
            baseUrl = DoSomethingAPI.THOR_URL;
        }
        else {
            baseUrl = DoSomethingAPI.QA_URL;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResponseCampaign.class, new ResponseCampaignDeserializer<ResponseCampaign>())
                .setDateFormat(JSON_DATE_FORMAT_DO_SOMETHING).create();
        GsonConverter gsonConverter = new GsonConverter(gson);
        return getRequestAdapterBuilder().setConverter(gsonConverter)
                .setEndpoint(baseUrl).build().create(DoSomethingAPI.class);
    }

    public static NorthstarAPI getNorthstarAPIService() {
        String baseUrl;
        if (BuildConfig.BUILD_TYPE.equals("release")) {
            baseUrl = NorthstarAPI.PRODUCTION_URL;
        }
        else if (BuildConfig.BUILD_TYPE.equals("internal")) {
            baseUrl = NorthstarAPI.THOR_URL;
        }
        else {
            baseUrl = NorthstarAPI.QA_URL;
        }

        Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT_NORTHSTAR).create();
        GsonConverter gsonConverter = new GsonConverter(gson);
        return getRequestAdapterBuilder().setConverter(gsonConverter)
                .setEndpoint(baseUrl).build().create(NorthstarAPI.class);
    }

}
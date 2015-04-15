package org.dosomething.letsdothis.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;

import java.util.concurrent.TimeUnit;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class DataHelper
{

    private NorthstarAPI northstarAPIService;

    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final int    CONNECT_TIMEOUT  = 45;
    public static final int    READ_TIMEOUT     = 30;

    public DataHelper()
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

        Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT).create();

        GsonConverter gsonConverter = new GsonConverter(gson);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                                                           .setClient(new OkClient(okHttpClient))
                                                           .setConverter(gsonConverter)
                                                           .setRequestInterceptor(
                                                                   requestInterceptor)
                                                           .setErrorHandler(new ErrorHandler()
                                                           {
                                                               @Override
                                                               public Throwable handleError(RetrofitError cause)
                                                               {
                                                                   return cause;
                                                               }
                                                           }).setEndpoint(NorthstarAPI.BASE_URL)
                                                           .build();

        northstarAPIService = restAdapter.create(NorthstarAPI.class);
    }

    public NorthstarAPI getNorthstarAPIService()
    {
        return northstarAPIService;
    }
}

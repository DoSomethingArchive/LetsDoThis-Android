package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.network.models.ResponseCampaign;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by izzyoji :) on 4/20/15.
 */
public interface DoSomethingAPI
{

    public static final String BASE_URL = "https://www.dosomething.org/api/v1/";

    @Headers("Content-Type: application/json")
    @GET("/content/{id}")
    ResponseCampaign campaign(@Path("id") int id) throws NetworkException;
}

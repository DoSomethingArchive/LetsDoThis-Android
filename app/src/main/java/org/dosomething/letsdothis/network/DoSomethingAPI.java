package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.network.models.RequestKudo;
import org.dosomething.letsdothis.network.models.ResponseCampaign;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by izzyoji :) on 4/20/15.
 */
public interface DoSomethingAPI
{

    public static final String BASE_URL = BuildConfig.DEBUG
            ? "http://staging.beta.dosomething.org/api/v1/"
            : "https://www.dosomething.org/api/v1/";

    @Headers("Content-Type: application/json")
    @GET("/content/{id}.json")
    ResponseCampaign campaign(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items.json?status=approved")
    ResponseReportBackList reportBackList(@Query("campaigns") String campaignIds, @Query(
            "count") int count, @Query("random") boolean random, @Query(
            "page") int page) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items/{id}.json")
    ResponseReportBack reportBack(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/kudos.json")
    ResponseReportBack submitKudos(@Body RequestKudo requestKudo) throws NetworkException;
}

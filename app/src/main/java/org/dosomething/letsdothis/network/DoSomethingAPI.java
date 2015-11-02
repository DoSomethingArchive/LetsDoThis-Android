package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;
import org.dosomething.letsdothis.network.models.ResponseTaxonomyTerm;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by izzyoji :) on 4/20/15.
 */
public interface DoSomethingAPI {
    String PRODUCTION_URL = "https://www.dosomething.org/api/v1/";
    String THOR_URL = "https://thor.dosomething.org/api/v1";
    String QA_URL = "https://staging.dosomething.org/api/v1";

    @Headers("Content-Type: application/json")
    @GET("/campaigns/{id}.json")
    ResponseCampaignWrapper campaign(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items.json?load_user=true")
    ResponseReportBackList reportBackList(
            @Query("status") String status,
            @Query("campaigns") String campaignIds,
            @Query("count") int count,
            @Query("random") boolean random,
            @Query("page") int page) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/reportback-items/{id}.json?load_user=true")
    ResponseReportBack reportBack(@Path("id") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/campaigns.json")
    ResponseCampaignList campaignList(
            @Query("term_ids") int interestGroupId,
            @Query("mobile_app_date") String currentDate) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/campaigns.json?mobile_app=1")
    ResponseCampaignList campaignListByIds(@Query("ids") String ids) throws NetworkException;

    @GET("/taxonomy_term/{id}.json")
    ResponseTaxonomyTerm taxonomyTerm(@Path("id") int id) throws NetworkException;
}

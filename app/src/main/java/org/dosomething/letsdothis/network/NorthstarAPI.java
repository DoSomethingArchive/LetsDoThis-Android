package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.network.models.ResponseAvatar;
import org.dosomething.letsdothis.network.models.ParseInstallationRequest;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.RequestKudo;
import org.dosomething.letsdothis.network.models.ResponseAvatar;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.network.models.ResponseRbData;
import org.dosomething.letsdothis.network.models.ResponseRegister;
import org.dosomething.letsdothis.network.models.ResponseSubmitReportBack;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.network.models.ResponseUserList;
import org.dosomething.letsdothis.network.models.ResponseUserUpdate;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public interface NorthstarAPI
{

    String BASE_URL = BuildConfig.DEBUG
            ? "http://northstar-qa.dosomething.org/v1"
            : "http://northstar.dosomething.org/v1";

    @FormUrlEncoded
    @POST("/login")
    ResponseLogin loginWithMobile(@Field("mobile") String mobile, @Field(
            "password") String password) throws NetworkException;

    @FormUrlEncoded
    @POST("/login")
    ResponseLogin loginWithEmail(@Field("email") String email, @Field(
            "password") String password) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/users?create_drupal_user=1")
    ResponseRegister registerWithEmail(@Body User user) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/users?create_drupal_user=1")
    ResponseRegister registerWithMobile(@Body User user) throws NetworkException;

    @GET("/users")
    ResponseUserList userList(@Query("page") int page, @Query(
            "limit") int limit) throws NetworkException;

    @GET("/users/_id/{id}")
    ResponseUser userProfile(@Path("id") String id) throws NetworkException;

    @GET("/users/drupal_id/{id}")
    ResponseUser userProfileWithDrupalId(@Path("id") String id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @PUT("/users/{id}")
    ResponseUserUpdate updateUser(@Path("id") String id, @Body TypedInput user) throws NetworkException;

    @POST("/logout")
    Response logout(@Header("Session") String sessionToken) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/user/campaigns/{nid}/reportback")
    ResponseSubmitReportBack submitReportback(@Header("Session") String sessionToken, @Body RequestReportback requestreportback, @Path("nid") int id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/user/campaigns/{id}")
    ResponseRbData getRbData(@Header("Session") String sessionToken, @Path("id") int campId) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/user/campaigns/{id}/signup")
    ResponseCampaignSignUp campaignSignUp(@Body RequestCampaignSignup requestCampaignSignup, @Path("id") int id, @Header("Session") String sessionToken);

    @Headers("Content-Type: application/json")
    @GET("/users/_id/{id}/campaigns")
    ResponseUserCampaign getUserCampaigns(@Path("id") String id);

    @GET("/signup-group/{groupId}")
    ResponseGroup group(@Path("groupId") int groupId);

    @Headers("Content-Type: application/json")
    @POST("/kudos")
    ResponseReportBack submitKudos(@Body RequestKudo requestKudo, @Header("Session") String sessionToken) throws NetworkException;

    @Headers("Content-Type: application/json")
    @PUT("/users/{id}")
    ResponseUserUpdate setParseInstallationId(@Path("id") String id, @Body ParseInstallationRequest ParseInstallationRequest) throws NetworkException;

    @Multipart
    @POST("/users/{id}/avatar")
    ResponseAvatar uploadAvatar(@Path("id") String id, @Part("photo") TypedFile file) throws NetworkException;

}

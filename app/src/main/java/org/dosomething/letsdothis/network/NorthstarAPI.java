package org.dosomething.letsdothis.network;
import android.net.Uri;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ParseInstallationRequest;
import org.dosomething.letsdothis.network.models.RequestKudo;
import org.dosomething.letsdothis.network.models.ResponseAvatar;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.ResponseAvatar;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.network.models.ResponseRegister;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseUser;
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
    @POST("/user/campaigns/{id}/signup")
    ResponseCampaignSignUp campaignSignUp(@Body RequestCampaignSignup requestCampaignSignup, @Path("id") int id, @Header("Session") String sessionToken);

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
    public ResponseAvatar uploadAvatar(@Path("id") String id, @Part("photo") TypedFile file) throws NetworkException;

    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE

    @POST("/users")
    public Response registerWithEmail(@Query("email") String email, @Query(
            "password") String password, @Query("birthdate") Date date, @Query(
            "first_name") String firstName, @Query("last_name") String lastName, @Query(
            "addr_street1") String street1, @Query("addr_street2") String street2, @Query(
            "addr_city") String city, @Query("addr_state") String state, @Query(
            "addr_zip") String zip, @Query("country") String country, @Query(
            "agg_id") int aggId, @Query("cgg_id") int cggId, @Query(
            "drupal_id") int drupalId, @Query("race") String race, @Query(
            "religion") String religion, @Query("college_name") String collegeName, @Query(
            "degree_type") String degreeType, @Query("major_name") String majorName, @Query(
            "hs_gradyear") String hsGradYear, @Query("hs_name") String hsName, @Query(
            "sat_math") int satMath, @Query("sat_verbal") int satVerbal, @Query(
            "sat_writing") int satWriting) throws NetworkException;

    @POST("/users")
    public Response registerWithMobile(@Query("mobile") String mobile, @Query(
            "password") String password, @Query("birthdate") Date date, @Query(
            "first_name") String firstName, @Query("last_name") String lastName, @Query(
            "addr_street1") String street1, @Query("addr_street2") String street2, @Query(
            "addr_city") String city, @Query("addr_state") String state, @Query(
            "addr_zip") String zip, @Query("country") String country, @Query(
            "agg_id") int aggId, @Query("cgg_id") int cggId, @Query(
            "drupal_id") int drupalId, @Query("race") String race, @Query(
            "religion") String religion, @Query("college_name") String collegeName, @Query(
            "degree_type") String degreeType, @Query("major_name") String majorName, @Query(
            "hs_gradyear") String hsGradYear, @Query("hs_name") String hsName, @Query(
            "sat_math") int satMath, @Query("sat_verbal") int satVerbal, @Query(
            "sat_writing") int satWriting) throws NetworkException;

}

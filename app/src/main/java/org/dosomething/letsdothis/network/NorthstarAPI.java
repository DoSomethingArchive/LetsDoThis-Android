package org.dosomething.letsdothis.network;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ParseInstallationRequest;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.RequestKudo;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.network.models.ResponseAvatar;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.network.models.ResponseProfileReportbacks;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.network.models.ResponseRbData;
import org.dosomething.letsdothis.network.models.ResponseRegister;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseSubmitReportBack;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
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
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public interface NorthstarAPI {
    String PRODUCTION_URL = "https://northstar.dosomething.org/v1";
    String THOR_URL = "https://northstar-thor.dosomething.org/v1";
    String QA_URL = "https://northstar-qa.dosomething.org/v1";

    /**
     * Login using the user's mobile number.
     *
     * @param mobile Mobile number
     * @param password Password
     * @return ResponseLogin
     * @throws NetworkException
     */
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/auth/token")
    ResponseLogin loginWithMobile(@Field("mobile") String mobile,
                                  @Field("password") String password) throws NetworkException;

    /**
     * Login using the user's email address.
     *
     * @param email Email address
     * @param password Password
     * @return ResponseLogin
     * @throws NetworkException
     */
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/auth/token")
    ResponseLogin loginWithEmail(@Field("email") String email,
                                 @Field("password") String password) throws NetworkException;

    /**
     * Register a new user on Northstar and Drupal. Need to create the Drupal user in order to
     * submit campaign sign ups and report backs.
     *
     * @param user User
     * @return ResponseRegister
     * @throws NetworkException
     */
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("/auth/register?create_drupal_user=1")
    ResponseRegister registerWithEmail(@Body User user) throws NetworkException;

    /**
     * Get the current user's profile info.
     *
     * @param sessionToken Token for the currently logged in user
     * @return ResponseUser
     * @throws NetworkException
     */
    @GET("/profile")
    ResponseUser userProfile(@Header("Session") String sessionToken) throws NetworkException;

    /**
     * Get the current user's reportbacks.
     *
     * @param sessionToken Token for the currently logged in user
     * @return ResponseProfileReportbacks
     * @throws NetworkException
     */
    @GET("/profile/reportbacks")
    ResponseProfileReportbacks userProfileReportbacks(@Header("Session") String sessionToken) throws NetworkException;

    /**
     * Get the current user's signups.
     *
     * @param sessionToken Token for the currently logged in user
     * @return ResponseProfileSignups
     * @throws NetworkException
     */
    @GET("/profile/signups")
    ResponseProfileSignups userProfileSignups(@Header("Session") String sessionToken) throws NetworkException;

    /**
     * Update profile of the currently logged in user.
     *
     * @param sessionToken Token for currently logged in user
     * @param user Profile updates to submit
     * @return ResponseUserUpdate
     * @throws NetworkException
     */
    @Headers("Content-Type: application/json")
    @POST("/profile")
    ResponseUserUpdate updateUser(@Header("Session") String sessionToken,
                                  @Body TypedInput user) throws NetworkException;

    /**
     * Update profile of the currently logged in user with a Parse installation ID.
     *
     * @param sessionToken Token for currently logged in user
     * @param parseInstallationRequest ParseInstallationRequest
     * @return ResponseUserUpdate
     * @throws NetworkException
     */
    @Headers("Content-Type: application/json")
    @POST("/profile")
    ResponseUserUpdate setParseInstallationId(@Header("Session") String sessionToken,
                                              @Body ParseInstallationRequest parseInstallationRequest) throws NetworkException;

    /**
     * Logs the user out of Northstar by invalidating its session token.
     *
     * HACK: see hackEmptyBody param
     *
     * @param sessionToken Token to invalidate
     * @param hackEmptyBody Without body data, Retrofit errors out because it expects POST requests
     *                      to have a non-empty body
     * @return Response
     * @throws NetworkException
     */
    @POST("/auth/invalidate")
    Response logout(@Header("Session") String sessionToken,
                    @Body String hackEmptyBody) throws NetworkException;

    /**
     * Submit a reportback for the logged in user.
     *
     * @param sessionToken Token for the currently logged in user
     * @param requestreportback
     * @return
     * @throws NetworkException
     */
    @Headers("Content-Type: application/json")
    @POST("/reportbacks")
    ResponseSubmitReportBack submitReportback(@Header("Session") String sessionToken,
                                              @Body RequestReportback requestreportback) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/user/campaigns/{id}")
    ResponseRbData getRbData(@Header("Session") String sessionToken,
                             @Path("id") int campId) throws NetworkException;

    /**
     * Sign up the logged in user for a campaign.
     *
     * @param sessionToken Token for the currently logged in user
     * @param requestCampaignSignup
     * @return ResponseCampaignSignUp
     * @throws NetworkException
     */
    @Headers("Content-Type: application/json")
    @POST("/signups")
    ResponseCampaignSignUp submitSignUp(@Header("Session") String sessionToken,
                                        @Body RequestCampaignSignup requestCampaignSignup) throws NetworkException;

    @Headers("Content-Type: application/json")
    @GET("/users/_id/{id}/campaigns")
    ResponseUserCampaign getUserCampaigns(@Path("id") String id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/kudos")
    ResponseReportBack submitKudos(@Body RequestKudo requestKudo,
                                   @Header("Session") String sessionToken) throws NetworkException;

    @Multipart
    @POST("/users/{id}/avatar")
    ResponseAvatar uploadAvatar(@Path("id") String id,
                                @Part("photo") TypedFile file) throws NetworkException;

}

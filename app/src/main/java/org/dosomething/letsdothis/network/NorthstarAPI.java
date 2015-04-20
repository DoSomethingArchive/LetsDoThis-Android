package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.network.models.ResponseSignup;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserList;
import org.dosomething.letsdothis.network.models.ResponseUserUpdate;

import java.util.Date;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public interface NorthstarAPI
{

    public static final String BASE_URL = "https://api.dosomething.org/v1/";

    @FormUrlEncoded
    @POST("/login")
    ResponseLogin loginWithMobile(@Field("mobile") String mobile, @Field(
            "password") String password) throws NetworkException;

    @FormUrlEncoded
    @POST("/login")
    ResponseLogin loginWithEmail(@Field("email") String email, @Field(
            "password") String password) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/users")
    ResponseSignup registerWithEmail(@Body User user) throws NetworkException;

    @Headers("Content-Type: application/json")
    @POST("/users")
    ResponseSignup registerWithMobile(@Body User json) throws NetworkException;

    @GET("/users")
    ResponseUserList userList(@Query("page") int page, @Query(
            "limit") int limit) throws NetworkException;

    @GET("/users/_id/{id}")
    ResponseUser[] userProfile(@Path("id") String id) throws NetworkException;

    @Headers("Content-Type: application/json")
    @PUT("/users/{id}")
    ResponseUserUpdate updateUser(@Path("id") String id, @Body TypedInput user) throws NetworkException;

    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    @POST("/logout")
    public Response logout(@Header("Session") String sessionToken) throws NetworkException;

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

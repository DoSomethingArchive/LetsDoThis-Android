package org.dosomething.letsdothis.network;
import org.dosomething.letsdothis.network.models.UserListResponse;
import org.dosomething.letsdothis.network.models.UserResponse;

import java.util.Date;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public interface NorthstarAPI
{

    public static final String BASE_URL = "https://api.dosomething.org/v1/";

    @FormUrlEncoded
    @POST("/login")
    Response loginWithMobile(@Field("mobile") String mobile, @Field(
            "password") String password);

    @FormUrlEncoded
    @POST("/login")
    Response loginWithEmail(@Field("email") String email, @Field(
            "password") String password);

    @Headers("Content-Type: application/json")
    @POST("/users")
    Response registerWithEmail(@Body String json);

    @Headers("Content-Type: application/json")
    @POST("/users")
    Response registerWithMobile(@Body String json);

    @GET("/users")
    UserListResponse userList(@Query("page") int page, @Query("limit") int limit);

    //----------------Retrieve User
    @GET("/users/_id/{id}")
    UserResponse[] userProfile(@Path("id") String id);


    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    //-----------NOT DONE
    @POST("/logout")
    public Response logout(@Header("Session") String sessionToken);

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
            "sat_writing") int satWriting);

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
            "sat_writing") int satWriting);

}

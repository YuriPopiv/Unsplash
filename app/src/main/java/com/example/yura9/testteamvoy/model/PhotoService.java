package com.example.yura9.testteamvoy.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yura9 on 4/4/2017.
 */

public interface PhotoService {

    @GET("oauth/authorize")
    Call<TokenResponce> authorize(@Query("client_id") String client_id, @Query("redirect_uri") String redirect_uri,
                                  @Query("response_type") String response_type, @Query("scope") String[] scope);

    @POST("oauth/token")
    Call<TokenResponce> getToken(@Query("client_id") String client_id, @Query("client_secret") String client_secret,
                                 @Query("redirect_uri") String redirect_uri,
                                 @Query("code") String code, @Query("grant_type") String grant_type);

    @GET("photos")
    Call<List<Photo>> getPhotos(@Header("Authorization")String s, @Query("page") int page, @Query("order_by") String orderBy);

    @GET("/photos/random")
    Call<Photo> getRandom(@Header("Authorization")String s);

    @POST("/photos/{id}/like")
    Call<Photo> likePhoto(@Header("Authorization")String s, @Path("id") String id);

    @DELETE("/photos/{id}/like")
    Call<Photo> dislikePhoto(@Header("Authorization")String s, @Path("id") String id);

    @GET("/me")
    Call<Photo> getMe(@Header("Authorization")String s);

}

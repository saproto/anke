package com.proto.oauth;

import com.proto.home.Article;
import com.proto.home.ArticleEntry;
import com.proto.home.NewsEntry;
import com.proto.service.MyResponse;
import com.proto.user.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Dennis on 19/03/2018.
 */

public interface SAProtoClient {
    @POST("oauth/token")
    @FormUrlEncoded
    Call<AuthorizationToken> getAuthToken(@Field("grant_type") String grantType, @Field("client_id") String clientId,@Field("client_secret") String clientSecret, @Field("redirect_uri") String redirectUri,@Field("code") String code);

    @GET("api/user/info")
    Call<User> getUser(@Header("Authorization") String token);

    /*@GET("api/user/info")
    Call<ResponseBody> getUser(@Header("Authorization") String token);
*/

    @GET("api/user/info")
    Call<MyResponse> getUserPlane(@Header("Authorization") String token);

    @GET("api/user/info")
    void getUserPlane2(@Header("Authorization") String token, Callback<MyResponse> callback);

    @GET("api/protoink")
    Call<ArticleEntry[]> getProtoInkArticles();

}

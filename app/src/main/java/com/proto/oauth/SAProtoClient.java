package com.proto.oauth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Dennis on 19/03/2018.
 */

public interface SAProtoClient {
    @POST("oauth/token")
    @FormUrlEncoded
    Call<AuthorizationToken> getAuthToken(@Field("grant_type") String grantType, @Field("client_id") String clientId,@Field("client_secret") String clientSecret, @Field("redirect_uri") String redirectUri,@Field("code") String code);


}

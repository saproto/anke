package com.proto.oauth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 20/03/2018.
 */

public class AuthorizationToken {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private String expiresIn;

    public String getAccessToken(){
        return accessToken.toString();
    }
}

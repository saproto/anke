package com.proto.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 20/03/2018.
 */

public class User {

    @SerializedName("name")
    private String name;

    @SerializedName("calling_name")
    private String callingName;

    @SerializedName("email")
    private String email;

    public String getName() {
        return name;
    }

    public String getCallingName() {
        return callingName;
    }

    public String getEmail() {
        return email;
    }
}

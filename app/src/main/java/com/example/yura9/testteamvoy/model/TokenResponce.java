package com.example.yura9.testteamvoy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yura9 on 4/4/2017.
 */

public class TokenResponce {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("token_type")
    private String token_type;

    @SerializedName("scope")
    private String scope;

    @SerializedName("created_at")
    private String created_at;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public String getCreated_at() {
        return created_at;
    }

}

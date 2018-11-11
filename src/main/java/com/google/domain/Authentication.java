package com.google.domain;

import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Authentication {

    @Id
    @SerializedName("id")
    private String authenticationId;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_refresh_interval_sec")
    private long tokenRefreshTime;

    private Date createdAt;

    private Date expirationDate;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public void setTokenRefreshTime(long tokenRefreshTime) {
        this.tokenRefreshTime = tokenRefreshTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenRefreshTime() {
        return tokenRefreshTime;
    }

}

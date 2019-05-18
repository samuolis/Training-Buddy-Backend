package com.google.domain;

import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreLoad;

import java.util.Date;
import java.util.List;

@Entity
public class User {

    @SerializedName("userId")
    @Id
    private String userId;

    @Ignore
    @IgnoreLoad
    private int id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("createdOn")
    private Date createdAt = new Date();

    @SerializedName("profilePictureIndex")
    private int profilePictureIndex;

    private List<Long> signedEventsList;

    public List<Long> getSignedEventsList() {
        return signedEventsList;
    }

    public void setSignedEventsList(List<Long> signedEventsList) {
        this.signedEventsList = signedEventsList;
    }

    public int getProfilePictureIndex() {
        return profilePictureIndex;
    }

    public void setProfilePictureIndex(int profilePictureIndex) {
        this.profilePictureIndex = profilePictureIndex;
    }

    public String getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

package com.google.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event {

    @Id
    private Long eventId;

    @Index
    private String userId;

    private String eventName;

    private String eventDescription;

    private String eventLocationName;

    @Index
    private Double eventLocationLongitude;

    @Index
    private Double eventLocationLatitude;

    @Index
    private String eventLocationCountryCode;

    @Index
    private int eventPlayers;

    @Index
    private Date eventDate;

    @Ignore
    private Float eventDistance;

    @Ignore
    private String signedUserId;

    private List<User> eventSignedPlayers;

    private List<CommentMessage> eventComments;

    public List<CommentMessage> getEventComments() {
        return eventComments;
    }

    public void setEventComments(List<CommentMessage> eventComments) {
        this.eventComments = eventComments;
    }

    public String getSignedUserId() {
        return signedUserId;
    }

    public void setSignedUserId(String signedUserId) {
        this.signedUserId = signedUserId;
    }

    public List<User> getEventSignedPlayers() {
        return eventSignedPlayers;
    }

    public void setEventSignedPlayers(List<User> eventSignedPlayers) {
        this.eventSignedPlayers = eventSignedPlayers;
    }

    public Float getEventDistance() {
        return eventDistance;
    }

    public void setEventDistance(Float eventDistance) {
        this.eventDistance = eventDistance;
    }

    public String getEventLocationCountryCode() {
        return eventLocationCountryCode;
    }

    public void setEventLocationCountryCode(String eventLocationCountryCode) {
        this.eventLocationCountryCode = eventLocationCountryCode;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public Double getEventLocationLongitude() {
        return eventLocationLongitude;
    }

    public void setEventLocationLongitude(Double eventLocationLongitude) {
        this.eventLocationLongitude = eventLocationLongitude;
    }

    public Double getEventLocationLatitude() {
        return eventLocationLatitude;
    }

    public void setEventLocationLatitude(Double eventLocationLatitude) {
        this.eventLocationLatitude = eventLocationLatitude;
    }

    public int getEventPlayers() {
        return eventPlayers;
    }

    public void setEventPlayers(int eventPlayers) {
        this.eventPlayers = eventPlayers;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    private String getEventDateToString(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return formatter.format(this.eventDate);
    }

    private ArrayList<String> getSignedPlayersInString(){
        ArrayList<String> newArrayList = new ArrayList<String>();
        this.eventSignedPlayers.forEach((userIdForEach) -> {
            newArrayList.add(userIdForEach.toString());
        });
        return newArrayList;
    }

    private ArrayList<String> getArrayCommentsInString(){
        ArrayList<String> newArrayList = new ArrayList<String>();
        if (eventComments == null){
            eventComments = new ArrayList<CommentMessage>();
        }
        this.eventComments.forEach((comment) -> {
            newArrayList.add(comment.toString());
        });
        return newArrayList;
    }

    @Override
    public String toString() {
        return "{" + "\"eventId\": \"" + this.eventId + "\", \"userId\": \"" + this.userId +
                "\", \"eventDescription\": \"" + this.eventDescription + "\", \"eventLocationName\": \"" + this.eventLocationName +
                "\", \"eventLocationLongitude\": \"" + this.eventLocationLongitude + "\", \"eventLocationLatitude\": \"" + this.eventLocationLatitude +
                "\", \"eventLocationCountryCode\": \"" + this.eventLocationCountryCode + "\", \"eventPlayers\": \"" + this.eventPlayers +
                "\", \"eventDate\": \"" + getEventDateToString() + "\", \"eventDistance\": \"" + this.eventDistance +
                "\", \"signedUserId\": \"" + this.signedUserId + "\", \"eventSignedPlayers\": " + getSignedPlayersInString().toString() +
                ", \"eventComments\": " + getArrayCommentsInString() + ", \"eventName\": \"" + this.eventName + "\"}";
    }
}

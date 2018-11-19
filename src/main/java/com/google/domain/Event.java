package com.google.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
}
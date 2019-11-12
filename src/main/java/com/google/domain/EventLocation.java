package com.google.domain;

public class EventLocation {

    private String eventLocationName;

    private String eventLocationCountryCode;

    private Double eventLocationLatitude;

    private Double eventLocationLongitude;

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public String getEventLocationCountryCode() {
        return eventLocationCountryCode;
    }

    public void setEventLocationCountryCode(String eventLocationCountryCode) {
        this.eventLocationCountryCode = eventLocationCountryCode;
    }

    public Double getEventLocationLatitude() {
        return eventLocationLatitude;
    }

    public void setEventLocationLatitude(Double eventLocationLatitude) {
        this.eventLocationLatitude = eventLocationLatitude;
    }

    public Double getEventLocationLongitude() {
        return eventLocationLongitude;
    }

    public void setEventLocationLongitude(Double eventLocationLongitude) {
        this.eventLocationLongitude = eventLocationLongitude;
    }

    @Override
    public String toString() {
        return "EventLocation{" +
                "eventLocationName='" + eventLocationName + '\'' +
                ", eventLocationCountryCode='" + eventLocationCountryCode + '\'' +
                ", eventLocationLatitude=" + eventLocationLatitude +
                ", eventLocationLongitude=" + eventLocationLongitude +
                '}';
    }
}

package com.google.domain;

public class CommentMessage {

    private String userId;

    private Long eventId;

    private String messageText;

    private String messageTime;

    private String messageUserName;

    public String getMessageUserName() {
        return messageUserName;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        return "{" +
                "\"eventId\": \"" + this.eventId +
                "\", \"userId\": \"" + this.userId +
                "\", \"messageText\": \"" + this.messageText +
                "\", \"messageTime\": \"" + this.messageTime +
                "\", \"messageUserName\": \"" + this.messageUserName +
                "\"}";
    }
}

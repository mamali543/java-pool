package com.ader.sockets.models;

public class ChatMessage {
    private String message;
    private Long fromId;
    private Long roomId;

        // Default constructor
    public ChatMessage() {
    
    }
    public ChatMessage(String message, Long fromId, Long roomId) {
        this.message = message;
        this.fromId = fromId;
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", fromId=" + fromId +
                ", roomId=" + roomId +
                '}';
    }
}

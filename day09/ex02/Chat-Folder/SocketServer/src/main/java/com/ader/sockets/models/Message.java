package com.ader.sockets.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private Long id;
    private Long authorId;
    private Long roomId;
    private String message;
    private LocalDateTime time;

    public Message(){

    }
    public Message(Long id, Long authorId, Long roomId, String message, LocalDateTime dateTime){
        this.id = id;
        this.authorId = authorId;
        this.roomId = roomId;
        this.message = message;
        this.time = dateTime;
    }

    public Long getId(){return this.id;}
    public Long getAuthorId(){return this.authorId;}
    public String getMessage(){return this.message;}
    public Long getRoomId(){return this.roomId;}
    public LocalDateTime getDateTime() {return time;}

    public void setId(Long id){this.id = id;}
    public void setAuthorId(Long authorId){this.authorId = authorId;}
    public void setRoomId(Long roomId){this.roomId = roomId;}
    public void setMessage(String message){this.message = message;}
    public void setLocalDateTime(LocalDateTime time){
        this.time = time;
    }

    //equals method
    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     //getClass() returns the runtime class of the object
    //     if (o == null || getClass() != o.getClass()) return false;
    //     Message message = (Message) o;
    //     return Objects.equals(id, message.id) &&
    //             Objects.equals(author, message.author) &&
    //             // Objects.equals(room, message.room) &&
    //             Objects.equals(message, message.message) &&
    //             Objects.equals(time, message.time);
    // }
    //hashCode method Returns a hash code value for the object, which is used in hash-based collections like HashSet, HashMap, and HashTable.
    @Override
    public int hashCode() {
        return Objects.hash(id, authorId,roomId,  message, time);
    }
    //toString method Returns a string representation of the object, which is intended for human reading
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + id +
                ", authorId='" + authorId + '\'' +
                ", roomId='" + roomId+ '\'' +
                ", text=" + message +
                ", dateTime=" + time +
                '}';
    }
}
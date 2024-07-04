package mr.school1337.chat.models;

import java.time.LocalDateTime;
public class Message {
    private Long id;
    private User author;
    private Chatroom room;
    private String message;
    private LocalDateTime time;
    public void Message(){

    }
    public void Message(Long id, User author, Chatroom room, String message, LocalDateTime dateTime){
        this.id = id;
        this.author = author;
        this.room = room;
        this.message = message;
        this.time = dateTime;
    }

    public Long getId(){return this.id;}
    public User getUser(){return this.author;}
    public String getMessage(){return this.message;}
    public Chatroom getRoom(){return this.room;}
    public LocalDateTime getDateTime() {return dateTime;}

    public void setId(Long id){this.id = id;}
    public void setAuthor(User author){this.author = author;}
    public void setRoom(Chatroom room){this.room = room;}
    public void setMessage(String message){this.message = message;}
    public void setLocalDateTime(LocalDateTime time){
        this.time = time;
    }

    //equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //getClass() returns the runtime class of the object
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(author, message.author) &&
                Objects.equals(room, message.room) &&
                Objects.equals(message, message.message) &&
                Objects.equals(time, message.time);
    }
    //hashCode method Returns a hash code value for the object, which is used in hash-based collections like HashSet, HashMap, and HashTable.
    @Override
    public int hashCode() {
        return Objects.hash(id, author, room, message, time);
    }
    //toString method Returns a string representation of the object, which is intended for human reading
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + id +
                ", author='" + author + '\'' +
                ", room='" + room + '\'' +
                ", text=" + text +
                ", dateTime=" + time +
                '}';
    }
}

package com.ader.sockets.models;
import java.util.List;
import com.ader.sockets.models.Message;
import com.ader.sockets.models.User;
import java.util.Objects;

public class Chatroom {
    private Long chatroom_id;
    private String chatroom_name;
    private Long chatroom_ownerId;
    private List<Message> chatroom_messages;
    private List<User> chatroom_users;

    public Chatroom(Long chatroomid, String chatroomname, Long chatroomownerid, List<Message> chatroommessages, List<User> chatroomusers){
        this.chatroom_id = chatroomid;
        this.chatroom_name = chatroomname;
        this.chatroom_ownerId = chatroomownerid;
        this.chatroom_messages = chatroommessages;
        this.chatroom_users = chatroomusers;
    }

    public Long getId(){return this.chatroom_id;}
    public String getName(){return this.chatroom_name;}
    public Long getOwner(){return this.chatroom_ownerId;}
    public List<Message> getMessages(){return this.chatroom_messages;}
    public List<User> getUsers(){return this.chatroom_users;}

    public void setId(Long id){
        this.chatroom_id = id;
    }

    public void setName(String name){
        this.chatroom_name = name;
    }

    public void setOwner(Long owner){
        this.chatroom_ownerId = owner;
    }
    
    public void setMessages(List<Message> messages){
        this.chatroom_messages = messages;
    }

    public void setUsers(List<User> users){
        this.chatroom_users = users;
    }
    //equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //getClass() returns the runtime class of the object
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        return Objects.equals(chatroom_id, chatroom.chatroom_id) &&
                Objects.equals(chatroom_name, chatroom.chatroom_name) &&
                Objects.equals(chatroom_ownerId, chatroom.chatroom_ownerId) &&
                Objects.equals(chatroom_messages, chatroom.chatroom_messages) &&
                Objects.equals(chatroom_users, chatroom.chatroom_users) ;
    }
    //hashCode method Returns a hash code value for the object, which is used in hash-based collections like HashSet, HashMap, and HashTable.
    @Override
    public int hashCode() {
        return Objects.hash(chatroom_id, chatroom_name, chatroom_ownerId, chatroom_messages, chatroom_users);
    }
    //toString method Returns a string representation of the object, which is intended for human reading
    @Override
    public String toString() {
        return "Chatroom{" +
                "chatroomId=" + chatroom_id +
                ", name='" + chatroom_name + '\'' +
                ", owner='" + chatroom_ownerId + '\'' +
                ", messages=" + chatroom_messages +
                ", users=" + chatroom_users +
                '}';
    }
}

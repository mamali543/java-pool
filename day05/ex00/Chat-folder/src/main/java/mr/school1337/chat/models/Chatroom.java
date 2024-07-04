package mr.school1337.chat.models;
import java.util.List;
import mr.school1337.chat.models.Messages;
public class Chatroom {
    private Long chatroom_id;
    private String chatroom_name;
    private String chatroom_owner;
    private List<Messages> chatroom_messages;
    public void Chatroom(Long chatroomid, String chatroomname, String chatroomowner, List<Messages> chatroommessages){
        this.chatroom_id = chatroomid;
        this.chatroom_name = chatroomname;
        this.chatroom_owner = chatroomowner;
        this.chatroom_messages = chatroommessages;
    }

    public Long getId(){return this.chatroom_id};
    public String getName(){return this.chatroom_name};
    public String getOwner(){return this.chatroom_owner};
    public Messages getMessages(){return this.chatroom_messages};

    public void setId(Long id){
        this.chatroom_id = id;
    }
    public void setName(String name){
        this.chatroom_name = name;
    }
    public void setOwner(String owner){
        this.chatroom_owner = owner;
    }
    public void setMessages(List<Messages> messages){
        this.chatroom_messages = messages;
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
                Objects.equals(chatroom_owner, chatroom.chatroom_owner) &&
                Objects.equals(chatroom_messages, chatroom.chatroom_messages);
    }
    //hashCode method Returns a hash code value for the object, which is used in hash-based collections like HashSet, HashMap, and HashTable.
    @Override
    public int hashCode() {
        return Objects.hash(chatroom_id, chatroom_name, chatroom_owner, chatroom_messages);
    }
    //toString method Returns a string representation of the object, which is intended for human reading
    @Override
    public String toString() {
        return "Chatroom{" +
                "chatroomId=" + chatroom_id +
                ", name='" + chatroom_name + '\'' +
                ", owner='" + chatroom_owner + '\'' +
                ", messages=" + chatroom_messages +
                '}';
    }
}

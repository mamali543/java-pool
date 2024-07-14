package mr.school1337.chat.models;
import java.util.Objects;
import java.util.List;
import java.util.Objects;


public class User {
    private Long user_id;
    private String user_login;
    private String user_password;
    private List<Chatroom> user_chatrooms;
    private List<Chatroom> user_socializedChatrooms;

    public User(Long id, String login, String password, List<Chatroom> userChatrooms, List<Chatroom> userSocializedChatrooms){
        this.user_id = id;
        this.user_login = login;
        this.user_password = password;
        this.user_chatrooms = userChatrooms;
        this.user_socializedChatrooms = userSocializedChatrooms;
    }
    public Long getId(){return this.user_id;}
    public String getLogin(){return this.user_login;}
    public String getPassword(){return this.user_password;}
    public List<Chatroom> getChatrooms(){return this.user_chatrooms;}
    public List<Chatroom> getSocializedChatrooms(){return this.user_socializedChatrooms;}
    public void setId(Long id){
        this.user_id = id;
    }
    public void setLogin(Long id){
        this.user_id = id;
    }
    public void setPassword(Long id){
        this.user_id = id;
    }
    public void setUserChatrooms(List<Chatroom> userChatrooms){
        this.user_chatrooms = userChatrooms;
    }
    public void setUserSocializedChatrooms(List<Chatroom> socializedChatrooms){
        this.user_socializedChatrooms = socializedChatrooms;
    }
    //equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
    //getClass() returns the runtime class of the object
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(user_id, user.user_id) &&
                Objects.equals(user_login, user.user_login) &&
                Objects.equals(user_password, user.user_password) &&
                Objects.equals(user_chatrooms, user.user_chatrooms) &&
                Objects.equals(user_socializedChatrooms, user.user_socializedChatrooms);
    }
    //hashCode method Returns a hash code value for the object, which is used in hash-based collections like HashSet, HashMap, and HashTable.
    @Override
    public int hashCode() {
        return Objects.hash(user_id, user_login, user_password, user_chatrooms, user_socializedChatrooms);
    }
    //toString method Returns a string representation of the object, which is intended for human reading
    @Override
    public String toString() {
        return "User{" +
                "userId=" + user_id +
                ", login='" + user_login + '\'' +
                ", password='" + user_password + '\'' +
                ", createdRooms=" + user_chatrooms +
                ", socializedChatrooms=" + user_socializedChatrooms +
                '}';
    }
}

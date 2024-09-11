package main.java.com.ader.models;

public class User {

    private Long userId;
    private String userEmail;
    private String userPassword;

    public User(Long userId, String userEmail,  String userPassword)
    {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    public User(Long userId, String userPassword)
    {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    };
}
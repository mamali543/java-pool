package main.java.com.ader.models;

public class User {

    private Long userId;
    // private String userName;
    private String userEmail;

    // public User(Integer userId, String userName,  String userEmail)
    // {
    //     this.userId = userId;
    //     this.userEmail = userEmail;
    //     this.userName = userName;
    // }

    public User(Long userId, String userEmail)
    {
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // public void setUserName(String userName) {
    //     this.userName = userName;
    // }

    // public String getUserName() {
    //     return userName;
    // }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                '}';
    };
}
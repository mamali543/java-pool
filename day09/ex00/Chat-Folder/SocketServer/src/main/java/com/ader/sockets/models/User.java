package com.ader.sockets.models;

/**
 * User
 */
public class User {

    private Long userId;
    private String username;
    private String password;

    public User(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;   
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [userID= " + userId+ " password=" + password + ", username=" + username + "]";
    }
}
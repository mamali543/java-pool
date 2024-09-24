 package com.ader.sockets.service;

 import com.ader.sockets.models.User;
 
 public interface UserService {
    boolean SignUp(User user);
    boolean SignIn(User user);
    User getUser(String username);
}
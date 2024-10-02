 package com.ader.sockets.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.User;
import com.ader.sockets.repositories.UsersRepository;

@Component("userService")
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private PasswordEncoder encoder;

    public UserServiceImpl(@Qualifier("usersRepository") UsersRepository usersRepository, 
    @Qualifier("encooder") PasswordEncoder encoder) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean SignUp(User user) {
        Optional<User> user1 = usersRepository.findByName(user.getUsername());
        if (user1.isPresent()) {
            return false;
        }
        user.setPassword(encoder.encode(user.getUserPassword()));   
        usersRepository.save(user);
        return true;
    }

    @Override
    public boolean SignIn(User user){
        Optional<User> user1 = usersRepository.findByName(user.getUsername());
        if (!(user1.isPresent())) {
            return false;
        }
        return encoder.matches(user.getUserPassword(), user1.get().getUserPassword());
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = usersRepository.findByName(username);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }
    
}
package main.java.mr.school1337.repositories;

import main.java.mr.school1337.models.User;

public interface UserRepositorie {
    User findByLogin(String login);
    void update(User user);
}
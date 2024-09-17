package com.ader.sockets.repositories;

/**
 * UsersRepositoryJdbcImpl
 */
public class UsersRepositoryJdbcImpl implements UsersRepository {

    @Override
    public void signUp(String username) {
        System.out.println("User " + username + " signed up!");
    }

}
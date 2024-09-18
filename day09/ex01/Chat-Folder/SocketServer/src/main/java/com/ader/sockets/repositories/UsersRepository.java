package com.ader.sockets.repositories;

import java.util.Optional;

import com.ader.sockets.models.User;

 public interface UsersRepository extends CrudRepositorie<User> {
    Optional<User> findByName(String username);
 }
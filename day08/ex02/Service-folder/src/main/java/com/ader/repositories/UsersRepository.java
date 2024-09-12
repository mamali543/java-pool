package com.ader.repositories;

import java.util.Optional;
import com.ader.repositories.CrudRepository;

import com.ader.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
}
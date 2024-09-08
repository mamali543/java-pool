package main.java.com.ader.repositories;

import java.util.Optional;
import main.java.com.ader.interfaces.CrudRepository;

import main.java.com.ader.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);    
}
package com.ader.services;

import com.ader.models.User;
import com.ader.repositories.UsersRepository;
import com.ader.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UsersServicesImpl implements UsersService{

    private UsersRepository repositorie;
    private UsersRepository repositorieTemplate;
    //@Qualifier to specify which bean should be injected where, Spring will wire the correct DataSource for each CRUD implementation.
    public UsersServicesImpl(@Qualifier("UsersRepositoryTemplate") UsersRepository repositorieTemplate, @Qualifier("UsersRepository") UsersRepository repositorie)
    {
        this.repositorie = repositorie;
        this.repositorieTemplate = repositorieTemplate;
    }
    @Override
    /**
     * Signs up a new user with the given email address.
     *
     * @param email The email address of the new user.
     * @return The generated password for the new user if sign-up is successful.
     * @throws RuntimeException If the user creation fails.
     */
    public String signUp(String email)
    {
        String password = UUID.randomUUID().toString();
        User user = new User(null, email, password);
        repositorieTemplate.save(user);
        return password;

    }
}
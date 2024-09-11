package main.java.com.ader.services;

import main.java.com.ader.repositories.UsersRepository;
import main.java.com.ader.services.UsersService;
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
        boolean isCreated = repositorie.save(email, password);
        if (isCreated) {
            return password;
        } else {
            throw new RuntimeException("Failed to create user");
        }
    }
}
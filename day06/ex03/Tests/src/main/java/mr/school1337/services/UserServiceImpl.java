package main.java.mr.school1337.services;


import main.java.mr.school1337.exceptions.AlreadyAuthenticatedException;
import main.java.mr.school1337.models.User;
import main.java.mr.school1337.repositories.UserRepositorie;

public class UserServiceImpl {
    
    private UserRepositorie userRepositorie;

    public UserServiceImpl(UserRepositorie userRepositorie)
    {
        this.userRepositorie = userRepositorie;
    }

    public boolean authenticate(String login, String password){

        User user = userRepositorie.findByLogin(login);
        if (user != null)
        {
            if (user.isAuthenticated())
                throw new AlreadyAuthenticatedException();
            if (user.getPassword().equals(password))
            {
                user.setAuthenticated(true);
                userRepositorie.update(new User(user.getId(),user.getLogin() ,user.getPassword(), true));
                return true;
            }
        }
        return false;
    }
}

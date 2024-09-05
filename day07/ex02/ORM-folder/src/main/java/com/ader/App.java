package com.ader;

import java.lang.foreign.Linker.Option;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ader.models.User;
import com.ader.services.OrmManager;

public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Set<Class<?>> classes =  new HashSet<>();
        classes.add(User.class);

        try {
            OrmManager ormManager = new OrmManager(classes);
            List<User> users = List.of(
                new User(null, "reda", "ader", 25, "reda@email.com"),
                new User(null, "iliass", "amali", 21, "iliass@email.com"),
                new User(null, "rayan", "amali", 13, "rayan@email.com")
            );

            for (User u: users)
            {
                ormManager.save(u);
            }
            Optional<User> user =  ormManager.findById(1L, User.class);
            if (user.isPresent())
            {
                User u = user.get();
                logger.info("<<the user retrieved>>: "+u.toString());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

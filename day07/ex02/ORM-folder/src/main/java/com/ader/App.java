package com.ader;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.ader.models.User;
import com.ader.services.OrmManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Set<Class<?>> classes =  new HashSet<>();
        classes.add(User.class);

        try {
            OrmManager ormManager = new OrmManager(classes);
        }
        catch (SQLException e)
        {
            System.err.println("SQLException caught ...");
        }
    }
}

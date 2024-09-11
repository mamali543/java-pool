package com.ader;

import main.java.com.ader.services.UsersServicesImpl;
import main.java.com.ader.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


//Transition to Java-Based Configuration:
public class App 
{
    public static void main( String[] args )
    {
        //a Spring container that supports Java-based configuration using @Configuration classes (instead of XML configuration).
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ApplicationConfig.context);
        ctx.refresh();

    }
}

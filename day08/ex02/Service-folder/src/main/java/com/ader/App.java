package com.ader;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ader.repositories.UsersRepositoryJdbcImpl;
import com.ader.services.UsersServicesImpl;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;



// Transition to Java-Based Configuration:
public class App {
    public static void main(String[] args) {
        // A Spring container that supports Java-based configuration using @Configuration classes (instead of XML configuration).
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        UsersServicesImpl service = ctx.getBean(UsersServicesImpl.class);
        UsersRepositoryJdbcImpl repo = ctx.getBean(UsersRepositoryJdbcImpl.class);
        System.out.println(">>>>Password: "+service.signUp("amalyreda@gmail.com"));
        System.out.println(">>>>Password: "+service.signUp("amalyelias@gmail.com"));
        System.out.println(repo.findAll());
        ctx.close();
    }
}

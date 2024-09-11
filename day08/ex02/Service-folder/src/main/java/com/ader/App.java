package com.ader;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ader.ApplicationConfig;


// Transition to Java-Based Configuration:
public class App {
    public static void main(String[] args) {
        // A Spring container that supports Java-based configuration using @Configuration classes (instead of XML configuration).
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ApplicationConfig.class);  // Correct import should resolve here
        ctx.refresh();

        UsersServicesImpl service = ctx.getBean(UsersServicesImpl.class);
        System.out.println(service.signUp("amalyreda@gmail.com"));
        ctx.close();
    }
}

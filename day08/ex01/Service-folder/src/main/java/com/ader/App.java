package com.ader;

import main.java.com.ader.repositories.UsersRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UsersRepository usersRepository = context.getBean("UsersRepositoryImpl", UsersRepository.class);
        System.out.println(usersRepository.findAll());
        usersRepository = context.getBean("UsersRepositoryTemplateImpl", UsersRepository.class);
        System.out.println(usersRepository.findAll());
    }
}

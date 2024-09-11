package com.ader.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"main.java.com.ader.repositories"}, {"main.java.com.ader.services"}) //automatically registers them as beans.
@PropertySource("classpath:db.properties")
public class ApplicationConfig {

    //The Environment object is automatically injected by Spring. It provides access to the environment properties loaded from db.properties. These properties are then used in the DataSource configurations.
    @Autowired
    //Autowired is a spring annotation that allows for dependency injection, it tells spring to inject the Environment bean from the spring context
    Environment env;
    
    @Bean
    public HickariDataSource hickariDataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        return dataSource;
    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        return dataSource;
    }
}
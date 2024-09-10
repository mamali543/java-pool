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
@ComponentScan({"main.java.com.ader.repositories"}, {"main.java.com.ader.services"})
@PropertySource("classpath:db.properties")
public class ApplicationConfig {

    @Autowired
    Environment env;
    
    @Bean
    public HickariDataSource hickariDataSource()
    {

    }

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
    }
}
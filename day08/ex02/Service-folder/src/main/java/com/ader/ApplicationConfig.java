package com.ader;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.ader.repositories", "com.ader.services"})
@PropertySource("classpath:db.properties")
public class ApplicationConfig {

    // The Environment object is automatically injected by Spring.
    // It provides access to the environment properties loaded from db.properties.
    // These properties are then used in the DataSource configurations.
    // Note: Ensure that the package declaration at the top of this file is correct: package com.ader.config;
    @Autowired
    //Autowired is a spring annotation that allows for dependency injection, it tells spring to inject the Environment bean from the spring context, serverless
    Environment env;
    
    @Bean(destroyMethod = "close") // Ensure HikariCP is closed properly when the application shuts down
    @Qualifier("hikariDataSource")
    public HikariDataSource hickariDataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        return dataSource;
    }

    @Bean
    @Qualifier("driverManagerDataSource")
    public DriverManagerDataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        return dataSource;
    }
}
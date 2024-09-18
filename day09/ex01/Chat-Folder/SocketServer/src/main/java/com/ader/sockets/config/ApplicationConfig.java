package com.ader.sockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// ... existing imports and code ...
@Configuration
@ComponentScan(basePackages = {"com.ader.sockets.repositories", "com.ader.sockets.service", "com.ader.sockets.server"})
@PropertySource("classpath:db.properties")
public class ApplicationConfig{

    @Autowired  //Spring annotation for automatic dependency injection
    Environment env;

    @Bean
    @Qualifier("hikariDatasource")
    public HikariDataSource hikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver.name"));
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        return dataSource;
    }

    @Bean
    @Qualifier("encooder")
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
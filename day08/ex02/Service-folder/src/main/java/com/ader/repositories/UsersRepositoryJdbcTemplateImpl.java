package com.ader.repositories;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;
// import com.zaxxer.hikari.HikariDataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import com.ader.models.User;
import com.ader.repositories.UsersRepository;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Component("UsersRepositoryTemplate")
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryJdbcTemplateImpl(@Qualifier("driverManagerDataSource") DriverManagerDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlQuery = "SELECT FROM ex08.users WHERE userId = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[] { id }, new UserMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM ex08.users";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    @Override
    public void save(User entity) {
        System.out.println("wewe");
        String sqlQuery = "INSERT INTO ex08.users (userEmail, userPassword) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, entity.getUserEmail(), entity.getUserPassword());
    }

    @Override
    public void update(User entity) {
        String sqlQuery = "UPDATE ex08.users SET userEmail = ?, userPassword = ? WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, entity.getUserEmail(), entity.getUserPassword(), entity.getUserId());
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM ex08.users WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sqlQuery = "SELECT FROM ex08.users WHERE userEmail = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[] { email }, new UserMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
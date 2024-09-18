package com.ader.sockets.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.User;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * UsersRepositoryJdbcImpl
 */
@Component("usersRepository")
public class UsersRepositoryJdbcImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepositoryJdbcImpl(@Qualifier("hikariDatasource") HikariDataSource dataSource) {
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
        String sqlQuery = "INSERT INTO ex08.users (userName, userPassword) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, entity.getUsername(), entity.getUserPassword());
    }

    @Override
    public void update(User entity) {
        String sqlQuery = "UPDATE ex08.users SET userName = ?, userPassword = ? WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, entity.getUsername(), entity.getUserPassword(), entity.getUserId());
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM ex08.users WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<User> findByName(String username) {
        String sqlQuery = "SELECT * FROM ex08.users WHERE userName = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[] { username }, new UserMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
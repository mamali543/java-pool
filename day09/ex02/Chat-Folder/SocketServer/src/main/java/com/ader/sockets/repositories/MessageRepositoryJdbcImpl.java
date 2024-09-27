package com.ader.sockets.repositories;


import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.Message;
import com.ader.sockets.models.User;
import com.ader.sockets.repositories.MessageMapper;
import com.ader.sockets.repositories.MessageRepository;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Timestamp; // Make sure you have this import

import org.springframework.jdbc.support.GeneratedKeyHolder; // Import for KeyHolder
import org.springframework.jdbc.support.KeyHolder; // Import for KeyHolder
import java.sql.Connection; // Import for Connection
import java.sql.PreparedStatement; // Import for PreparedStatement
import java.sql.Statement; // Import for Statement
import java.sql.ResultSet; // Import for ResultSet
import java.sql.SQLException; // Import for SQLException

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * UsersRepositoryJdbcImpl
 */
@Component("messagesRepository")
public class MessageRepositoryJdbcImpl implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageRepositoryJdbcImpl(@Qualifier("hikariDatasource") HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // @Override
    // public Optional<Message> findById(Long id) {
    //     String sqlQuery = "SELECT FROM ex08.message WHERE messageId = ?";
    //     try {
    //         return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[] { id }, new MessageMapper()));
    //     } catch (EmptyResultDataAccessException e) {
    //         return Optional.empty();
    //     }
    // }

    @Override
    public List<Message> findAll() {
        String sqlQuery = "SELECT * FROM ex08.message";
        return jdbcTemplate.query(sqlQuery, new MessageMapper());
    }


    @Override
    public void save(Message message) {
        String sqlQuery = "INSERT INTO ex08.message (senderId, roomId, messageText, datetime) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery, message.getAuthorId(), message.getRoomId(), message.getMessage(), Timestamp.valueOf(message.getDateTime()));
    }

    @Override
    public void saveLong(Message message) {
        String sqlQuery = "INSERT INTO ex08.message (senderId, roomId, messageText, datetime) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder(); // Create a KeyHolder

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, message.getAuthorId());
            ps.setLong(2, message.getRoomId());
            ps.setString(3, message.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));
            return ps;
        }, keyHolder);

        // return keyHolder.getKey().longValue();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM ex08.message WHERE messageId = ?";
        jdbcTemplate.update(sql, id);
    }

    // @Override
    // public void update(User entity) {
    //     String sqlQuery = "UPDATE ex08.message SET userName = ?, userPassword = ? WHERE messageId = ?";
    //     jdbcTemplate.update(sqlQuery, entity.getUsername(), entity.getUserPassword(), entity.getmessageId());
    // }

    // @Override
    // public void delete(Long id) {
    //     String sqlQuery = "DELETE FROM ex08.message WHERE messageId = ?";
    //     jdbcTemplate.update(sqlQuery, id);
    // }

    // @Override
    // public Optional<User> findByName(String username) {
    //     String sqlQuery = "SELECT * FROM ex08.message WHERE userName = ?";
    //     try {
    //         return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[] { username }, new MessageMapper()));
    //     } catch (EmptyResultDataAccessException e) {
    //         return Optional.empty();
    //     }
    // }
}
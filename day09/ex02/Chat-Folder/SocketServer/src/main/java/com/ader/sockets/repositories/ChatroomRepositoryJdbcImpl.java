package com.ader.sockets.repositories;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.Chatroom;
import com.ader.sockets.models.Message;
import com.ader.sockets.models.User;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.jdbc.support.GeneratedKeyHolder; // For GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder; // For KeyHolder
import java.sql.Connection; // For Connection
import java.sql.PreparedStatement; // For PreparedStatement
import java.sql.Statement; // For Statement

import com.ader.sockets.repositories.ChatroomMapper;

import com.ader.sockets.repositories.ChatroomRepository;

import java.sql.Timestamp; // Make sure you have this import


import org.springframework.jdbc.core.JdbcTemplate;

/**
 * UsersRepositoryJdbcImpl
 */
@Component("chatroomRepository")
public class ChatroomRepositoryJdbcImpl implements ChatroomRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatroomRepositoryJdbcImpl(@Qualifier("hikariDatasource") HikariDataSource dataSource) {
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
    public List<Chatroom> findAll() {
        String sqlQuery = "SELECT * FROM ex08.charooms";
        return jdbcTemplate.query(sqlQuery, new ChatroomMapper());
    }

    @Override
    public void save(Chatroom chatroom) {

        String sqlQuery = "INSERT INTO ex08.chatrooms (chatroomName, chatroomOwnerId) VALUES ( ?, ?)";
        jdbcTemplate.update(sqlQuery, chatroom.getName(), chatroom.getOwner());
    }

    @Override
    public Long saveToGetId(Chatroom chatroom) { // Change return type to Long
        String sqlQuery = "INSERT INTO ex08.chatrooms (chatroomName, chatroomOwnerId) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // Create a KeyHolder
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, chatroom.getName());
            ps.setLong(2, chatroom.getOwner());
            return ps;
        }, keyHolder);
        // Ensure you are only returning the chatroomid
        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("chatroomid")) {
            return ((Number) keyHolder.getKeys().get("chatroomid")).longValue(); // Return the generated ID
        }
        return null; // Handle case where key is not found
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM ex08.chatrooms WHERE chatroomId = ?";
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
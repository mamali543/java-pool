package com.ader.sockets.repositories;

import com.ader.sockets.models.Chatroom;
import com.ader.sockets.models.User;
import com.ader.sockets.models.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatroomMapper implements RowMapper<Chatroom>{
    @Override
    public Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Message> messages = new ArrayList<>(); // Assuming Message is a class representing a message
        List<User> users = new ArrayList<>();
        Chatroom msg = new Chatroom(rs.getLong("chatroomId"), rs.getString("chatroomName"), rs.getLong("chatroomOwnerId"), messages, users );
        return msg;
    }
}


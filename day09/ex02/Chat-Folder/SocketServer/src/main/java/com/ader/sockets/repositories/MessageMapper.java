package com.ader.sockets.repositories;


import com.ader.sockets.models.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message msg = new Message(rs.getLong("messageId"), rs.getLong("senderId"), rs.getLong("roomId"), rs.getString("messageText"), rs.getTimestamp("datetime").toLocalDateTime());
        return msg;
    }
}
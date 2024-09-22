package com.ader.sockets.repositories;


import com.ader.sockets.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getLong("userId"), rs.getString("userName"), rs.getString("userPassword"));
        return user;
    }
}
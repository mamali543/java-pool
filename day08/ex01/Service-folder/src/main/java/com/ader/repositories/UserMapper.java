package main.java.com.ader.repositories;

import main.java.com.ader.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getLong("userId"), rs.getString("userEmail"));
        return user;
    }
}
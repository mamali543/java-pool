package main.java.com.ader.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import main.java.com.ader.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository{

    private DataSource dataSource;
    private final String tableName = "users";

    public UsersRepositoryJdbcImpl(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    @Override
    public Optional findById(Long id)
    {
        String sqlQuery = "SELECT FROM " + tableName + " WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            ps.setLong(1, id);
            try (ResultSet rs =  ps.executeQuery())
            {
                if (rs.next())
                {
                    User user = new User(rs.getLong("userId"),rs.getString("userName") , rs.getString("userEmail"));
                    return Optional.of(user);
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
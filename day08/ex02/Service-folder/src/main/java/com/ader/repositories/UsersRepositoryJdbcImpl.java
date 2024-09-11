package main.java.com.ader.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import main.java.com.ader.models.User;
import org.springframework.stereotype.Component;

@Component("UsersRepository")
public class UsersRepositoryJdbcImpl implements UsersRepository{

    private HickariDataSource dataSource;
    private final String tableName = "ex08.users";

    public UsersRepositoryJdbcImpl(HickariDataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    @Override
    public Optional findById(Long id)
    {
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE userId = ?";
        User user = null;
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            ps.setLong(1, id);
            try (ResultSet rs =  ps.executeQuery())
            {
                if (rs.next())
                {
                    // User user = new User(rs.getLong("userId"),rs.getString("userName") , rs.getString("userPassword"));
                    user = new User(rs.getLong("userId"), rs.getString("userPassword"));
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE userPassword = ?";
        User user =null;
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            ps.setString(1, email);
            try (ResultSet rs =  ps.executeQuery())
            {
                if (rs.next())
                {
                    // User user = new User(rs.getLong("userId"),rs.getString("userName") , rs.getString("userPassword"));
                    user = new User(rs.getLong("userId"), rs.getString("userPassword"));
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return Optional.of(user);
    }

    @Override
    public List findAll()
    {
        String sqlQuery = "SELECT * FROM " + tableName;
        List<User> usersList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            try (ResultSet rs =  ps.executeQuery())
            {
                while (rs.next())
                {
                    // User user = new User(rs.getLong("userId"),rs.getString("userName") , rs.getString("userPassword"));
                    User user = new User(rs.getLong("userId"), rs.getString("userPassword"));
                    usersList.add(user);
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return usersList;
    }

    @Override
    /**
     * Deletes a user from the database based on the provided user ID.
     *
     * @param id The ID of the user to be deleted.
     * @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public void delete(Long id)
    {
        String sqlQuery = "DELETE FROM " + tableName + " WHERE userId = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User entity){
        String sqlQString = "UPDATE ex08.users SET userPassword = ? WHERE userId = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stm = connection.prepareStatement(
                    sqlQString);
            stm.setString(1, entity.getuserPassword());
            stm.setLong(2, entity.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void save(User entity)
    {
        String sqlQuery = "INSERT INTO "+tableName+"(userPassword) VALUES (?)";
        try(Connection connection = this.dataSource.getConnection())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getuserPassword());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            entity.setUserId(rs.getLong("userId"));
            entity.setUserPassword(rs.getString("userPassword"));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }


}
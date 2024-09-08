package main.java.com.ader.repositories;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import main.java.com.ader.models.User;
import main.java.com.ader.repositories.UserMapper;
import main.java.com.ader.repositories.UsersRepository;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository{

    private final JdbcTemplate jdbcTemplate;
    
    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional findById(Long id)
    {
        String sqlQuery = "SELECT FROM ex08.users WHERE userId = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, new UserMapper()));
        }
        catch(EmptyResultDataAccessException e)
        {
            return Optional.empty();
        }
    }


    @Override
    public List findAll()
    {
        String sqlQuery = "SELECT * FROM ex08.users";
        return jdbcTemplate.query(sqlQuery,  new UserMapper());
    }

    @Override
    public void save(User entity)
    {
        String sqlQuery = "INSERT INTO ex08.users(userEmail) VALUES (?)";
        jdbcTemplate.update(sqlQuery, entity.getUserEmail());
    }

    @Override
    public void update(User entity)
    {
        String sqlQuery = "UPDATE ex08.users SET userEmail = ? WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, entity.getUserEmail(), entity.getUserId());
    }

    @Override
    public void delete(Long id)
    {
        String sqlQuery = "DELETE FROM ex08.users WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        String sqlQuery = "SELECT FROM ex08.users WHERE userEmail = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery,new Object[]{email}, new UserMapper()));
        }
        catch(EmptyResultDataAccessException e)
        {
            return Optional.empty();
        }
    }

}
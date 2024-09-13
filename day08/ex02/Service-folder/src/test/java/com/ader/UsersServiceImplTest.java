package com.ader;

import com.ader.services.UsersServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestApplicationConfig.class)
@Transactional
public class UsersServiceImplTest {

    @Autowired
    private UsersServicesImpl usersService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        // Create table for users in H2
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS ex08");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS ex08.users (userId SERIAL PRIMARY KEY, userEmail VARCHAR(255), userPassword VARCHAR(255))");
    }

    @Test
    public void testSignUp() {
        String email = "test@example.com";
        String password = usersService.signUp(email);

        assertNotNull(password);
        assertTrue(password.length() > 0);

        // Verify the user is saved in the database
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ex08.users WHERE userEmail = ?",
                new Object[] { email }, Integer.class);
        assertEquals(1, count);
    }
}

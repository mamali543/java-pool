package mr.school1337.chat.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import java.util.ArrayList;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.time.LocalDateTime;

import mr.school1337.chat.exceptions.NotSavedSubEntityException;
import java.lang.*;

import mr.school1337.chat.models.Chatroom;
import mr.school1337.chat.models.Message;
import mr.school1337.chat.models.User;
import mr.school1337.chat.repositories.MessagesRepositoryJdbcImpl;

public class Program{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String url = "jdbc:postgresql://localhost:5432/chat"; //standard java api that enables java programs to interact with databases, JDBC URL
    public static final String db_user = "macbookpro";    // \du

    public static void main(String[] args){
        DataSource dataSource = createDataSourceConnection();
        if (dataSource != null) {
            MessagesRepositoryJdbcImpl messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
            Optional<Message> message = messagesRepositoryJdbc.findById(5L);
            if (message.isPresent())
            {
                Message msg = message.get();
                msg.setMessage("BYE");
                msg.setLocalDateTime(null);
                messagesRepositoryJdbc.update(msg);
            }
            ((HikariDataSource) dataSource).close();
        }
    }

    private static DataSource createDataSourceConnection() {
        try {
            //set an object conf for a hickari datasource
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(url); // URL to your database
            config.setUsername(db_user); // Database username

            //construct the hickari dataSource with the conf object
            HikariDataSource hikariDataSource = new HikariDataSource(config);

            System.out.println("Database succefully connected!");
            executeSqlScript(hikariDataSource, "/schema.sql");
            executeSqlScript(hikariDataSource, "/data.sql");
            return hikariDataSource;
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void executeSqlScript(DataSource dataSource, String resourcePath) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             InputStream is = Program.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Statement statement = conn.createStatement()) {

            String line;
            StringBuilder sqlQuery = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("--")) { // Ignore comments and empty lines
                    continue;
                }
                sqlQuery.append(line);
                // Check for end of statement
                if (line.endsWith(";")) {
                    boolean a = statement.execute(sqlQuery.toString());
                    sqlQuery.setLength(0); // Clear the SQL statement buffer
                }
            }
        } catch (Exception e) {
            throw new SQLException("Failed to execute SQL script: " + resourcePath, e);
        }
    }
}
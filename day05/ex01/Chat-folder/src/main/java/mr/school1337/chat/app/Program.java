package mr.school1337.chat.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Statement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mr.school1337.chat.models.Message;
import mr.school1337.chat.repositories.MessagesRepositoryJdbcImpl;

public class Program{
    public static final String url = "jdbc:postgresql://localhost:5432/chat";
    public static final String db_user = "macbookpro";
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Message Id: ");
        if (!scanner.hasNextLong())
        {
            System.err.println("Invalid Message Id!");
            System.exit(-1);
        }
        Long id = scanner.nextLong();
        Connection connection = createDataSourceConnection();
    }

    private static Connection createDataSourceConnection() {
        try {
            //set an object conf for a hickari datasource
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(url); // URL to your database
            config.setUsername(db_user); // Database username

            //construct the hickari dataSource with the conf object
            HikariDataSource hikariDataSource = new HikariDataSource(config);


            if (hikariDataSource.getConnection() == null) {
                throw new SQLException("Database connection failed");
            }
            System.out.println("Database succefully connected!");
            Connection hickariConnection =  hikariDataSource.getConnection();
            executeSqlScript(hickariConnection, "/schema.sql");
            executeSqlScript(hickariConnection, "/data.sql");
            return hickariConnection;
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void executeSqlScript(Connection conn, String resourcePath) throws SQLException {
        try (InputStream is = Program.class.getResourceAsStream(resourcePath);
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
                    System.out.println("SqlQuery: "+sqlQuery);
                    boolean a = statement.execute(sqlQuery.toString());
                    sqlQuery.setLength(0); // Clear the SQL statement buffer
                }
            }
        } catch (Exception e) {
            throw new SQLException("Failed to execute SQL script: " + resourcePath, e);
        }
    }
}
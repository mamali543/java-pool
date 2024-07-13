package mr.school1337.chat.repositories;

import mr.school1337.chat.repositories.MessagesRepository;
import mr.school1337.chat.models.Message;
import mr.school1337.chat.models.Chatroom;
import mr.school1337.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mr.school1337.chat.exceptions.NotSavedSubEntityException;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    DataSource dataSource ;

    public MessagesRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    private boolean isIdExist(Long id, String tableName){
        String query = "SELECT EXISTS (SELECT 1 FROM chat." + tableName + " WHERE id = ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking ID existence: " + e.getMessage());
        }
        return false;
    }

    public void save(Message msg){
        if (!isIdExist(msg.getUser().getId(), "users") || !isIdExist(msg.getRoom().getId(), "chatrooms"))
            throw new NotSavedSubEntityException();

        String sqlQuery = "INSERT INTO chat.message (author_id, room_id, content, datetime) VALUES (?, ?, ?, ?) RETURNING id;";

        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlQuery)){
            ps.setLong(1, msg.getUser().getId());
            ps.setLong(2, msg.getRoom().getId());
            ps.setString(3, msg.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(msg.getDateTime()));
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Long message_id = rs.getLong(1);
                    msg.setId(message_id);
                    System.out.println(ANSI_BLUE+"Inserted message ID: " + message_id+ANSI_RESET);
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public Optional<Message> findById(Long id)
    {
        String sql = "SELECT m.id, m.author_id, m.room_id, u.id AS user_id, u.login, u.password, c.id AS chatroom_id, c.name, c.owner_id " +
                "FROM chat.message m " +
                "JOIN chat.users u ON m.author_id = u.id " +
                "JOIN chat.chatrooms c ON m.room_id = c.id " +
                "WHERE m.id = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    User user = new User(rs.getLong("user_id"), rs.getString("login"), rs.getString("password"), null, null);
                    Chatroom chatroom = new Chatroom(rs.getLong("chatroom_id"), rs.getString("name"), user, null);
                    Message message = new Message(rs.getLong("id"), user, chatroom, null, null);
                    return Optional.of(message);
                }
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }
}
package mr.school1337.chat.repositories;

import mr.school1337.chat.repositories.MessagesRepository;
import mr.school1337.chat.models.Message;
import mr.school1337.chat.models.Chatroom;
import mr.school1337.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
public class MessagesRepositoryJdbcImpl implements MessagesRepository{
    DataSource dataSource ;

    public MessagesRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
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
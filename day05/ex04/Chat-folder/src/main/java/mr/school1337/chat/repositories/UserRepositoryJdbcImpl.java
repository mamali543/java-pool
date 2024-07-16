package mr.school1337.chat.repositories;

import mr.school1337.chat.models.Chatroom;
import mr.school1337.chat.models.User;
import mr.school1337.chat.repositories.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class UserRepositoryJdbcImpl implements UserRepository{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    DataSource dataSource ;

    public UserRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<User> findAll(int page, int size)
    {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.login, u.password, " +
            "COALESCE(json_agg(json_build_object('id', uoc.id, 'name', uoc.name, 'owner_id', uoc.owner_id)) FILTER (WHERE uoc.owner_id = u.id), '[]') AS owned_chatrooms, " +
            "COALESCE(json_agg(json_build_object('id', uc.chatroom_id, 'name', c.name, 'owner_id', c.owner_id)) FILTER (WHERE uc.user_id = u.id), '[]') AS member_chatrooms " +
            "FROM chat.users u " +
            "LEFT JOIN chat.chatrooms uoc ON uoc.owner_id = u.id " +
            "LEFT JOIN chat.user_chatrooms uc ON uc.user_id = u.id " +
            "LEFT JOIN chat.chatrooms c ON c.id = uc.chatroom_id " + // Assuming 'chatrooms' is the correct table name
            "GROUP BY u.id " +
            "ORDER BY u.id " +
            "LIMIT ? OFFSET ?";
    


        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, size);
            ps.setLong(2, (page-1)*size);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setLogin(rs.getString("login"));
                    user.setPassword(rs.getString("password"));
                    user.setUserChatrooms(parseChatrooms(rs.getString("owned_chatrooms")));
                    user.setUserSocializedChatrooms(parseChatrooms(rs.getString("member_chatrooms")));
                    users.add(user);
                }
            }
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return users;
    }

    private List<Chatroom> parseChatrooms(String jsonChatrooms) throws SQLException {
        List<Chatroom> chatrooms = new ArrayList<>();
        JSONArray array = new JSONArray(jsonChatrooms);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Chatroom chatroom = new Chatroom(
                obj.getLong("id"),
                obj.getString("name"),
                new User(obj.getLong("owner_id"), null, null, null, null),
                new ArrayList<>()  // Empty list of messages
            );
            chatrooms.add(chatroom);
        }
        return chatrooms;
    }
}
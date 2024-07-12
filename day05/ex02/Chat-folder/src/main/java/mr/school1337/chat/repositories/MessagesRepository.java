package mr.school1337.chat.repositories;

import mr.school1337.chat.models.Message;

import java.sql.SQLException;
import java.util.Optional;
public interface MessagesRepository {
    Optional<Message> findById(Long id);
    void save(Message msg);

}
package mr.school1337.chat.repositories;

import mr.school1337.chat.models.User;

import java.util.List;
public interface UserRepository {
    List<User> findAll(int page, int size);
}
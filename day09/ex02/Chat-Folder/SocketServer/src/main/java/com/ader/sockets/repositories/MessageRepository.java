package com.ader.sockets.repositories;

import com.ader.sockets.models.Message;

import java.sql.SQLException;
import java.util.Optional;
import com.ader.sockets.repositories.CrudRepositorie;
import java.util.List;

public interface MessageRepository extends CrudRepositorie<Message> {
    void saveLong(Message message);
    List<Message> getLast30Messages(Long roomId);
}

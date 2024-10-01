package com.ader.sockets.service;

import com.ader.sockets.models.Message;
import java.util.List;
public interface MessageService {
    void save(Message msg);
    List<Message> getLast30Messages(Long roomId);
}
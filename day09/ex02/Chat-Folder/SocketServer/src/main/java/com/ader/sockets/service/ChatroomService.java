package com.ader.sockets.service;

import com.ader.sockets.models.Chatroom;
import java.util.List;
public interface ChatroomService {
    void createChatroom(Chatroom chatroom);
    List<Chatroom> getChatrooms();
}

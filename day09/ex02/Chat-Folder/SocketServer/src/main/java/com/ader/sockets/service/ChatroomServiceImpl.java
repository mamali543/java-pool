package com.ader.sockets.service;

import com.ader.sockets.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.ader.sockets.models.Chatroom;
import java.util.List;


@Component("ChatroomService")
public class ChatroomServiceImpl implements ChatroomService {
    private ChatroomRepository chatroomRepository;

    ChatroomServiceImpl(@Qualifier("chatroomRepository") ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    @Override
    public Long createChatroom(Chatroom chatroom) {
        System.out.println("Creating chatroom ...");
        return chatroomRepository.saveToGetId(chatroom);
    }
    
    @Override
    public List<Chatroom> getChatrooms() {
        return chatroomRepository.findAll();
    }
}

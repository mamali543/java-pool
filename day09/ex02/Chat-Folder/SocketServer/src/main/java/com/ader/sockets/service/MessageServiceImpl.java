package com.ader.sockets.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.Message;
import com.ader.sockets.repositories.MessageRepository;
import java.util.List;

@Component("messageService")
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    public MessageServiceImpl(@Qualifier("messagesRepository") MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void save(Message msg) {
        messageRepository.save(msg);
    }

    @Override
    public List<Message> getLast30Messages(Long roomId) {
        System.out.println("roomId to get last 30 messages: "+roomId);
        return messageRepository.getLast30Messages(roomId);
    }
}

package com.ader.sockets.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.Message;
import com.ader.sockets.repositories.MessageRepository;

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
}
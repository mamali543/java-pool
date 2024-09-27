package com.ader.sockets.repositories;

import com.ader.sockets.models.Chatroom;
import com.ader.sockets.repositories.CrudRepositorie;

public interface ChatroomRepository extends CrudRepositorie<Chatroom> {
    Long saveToGetId(Chatroom chatroom);
}

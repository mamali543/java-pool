package com.ader.sockets.server;


import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.User;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;
import com.ader.sockets.models.UserHandler;

import java.io.*;

/**
 * Server
 */
@Component
public class Server {
    private ServerSocket serverSocket;
    private int port;
    private boolean running;

    public Server() {
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(this.port);
            running = true;

        } catch (Exception e) {
            e.printStackTrace();
            closeServerSocket();
        }
    }
    
    public void start() {
        try {
            while (!serverSocket.isClosed())
            {
                System.out.println("Waiting for client connection...");
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected");
                
                UserHandler userHandler = new UserHandler(socket);
                new Thread(userHandler).start();
            }
        } catch (Exception e) {
            System.err.println("Error in server: " + e.getMessage());
        } finally {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
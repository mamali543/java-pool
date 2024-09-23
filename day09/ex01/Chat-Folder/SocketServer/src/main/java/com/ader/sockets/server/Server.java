package com.ader.sockets.server;


import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ader.sockets.models.User;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;
import com.ader.sockets.models.UserHandler;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

/**
 * Server
 */
@Component
public class Server {
    private ServerSocket serverSocket;
    private int port;
    private ExecutorService threadPool;
    // public ArrayList<UserHandler> userHandlers = new ArrayList<>();

    public Server() {
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(this.port);
            threadPool = Executors.newFixedThreadPool(10);

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
                //when we accet a connection we get a client socket
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected");
                UserHandler userHandler = new UserHandler(socket);
                threadPool.submit(userHandler);
                // userHandlers.add(userHandler);
            }
        } catch (Exception e) {
            System.err.println("Error in server: " + e.getMessage());
        } finally {
            closeServerSocket();
            shutdownThreadPool();
        }
    }
    private void shutdownThreadPool() {
        if (threadPool != null && !threadPool.isShutdown())
            threadPool.shutdown();
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
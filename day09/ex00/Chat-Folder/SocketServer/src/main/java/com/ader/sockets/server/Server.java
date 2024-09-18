package com.ader.sockets.server;


import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.ader.sockets.config.ApplicationConfig;
import com.ader.sockets.models.User;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;

import java.io.*;

/**
 * Server
 */
@Component
public class Server {
    private PrintWriter clientWriter;
    private ServerSocket serverSocket;
    private BufferedReader clientReader;
    private int port;
    private AnnotationConfigApplicationContext ctx;
    private UserService userService;

    public Server() {
    }

    public void setPort(int port) {
        this.port = port;
        ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userService = ctx.getBean(UserServiceImpl.class);
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(this.port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        try {
            System.out.println("Waiting for client connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            
            clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientWriter = new PrintWriter(socket.getOutputStream(), true);
            
            clientWriter.println("Hello from server!");
            // System.out.println("Sent greeting to client");
    
            String s;
            while ((s = clientReader.readLine()) != null) {
                // System.out.println("Received operation from client: " + s);
                
                if (s.equalsIgnoreCase("SignUp")) {
                    clientWriter.println("enter  username:");
                    
                    String userName = clientReader.readLine();
                    System.out.println("From Client: Received username: " + userName);
                    
                    clientWriter.println("Enter Password: ");

                    String userPassword = clientReader.readLine();
                    System.out.println("From Client: Received userPassword: " + userPassword);

                    User u = new User(userName, userPassword);
                    // clientWriter.println("Successful!");
                    if (userService.SignUp(u)){
                        clientWriter.println("successfully!");
                    } else {
                        clientWriter.println("User already exists!");
                    }
                    break;
                } else {
                    clientWriter.println("Enter what you want to do!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
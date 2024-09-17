package com.ader.sockets.server;


import java.net.ServerSocket;
import java.net.Socket;

import com.ader.sockets.models.User;

import java.io.*;

/**
 * Server
 */
public class Server {
    PrintWriter clientWriter;
    ServerSocket serverSocket;
    BufferedReader clientReader;
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);

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
                    clientWriter.println("Successful!");

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
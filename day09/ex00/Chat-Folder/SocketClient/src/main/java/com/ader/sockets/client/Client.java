package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    private int port;
    
    public Client()
    {

    }

    public void setPort(int port) {
        this.port = port;
    }

    public void init() {
        try {
            socket = new Socket("localhost", port);
            reader = new BufferedReader(new InputStreamReader(System.in));
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void start() {
        try {
            String serverMessage = serverReader.readLine();
            System.out.println("From Server: " + serverMessage);
    
            String userInput;
            while (true) {
                System.out.print("> ");
                userInput = reader.readLine();
                serverWriter.println(userInput);
                // System.out.println("Client: Send to server, " + userInput);
    
                // System.out.println("From Server: " + serverMessage);
    
                serverMessage = serverReader.readLine();
                if (!(serverMessage.equals("Enter what you want to do!")) && userInput.equalsIgnoreCase("SignUp") ) {
                    // System.out.print("Enter username: ");
                    System.out.print(serverMessage + "\n>");
                    String username = reader.readLine();
                    serverWriter.println(username);
                    // System.out.println("Client: Sent username, " + username);
    
                    serverMessage = serverReader.readLine();
                    System.out.print(serverMessage + "\n>");
                    String password = reader.readLine();
                    serverWriter.println(password);
                    serverMessage = serverReader.readLine();
                    System.out.print(serverMessage);
                    break;
                }
                else
                    System.out.println(serverMessage);

            }
        } catch (Exception e) {
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
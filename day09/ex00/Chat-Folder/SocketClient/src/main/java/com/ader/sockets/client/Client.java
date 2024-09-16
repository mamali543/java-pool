package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;


public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    public Client(int port) {
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
                System.out.println("Client: Send to server, " + userInput);
    
                // System.out.println("From Server: " + serverMessage);
    
                if (userInput.equalsIgnoreCase("SignUp")) {
                    // serverMessage = serverReader.readLine();
                    System.out.print("Enter username: ");
                    String username = reader.readLine();
                    serverWriter.println(username);
                    System.out.println("Client: Sent username, " + username);
    
                    serverMessage = serverReader.readLine();
                    System.out.println("From Server: " + serverMessage);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
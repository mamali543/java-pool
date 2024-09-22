package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.springframework.stereotype.Component;

import java.io.IOException; // Add this import

import java.io.PrintWriter;

@Component
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    private int port;
    private String userName;
    
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
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void start() {
        try {
            String serverMessage = serverReader.readLine();
            System.out.println("From Server: " + serverMessage);
            signUpOrSignIn(serverMessage);
            serverWriter.println(userName);
            // System.out.print(">");
            listener();
            while (true) {
                String userMessage = reader.readLine();
                if (userMessage.equals("exit"))
                    System.exit(0);
                // System.out.println(userName + ": " + userMessage);
                serverWriter.println(userName + ": " + userMessage);
                System.out.print(">");
            }

        } catch (Exception e) {
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }


    public void signUpOrSignIn(String serverMessage){
        try{
            System.out.print("> ");
            String userInput = reader.readLine();
            while(!(userInput.equalsIgnoreCase("SignUp")) && !(userInput.equalsIgnoreCase("SignIn"))) {
                System.out.println("Invalid input. Please enter either 'SignUp' or 'SignIn'");
                System.out.print("> ");
                userInput = reader.readLine();
            }
            serverWriter.println(userInput);
            serverMessage = serverReader.readLine();
            System.out.print(serverMessage + "\n>");
            userName = reader.readLine();
            serverWriter.println(userName);
    
            serverMessage = serverReader.readLine();
            System.out.print(serverMessage + "\n>");
            String password = reader.readLine();
            serverWriter.println(password);
            serverMessage = serverReader.readLine();
            System.out.println(serverMessage);
            if (userInput.equalsIgnoreCase("SignUp")) {
                signUpOrSignIn(serverMessage);
            }
            if (userInput.equalsIgnoreCase("SignIn")) {
                if (!(serverMessage.equalsIgnoreCase("start messaging")))
                    signUpOrSignIn(serverMessage);
            }
        }
        catch(Exception e){
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void listener(){
        // Read messages from server in a separate thread
        new Thread(() -> {
            String serverMessage;
            try {
                while ((serverMessage = serverReader.readLine()) != null) {
                    System.out.println(serverMessage);
                    System.out.print(">");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader serverReader, PrintWriter serverWriter) {
        try {
            if (serverReader != null) {
                serverReader.close();
            }
            if (serverWriter != null) {
                serverWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
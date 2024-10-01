package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.springframework.stereotype.Component; // Add this import

@Component
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    private int port;
    private String userName;
    boolean running;
    
    public Client()
    {

    }

    public void setPort(int port) {
        this.port = port;
        running = true;
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
            // System.out.println("Choose an option: ");
            signUpOrSignIn(serverMessage);
            listener();
            while (running) {
                String userMessage = reader.readLine();
                if (userMessage.equalsIgnoreCase("exit"))
                {
                    System.out.print("you have left the chat!");
                    serverWriter.println("exit");

                    closeEverything(socket, serverReader, serverWriter);
                    running = false;
                }
                else if (userMessage.equalsIgnoreCase("leave"))
                {
                    serverWriter.println("leave");
                    serverWriter.flush(); // Ensure the message is sent
                    chooseOrCreateRoom(serverMessage);
                }
                else
                    serverWriter.println(userName + ": " + userMessage);
            }

        } catch (Exception e) {
            System.err.println("Unexpected error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void chooseOrCreateRoom(String serverMessage)
    {
        // serverWriter.println("room options");
        // serverWriter.println("room options");
        String line;
        try{
            while (!(line = serverReader.readLine()).equals("END_OF_OPTIONS")) {
                System.out.println(line);
            }
            System.out.print(">>> ");
            String option = reader.readLine();
            while (!(option.equals("1") || option.equals("2") || option.equals("3"))) {
                System.out.println("Invalid option, please choose 1, 2 or 3");
                System.out.print("> ");
                option = reader.readLine();
            }
            serverWriter.println(option);
            if (option.equals("1"))
            {
                serverMessage = serverReader.readLine();
                System.out.println(serverMessage);
                line = reader.readLine();
                if (line.length() > 0) {
                    serverWriter.println(line);
                    serverMessage = serverReader.readLine();
                    System.out.println(serverMessage);
                    chooseOrCreateRoom(serverMessage);
                }
            }
            else if (option.equals("2"))
            {
                line = serverReader.readLine();
                if (line.equals("No chatrooms found, create one!"))
                {
                    System.out.println(line);
                    chooseOrCreateRoom(serverMessage);
                }
                else
                {          
                    //get chatrooms          
                    while (!(line .equals("END_OF_OPTIONS"))) {
                        System.out.println(line);
                        line = serverReader.readLine();
                    }
                    System.out.print(">>> ");
                    // choose chatroom
                    line = reader.readLine();
                    serverWriter.println(line);
                    serverMessage = serverReader.readLine();
                    System.out.println(serverMessage);
                }
            }
            else
            {
                System.out.println("You have left the chat app!");
                running = false;
                closeEverything(socket, serverReader, serverWriter);
                System.exit(0);
            }
        }
        catch(IOException e)
        {
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void signUpOrSignIn(String serverMessage){

        try{
            String line;
            while (!(line = serverReader.readLine()).equals("END_OF_OPTIONS")) {
                System.out.println(line);
            }
            System.out.print("> ");
            String option = reader.readLine();
            while (!(option.equals("1") || option.equals("2") || option.equals("3"))) {
                System.out.println("Invalid option, please choose 1, 2 or 3");
                System.out.print("> ");
                option = reader.readLine();
            }
            serverWriter.println(option);
            if (option.equals("3"))
            {
                closeEverything(socket, serverReader, serverWriter);
                running = false;
                System.out.println("You have left the chat app!");
                System.exit(0);
            }
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
            if (option.equalsIgnoreCase("2")) {
                signUpOrSignIn(serverMessage);
            }
            if (option.equalsIgnoreCase("1")) {
                chooseOrCreateRoom(serverMessage);
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
        //the lambda expression in the listener() method is providing an implementation of Runnable on the fly.
        new Thread(() -> {
            String serverMessage;
            try {
                while (running && (serverMessage = serverReader.readLine()) != null) {
                    System.out.println(serverMessage);
                    // System.out.print(">");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader serverReader, PrintWriter serverWriter) {
        try {
            running = false;
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
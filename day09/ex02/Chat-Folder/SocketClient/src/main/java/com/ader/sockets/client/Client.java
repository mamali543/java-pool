package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.ader.sockets.models.ChatMessage;
import com.ader.sockets.utils.JsonUtil;

@Component
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedReader serverReader;
    private PrintWriter serverWriter;
    private int port;
    private String userName;
    boolean running;
    //The volatile keyword in Java is used to indicate that a variable's value may be modified by different threads.
    //It ensures that any read of the variable will always return the most recently written value.
    private volatile boolean listening = true;

    
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
            ChatMessage serverMessage = receiveJsonMessage();
            System.out.println("From Server: " + serverMessage.getMessage());
            // System.out.println("Choose an option: ");
            signUpOrSignIn(serverMessage.getMessage());
            listener();
            while (running) {
                String userMessage = reader.readLine();
                if (userMessage.equalsIgnoreCase("exit") || userMessage.equalsIgnoreCase("leave"))
                {
                    System.out.print("you have left the chat!");
                    sendJsonMessage("exit", null, null);
                    stopListening();
                    closeEverything(socket, serverReader, serverWriter);
                    running = false;
                }
                else
                    sendJsonMessage(userName + ": " + userMessage, null , null);
            }

        } catch (Exception e) {
            System.err.println("Unexpected error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    private void stopListening() {
        listening = false;
    }

    public void chooseOrCreateRoom(String serverMessage)
    {
        try{
            String line;
            while (!((line = receiveJsonMessage().getMessage()).equals("END_OF_OPTIONS"))) {
                System.out.println(line);
            }
            System.out.print("> ");
            String option = reader.readLine();
            while (!(option.equals("1") || option.equals("2") || option.equals("3"))) {
                System.out.println("Invalid option, please choose 1, 2 or 3");
                System.out.print("> ");
                option = reader.readLine();
            }
            sendJsonMessage(option, null, null);
            if (option.equals("1"))
            {
                serverMessage = receiveJsonMessage().getMessage();
                System.out.println(serverMessage);
                line = reader.readLine();
                if (line.length() > 0) {
                    sendJsonMessage(line, null, null);
                    serverMessage = receiveJsonMessage().getMessage();
                    System.out.println(serverMessage);
                    chooseOrCreateRoom(serverMessage);
                }
            }
            else if (option.equals("2"))
            {
                ChatMessage chatMessage = receiveJsonMessage();
                line = chatMessage.getMessage();
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
                        chatMessage = receiveJsonMessage();
                        line = chatMessage.getMessage();
                    }
                    String i = receiveJsonMessage().getMessage();
                    System.out.print(">>> ");
                    // choose chatroom
                    line = reader.readLine();
                    if (line.equals(i))
                    {
                        System.out.print("you have left the chat!");
                        sendJsonMessage("exit", null, null);
                        stopListening();
                        closeEverything(socket, serverReader, serverWriter);
                        running = false;
                        return ;
                    }
                    sendJsonMessage(line, null, null);
                    serverMessage = receiveJsonMessage().getMessage();
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
        catch(Exception e)
        {
            System.err.println("Error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void signUpOrSignIn(String serverMessage){

        try{
            String line;
            while (!(line = receiveJsonMessage().getMessage()).equals("END_OF_OPTIONS")) {
                System.out.println(line);
            }
            System.out.print("> ");
            String option = reader.readLine();
            while (!(option.equals("1") || option.equals("2") || option.equals("3"))) {
                System.out.println("Invalid option, please choose 1, 2 or 3");
                System.out.print("> ");
                option = reader.readLine();
            }
            sendJsonMessage(option, null, null);
            if (option.equals("3"))
            {
                running = false;
                closeEverything(socket, serverReader, serverWriter);
                System.out.println("You have left the chat app!");
                System.exit(0);
            }
            serverMessage = receiveJsonMessage().getMessage();
            System.out.print(serverMessage + "\n>");
            userName = reader.readLine();
            sendJsonMessage(userName, null, null);
    
            serverMessage = receiveJsonMessage().getMessage();
            System.out.print(serverMessage + "\n>");
            String password = reader.readLine();
            sendJsonMessage(password, null, null);
            serverMessage = receiveJsonMessage().getMessage();
            System.out.println(serverMessage);
            if (option.equalsIgnoreCase("1") && !serverMessage.equals("User doesn't exist!")) 
                chooseOrCreateRoom(serverMessage);
            else
                signUpOrSignIn(serverMessage);
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
            try {
                while (listening) {
                    ChatMessage chatMessage = receiveJsonMessage();
                    // System.out.println(chatMessage.getMessage());
                    if (chatMessage != null) {
                        System.out.println(chatMessage.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                if (listening) {
                    System.err.println("Error in listener thread: " + e.getMessage());
                    e.printStackTrace();
                }
        }
        }).start();
    }

    public void sendJsonMessage(String message, Long fromId, Long roomId) throws Exception {
        ChatMessage chatMessage = new ChatMessage(message, fromId, roomId);
        String jsonMessage = JsonUtil.toJson(chatMessage);
        serverWriter.println(jsonMessage);
    }

    public ChatMessage receiveJsonMessage() throws Exception {
        String jsonMessage = serverReader.readLine();
        return JsonUtil.fromJson(jsonMessage, ChatMessage.class);
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
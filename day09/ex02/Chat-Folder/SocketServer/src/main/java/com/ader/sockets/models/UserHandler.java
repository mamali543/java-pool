package com.ader.sockets.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ader.sockets.config.ApplicationConfig;
import com.ader.sockets.service.ChatroomService;
import com.ader.sockets.service.ChatroomServiceImpl;
import com.ader.sockets.service.MessageService;
import com.ader.sockets.service.MessageServiceImpl;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;

public class UserHandler implements Runnable {

    private static ArrayList<UserHandler> userHandlers = new ArrayList<>();
    private static ArrayList<Chatroom> chatrooms = new ArrayList<>();
    private Chatroom currentChatroom; // Add this field to track the current chatroom

    private Socket socket;
    private BufferedReader clientReader;
    private PrintWriter clientWriter;
    private UserService userService;
    private MessageService messageService;
    private ChatroomService chatroomService;
    private User user;
    private List<String> options;
    String username;

    public UserHandler(Socket socket) {
        try{
            this.socket = socket;
            this.options = new ArrayList<>();
            clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientWriter = new PrintWriter(socket.getOutputStream(), true);
            
            clientWriter.println("Hello from server!");

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            userService = context.getBean(UserServiceImpl.class);
            messageService = context.getBean(MessageServiceImpl.class);
            chatroomService = context.getBean(ChatroomServiceImpl.class);
        }
        catch(Exception e){
            System.err.println("Error in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
    }

    public void createOrChooseRoom() {
        try{
            options.clear();

            options.add("1. Create room");
            options.add("2. Choose room");
            options.add("3. Exit");
            for (String option : options) {
                clientWriter.println(option);
            }
            clientWriter.println("END_OF_OPTIONS");
            clientWriter.flush(); // Ensure all data is sent
            String optionChoosed = clientReader.readLine();
            System.out.println("option choosed: "+optionChoosed);
            if (optionChoosed.equals("1"))
            {
                clientWriter.println("Enter room name: ");

                String roomName = clientReader.readLine();
                User u = userService.getUser(username);
                Chatroom chatroom = new Chatroom(null, roomName, u.getUserId(), null, null);
                
                Long rommId = chatroomService.createChatroom(chatroom);
                chatroom.setId(rommId);
                clientWriter.println("Room: "+chatroom.getName()+" created successfully!");
                chatrooms.add(chatroom);
                createOrChooseRoom();
            }
            else if (optionChoosed.equals("2"))
            {          
                System.out.println("option choosed: "+optionChoosed);
                int i =0;
                options.clear();
                
                if (chatrooms.isEmpty())
                {
                    clientWriter.println("No chatrooms found, create one!");
                    createOrChooseRoom();
                }
                else
                {
                    for (Chatroom chatroom : chatrooms) {
                        options.add(++i + ". " + chatroom.getName());
                    }
                    options.add(++i +". "+"exit");
                    options.add("END_OF_OPTIONS");
                    for (String option : options) {
                        clientWriter.println(option);
                    }
                }
                
                //get the room choosed
                String roomChoosed = clientReader.readLine();
                currentChatroom = chatrooms.get(Integer.parseInt(roomChoosed)-1);
                joinChatroom(currentChatroom);
            }
            else
                closeEverything(socket, clientReader, clientWriter);
        }
        catch(Exception e){
            System.err.println("Error in UserHandler chooseOrCreate room: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
    }

    public void joinChatroom(Chatroom chatroom) {
        this.currentChatroom = chatroom; // Set the current chatroom
        userHandlers.add(this);
        // Query the database for the last 30 messages
        clientWriter.println("Welcome to " + chatroom.getName() + " chatroom!");
        List<Message> lastMessages = messageService.getLast30Messages(chatroom.getId());
        for (Message message : lastMessages) {
            System.out.println("message: "+message.getMessage());
            getLastMessages(message.getMessage()); // Send each message to the user
        }
        broadcastMessage("SERVER: " + username + " has joined the chatroom " + chatroom.getName());
    }

    public void broadcastMessage(String messageToSend) {
        for (UserHandler userHandler : userHandlers) {
            // Check if the user is in the same chatroom
            System.out.println("userHandler.lentgh: " + userHandlers.size());
            if (userHandler.currentChatroom != null && userHandler.currentChatroom.equals(this.currentChatroom) && !userHandler.username.equals(username) ) {
                try {
                    System.out.println("client to recieve message " + userHandler.username);
                    userHandler.clientWriter.println(messageToSend);
                    userHandler.clientWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    closeEverything(socket, clientReader, clientWriter);
                }
            }
        }
    }
    public void getLastMessages(String messageToSend) {
        for (UserHandler userHandler : userHandlers) {
            // Check if the user is in the same chatroom
            if (userHandler.currentChatroom != null && userHandler.currentChatroom.equals(this.currentChatroom) && userHandler.username.equals(username) ) {
                try {
                    userHandler.clientWriter.println(messageToSend);
                    userHandler.clientWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    closeEverything(socket, clientReader, clientWriter);
                }
            }
        }
    }

    @Override
    public void run() {
            try {
                startSignUpOrSignIn();
                String messageFromClient;
                while (socket.isConnected()) {
                    messageFromClient = clientReader.readLine();
                    System.out.println("messageFromClient: "+messageFromClient);
                    if (messageFromClient == null || messageFromClient.equalsIgnoreCase("exit"))
                        break;  // Client has disconnected
                    else if (messageFromClient.equalsIgnoreCase("leave")) {
                        // this.currentChatroom = null;
                        createOrChooseRoom();
                        // continue;
                    }
                    else 
                    {
                        if ((this.user = userService.getUser(username)) != null)
                        {

                            System.out.println("user: "+this.user.getUserId());
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            Message msg = new Message(null, this.user.getUserId(), this.currentChatroom.getId(), messageFromClient.split(":")[1].trim(), currentDateTime);
                            System.out.println("message: "+msg.getMessage());
                            broadcastMessage(messageFromClient);
                            System.out.println("message sent");
                            messageService.save(msg);
                            System.out.println("message saved: ");
                        }
                    }
                }   
            }
            catch (IOException e) {
                System.err.println("Error in UserHandler run methode: " + e.getMessage());
            }
            catch(Exception e){
                System.err.println("Error in UserHandler run methode: " + e.getMessage());
            } finally {
                closeEverything(socket, clientReader, clientWriter);
            }
    } 

    public void sendOptions(){
        options.add("1. SignIn");
        options.add("2. SignUp");
        options.add("3. exit");
    }

    public void startSignUpOrSignIn() {
        sendOptions();
        try{
            for (String option : options) {
                clientWriter.println(option);
            }
            clientWriter.println("END_OF_OPTIONS");
            String optionChoosed = clientReader.readLine();

            if (optionChoosed.equals("3"))
            {
                closeEverything(socket, clientReader, clientWriter);
                return ;
            }
            clientWriter.println("enter  username:");
            
            username = clientReader.readLine();
            System.out.println("From Client: Received username: " + username);
            
            clientWriter.println("Enter Password: ");

            String userPassword = clientReader.readLine();
            System.out.println("From Client: Received userPassword: " + userPassword);

            User u = new User(username, userPassword);
            if (optionChoosed.equalsIgnoreCase("2"))
            {
                if (userService.SignUp(u)){
                    clientWriter.println("successfully, Now SignIn!");
                    startSignUpOrSignIn();
                } else {
                    clientWriter.println("User already exists!, try SignIn!");
                    startSignUpOrSignIn();
                }
            }
            else
            {
                if (userService.SignIn(u)){
                    createOrChooseRoom();
                    // clientWriter.println("start messaging");
                } else {
                    clientWriter.println("User doesn't exist!");
                    startSignUpOrSignIn();
                }
            }

        }
        catch(Exception e){
            System.err.println("in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
        
    }

    public void closeEverything(Socket socket, BufferedReader clientRedaer, PrintWriter clientWriter) {
        userHandlers.remove(this);
        broadcastMessage("SERVER: " + username + " has left the chat!");
        try {
            if (clientRedaer != null) {
                clientRedaer.close();
            }
            if (clientWriter != null) {
                // clientWriter.println("SERVER: You have been disconnected.");
                clientWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

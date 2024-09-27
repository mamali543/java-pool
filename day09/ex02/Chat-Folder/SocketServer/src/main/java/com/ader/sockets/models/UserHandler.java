package com.ader.sockets.models;

import com.ader.sockets.models.Message;
import com.ader.sockets.models.Chatroom;
import com.ader.sockets.models.User;
import com.ader.sockets.service.MessageService;
import com.ader.sockets.service.ChatroomService;
import com.ader.sockets.service.ChatroomServiceImpl;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;
import com.ader.sockets.service.MessageServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ader.sockets.config.ApplicationConfig;


import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements Runnable {

    private static ArrayList<UserHandler> userHandlers = new ArrayList<>();
    private static ArrayList<Chatroom> chatrooms = new ArrayList<>();

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

    public void listRooms() {
        // try {
        //     List<String> rooms = userService.getRooms();
        //     for (String room : rooms) {
        //         clientWriter.println(room);
        //     }
        //     clientWriter.println("END_OF_ROOMS");
        // }
        // catch (Exception e) {
        //     System.err.println("Error in UserHandler run: " + e.getMessage());
        // }
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
            String optionChoosed = clientReader.readLine();
            System.out.println("option choosed: "+optionChoosed);
            if (optionChoosed.equals("1"))
            {
                clientWriter.println("Enter room name: ");

                String roomName = clientReader.readLine();
                System.out.println("From Client: Received roomName: " + roomName);
                System.out.println("user to get: " + username);
                User u = userService.getUser(username);
                Chatroom chatroom = new Chatroom(null, roomName, u.getUserId(), null, null);
                
                chatroomService.createChatroom(chatroom);
                clientWriter.println("Roooom: "+chatroom.getName()+" created successfully!");
                chatrooms.add(chatroom);
                createOrChooseRoom();
            }
            else if (optionChoosed.equals("2"))
            {          
                System.out.println("option choosed: "+optionChoosed);
                int i =0;
                options.clear();
                if (chatrooms.size() > 0)
                {
                    for (Chatroom chatroom : chatrooms) {
                        options.add(++i + ". " + chatroom.getName());
                        // System.out.println(chatroom.getName());
                    }
                    for (String option : options) {
                        clientWriter.println(option);
                    }
                    clientWriter.println(++i +". "+"exit");
                    clientWriter.println("END_OF_OPTIONS");
                    String roomChoosed = clientReader.readLine();
                    System.out.println("room choosed: "+ Integer.parseInt(roomChoosed));
                    Chatroom chatroom = chatrooms.get(Integer.parseInt(roomChoosed)-1);
                    clientWriter.println("Welcome to "+chatroom.getName()+" chatroom!");

                }
                else
                {
                    clientWriter.println("No chatrooms found!");
                    createOrChooseRoom();
                }
            }
            else
            {
                System.out.println("again!  ");
            }
            // System.out.println("again!  ");
        }
        catch(Exception e){
            System.err.println("Error in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
    }

    @Override
    public void run() {

        // try {
                sendOptions();
                startSignUpOrSignIn();
                // createOrChooseRoom();

                // this.username = clientReader.readLine();
                // System.out.println("username to add: "+ username);
                // userHandlers.add(this);
                // sendOptions2();
                // getOperation();
        //     broadcastMessage("\nSERVER: " + username + " has entered the chat");
        //     String messageFromClient;
        //     while (socket.isConnected()) {
        //             messageFromClient = clientReader.readLine();
        //             if (messageFromClient == null || messageFromClient.equalsIgnoreCase("exit"))
        //                 break;  // Client has disconnected
        //             if ((this.user = userService.getUser(username)) != null)
        //             {
        //                 LocalDateTime currentDateTime = LocalDateTime.now();
        //                 Message msg = new Message(null, this.user.getUserId(), messageFromClient.split(":", 2)[1].trim(), currentDateTime);
        //                 messageService.save(msg);
        //                 broadcastMessage(messageFromClient);
        //             }
        //     }
        // }
        // catch (IOException e) {
        //     System.err.println("Error in UserHandler run: " + e.getMessage());
        // } finally {
        //     closeEverything(socket, clientReader, clientWriter);
        // }
    } 

    public void sendOptions(){

        options.add("1. SignIn");
        options.add("2. SignUp");
        options.add("3. exit");
    }

    public void startSignUpOrSignIn() {
        try{
            for (String option : options) {
                clientWriter.println(option);
            }
            clientWriter.println("END_OF_OPTIONS");
            String optionChoosed = clientReader.readLine();

            if (optionChoosed.equals("3"))
                closeEverything(socket, clientReader, clientWriter);
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
            System.err.println("Error in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
        
    }

    public void broadcastMessage(String messageToSend) {
        for (UserHandler userHandler : userHandlers) {
            try {
                if (!userHandler.username.equals(username)) {
                    userHandler.clientWriter.println(messageToSend);
                    userHandler.clientWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
                closeEverything(socket, clientReader, clientWriter);
            }
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

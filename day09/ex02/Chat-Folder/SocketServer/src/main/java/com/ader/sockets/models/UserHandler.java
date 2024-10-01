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
import com.ader.sockets.utils.JsonUtil;

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
            
            sendJsonMessage("Hello from server!", null, null);

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
                sendJsonMessage(option, null, null);
            }
            sendJsonMessage("END_OF_OPTIONS", null, null);

            ChatMessage optionChoosed = receiveJsonMessage();
            System.out.println("option choosed: "+ optionChoosed.getMessage());
            if (optionChoosed.getMessage().equals("1"))
            {
                sendJsonMessage("Enter room name: ", null, null);

                ChatMessage roomNameMessage = receiveJsonMessage();
                String roomName = roomNameMessage.getMessage();
                User u = userService.getUser(username);
                Chatroom chatroom = new Chatroom(null, roomName, u.getUserId(), null, null);
                
                Long rommId = chatroomService.createChatroom(chatroom);
                chatroom.setId(rommId);
                sendJsonMessage("Room: " + chatroom.getName() + " created successfully!", null, null);
                chatrooms.add(chatroom);
                createOrChooseRoom();
            }
            else if (optionChoosed.getMessage().equals("2"))
            {          
                System.out.println("option choosed: "+optionChoosed);
                int i =0;
                options.clear();
                
                if (chatrooms.isEmpty())
                {
                    sendJsonMessage("No chatrooms found, create one!", null, null);
                    createOrChooseRoom();
                }
                else
                {
                    for (Chatroom chatroom : chatrooms) {
                        sendJsonMessage(++i + ". " + chatroom.getName(), null, null);
                    }
                    sendJsonMessage(++i + ". exit", null, null);
                    sendJsonMessage("END_OF_OPTIONS", null, null);
                    sendJsonMessage(String.valueOf(i), null, null);
                }
                
                //get the room choosed
                ChatMessage roomChosenMessage = receiveJsonMessage();
                String roomChoosed = roomChosenMessage.getMessage();
                if (roomChoosed.equals("exit"))
                {
                    closeEverything(socket, clientReader, clientWriter);
                    return ;
                }
                currentChatroom = chatrooms.get(Integer.parseInt(roomChoosed)-1);
                joinChatroom(currentChatroom);
            }
            else
                closeEverything(socket, clientReader, clientWriter);
        }
        catch(Exception e){
            System.err.println("in UserHandler chooseOrCreate room: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
    }

    public void joinChatroom(Chatroom chatroom) {
        try{
            this.currentChatroom = chatroom; // Set the current chatroom
            userHandlers.add(this);
            // Query the database for the last 30 messages
            sendJsonMessage("Welcome to " + chatroom.getName() + " chatroom!", null, chatroom.getId());
            List<Message> lastMessages = messageService.getLast30Messages(chatroom.getId());
            for (Message message : lastMessages) {
                System.out.println("message: "+message.getMessage());
                getLastMessages(message.getMessage(), message.getAuthorId(), chatroom.getId()); // Send each message to the user
            }
            broadcastMessage("SERVER: " + username + " has joined the chatroom " + chatroom.getName() , null, chatroom.getId());
        } catch (Exception e) {
            e.printStackTrace();
            closeEverything(socket, clientReader, clientWriter);
        }
    }

    public void broadcastMessage(String messageToSend, Long authorId, Long roomId) {
        for (UserHandler userHandler : userHandlers) {
            // Check if the user is in the same chatroom
            System.out.println("userHandler.lentgh: " + userHandlers.size());
            if (userHandler.currentChatroom != null && userHandler.currentChatroom.equals(this.currentChatroom) && !userHandler.username.equals(username) ) {
                try {
                    System.out.println("client to recieve message " + userHandler.username);
                    userHandler.sendJsonMessage(messageToSend, authorId, roomId);

                } catch (Exception e) {
                    e.printStackTrace();
                    closeEverything(socket, clientReader, clientWriter);
                }
            }
        }
    }

    public void getLastMessages(String messageToSend, Long authorId, Long roomId) {
        for (UserHandler userHandler : userHandlers) {
            // Check if the user is in the same chatroom
            if (userHandler.currentChatroom != null && userHandler.currentChatroom.equals(this.currentChatroom) && userHandler.username.equals(username) ) {
                try {
                    userHandler.sendJsonMessage(messageToSend, authorId, roomId);
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
                if (socket.isClosed()) {
                    return;  // Exit if the socket is closed after sign-in/sign-up
                }
                System.err.println("Start messaging in a chatroom!!!");
                ChatMessage messageFromClient;
                while (socket.isConnected()) {
                    messageFromClient = receiveJsonMessage();
                    System.out.println("messageFromClient: "+messageFromClient.getMessage());
                    if (messageFromClient == null || messageFromClient.getMessage().equalsIgnoreCase("exit") || messageFromClient.getMessage().equalsIgnoreCase("leave"))
                        break;  // Client has disconnected
                    else 
                    {
                        if ((this.user = userService.getUser(username)) != null)
                        {
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            Message msg = new Message(null, this.user.getUserId(), this.currentChatroom.getId(), messageFromClient.getMessage().split(":")[1].trim(), currentDateTime);
                            System.out.println("message: "+msg.getMessage());
                            broadcastMessage(messageFromClient.getMessage(), this.user.getUserId(), this.currentChatroom.getId());
                            messageService.save(msg);
                        }
                    }
                }   
            }
            catch (IOException e) {
                System.err.println("in UserHandler run methode: " + e.getMessage());
            }
            catch(Exception e){
                System.err.println("in UserHandler run methode: " + e.getMessage());
            } finally {
                closeEverything(socket, clientReader, clientWriter);
            }
    } 

    public void sendOptions(){
        options.clear();
        options.add("1. SignIn");
        options.add("2. SignUp");
        options.add("3. exit");
    }

    public void startSignUpOrSignIn() {
        sendOptions();
        try{
            for (String option : options) {
                sendJsonMessage(option, null, null);
            }
            sendJsonMessage("END_OF_OPTIONS", null, null);
            ChatMessage optionChoosed = receiveJsonMessage();
            if (optionChoosed.getMessage().equals("3"))
            {
                closeEverything(socket, clientReader, clientWriter);
                return ;
            }
            sendJsonMessage("enter  username:", null, null);
            
            ChatMessage usernameMessage = receiveJsonMessage();
            username = usernameMessage.getMessage();
            System.out.println("From Client: Received username: " + username);
            
            sendJsonMessage("Enter Password: ", null, null);

            ChatMessage passwordMessage = receiveJsonMessage();
            String userPassword = passwordMessage.getMessage();
            System.out.println("From Client: Received userPassword: " + userPassword);

            User u = new User(username, userPassword);
            if (optionChoosed.getMessage().equalsIgnoreCase("2"))
            {
                if (userService.SignUp(u)){
                    sendJsonMessage("successfully, Now SignIn!", null, null);
                    startSignUpOrSignIn();
                } else {
                    sendJsonMessage("User already exists!, try SignIn!", null, null);
                    startSignUpOrSignIn();
                }
            }
            else
            {
                if (userService.SignIn(u)){
                    createOrChooseRoom();
                    // clientWriter.println("start messaging");
                } else {
                    sendJsonMessage("User doesn't exist!", null, null);
                    startSignUpOrSignIn();
                }
            }

        }
        catch(Exception e){
            System.err.println("in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
        
    }

    public void sendJsonMessage(String message, Long fromId, Long roomId) throws Exception {
        ChatMessage chatMessage = new ChatMessage(message, fromId, roomId);
        String jsonMessage = JsonUtil.toJson(chatMessage);
        clientWriter.println(jsonMessage);
    }

    public ChatMessage receiveJsonMessage() throws Exception {
        String jsonMessage = clientReader.readLine();
        return JsonUtil.fromJson(jsonMessage, ChatMessage.class);
    }

    public void closeEverything(Socket socket, BufferedReader clientRedaer, PrintWriter clientWriter) {
        userHandlers.remove(this);
        broadcastMessage("SERVER: " + username + " has left the chat!", null, null);
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

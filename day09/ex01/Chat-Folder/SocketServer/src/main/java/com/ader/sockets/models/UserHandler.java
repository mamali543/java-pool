package com.ader.sockets.models;

import com.ader.sockets.models.User;
import com.ader.sockets.service.UserService;
import com.ader.sockets.service.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ader.sockets.config.ApplicationConfig;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class UserHandler implements Runnable {

    public static ArrayList<UserHandler> userHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader clientReader;
    private PrintWriter clientWriter;
    private UserService userService;
    String username;

    public UserHandler(Socket socket) {
        try{
            this.socket = socket;
            clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientWriter = new PrintWriter(socket.getOutputStream(), true);
            
            clientWriter.println("Hello from server!");
            clientWriter.flush();

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            userService = context.getBean(UserServiceImpl.class);
            // context.close();
        }
        catch(Exception e){
            System.err.println("Error in UserHandler: " + e.getMessage());
            closeEverything(socket, clientReader, clientWriter);
        }
    }
    @Override
    public void run() {
        try {

            startSignUpOrSignIn();
            
            this.username = clientReader.readLine();
            System.out.println("username to add: "+ username);
            userHandlers.add(this);
            broadcastMessage("\nSERVER: " + username + " has entered the chat");
            String messageFromClient;
            while (socket.isConnected()) {
                    messageFromClient = clientReader.readLine();
                    if (messageFromClient == null) {
                        break;  // Client has disconnected
                    }
                    broadcastMessage(messageFromClient);
            }
        }
        catch (IOException e) {
            System.err.println("Error in UserHandler run: " + e.getMessage());
        } finally {
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
                clientWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSignUpOrSignIn() {
        try{
    
            String s;
            while ((s = clientReader.readLine()) != null) {
                clientWriter.println("enter  username:");
                
                String userName = clientReader.readLine();
                System.out.println("From Client: Received username: " + userName);
                
                clientWriter.println("Enter Password: ");

                String userPassword = clientReader.readLine();
                System.out.println("From Client: Received userPassword: " + userPassword);

                User u = new User(userName, userPassword);
                if (s.equalsIgnoreCase("SignUp"))
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
                        clientWriter.println("start messaging");
                    } else {
                        clientWriter.println("User doesn't exist!, choose SignUp");
                        startSignUpOrSignIn();
                    }
                }
                break;
            }
        }
        catch(Exception e){
            System.err.println("Error in UserHandler: " + e.getMessage());
        }
        
    }
    
}

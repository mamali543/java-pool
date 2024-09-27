package com.ader.sockets.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

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
            System.out.println("Choose an option: ");
            signUpOrSignIn(serverMessage);
            // chooseOrCreateRoom(serverMessage);
            // serverWriter.println(userName);
            // // System.out.print(">");
            // listener();
            // while (running) {
            //     String userMessage = reader.readLine();
            //     if (userMessage.equalsIgnoreCase("exit"))
            //     {
            //         serverWriter.println("exit");
            //         closeEverything(socket, serverReader, serverWriter);
            //         running = false;
            //     }
            //     else
            //     {
            //         serverWriter.println(userName + ": " + userMessage);
            //         // System.out.print(">");
            //     }
            // }

        } catch (Exception e) {
            System.err.println("Unexpected error in client: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, serverReader, serverWriter);
        }
    }

    public void chooseOrCreateRoom(String serverMessage)
    {
        String line;
        try{
            while (!(line = serverReader.readLine()).equals("END_OF_OPTIONS")) {
                System.out.println(line);
            }
            System.out.println("Enter your option: ");
            String options = reader.readLine();
            while (!(options.equals("1") || options.equals("2") || options.equals("3"))) {
                System.out.println("Invalid option, please choose 1, 2 or 3");
                System.out.print("> ");
                options = reader.readLine();
            }
            if (options.equals("1"))
            {
                serverWriter.println(options);
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
            else if (options.equals("2"))
            {
                serverWriter.println(options);
                line = serverReader.readLine();
                if (line.equals("No chatrooms found!"))
                {
                    System.out.println(line);
                    chooseOrCreateRoom(serverMessage);
                }
                else
                {                    
                    while (!(line .equals("END_OF_OPTIONS"))) {
                        System.out.println(line);
                        line = serverReader.readLine();
                    }
                    line = reader.readLine();
                    serverWriter.println(line);
                    serverMessage = serverReader.readLine();
                    System.out.println(serverMessage);
                    // chooseOrCreateRoom(serverMessage);
                }
            }
            else if (options.equals("3"))
            {
                System.out.println("again!  ");
            }
            // System.out.println("again!  ");
            // chooseOrCreateRoom(serverMessage);
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
package com.ader.sockets.app;

import com.ader.sockets.client.Client;
public class App 
{
    public static void main( String[] args )
    {
        if (args[0].startsWith("--port=") && args.length == 1)
        {
            Client client =  new Client(Integer.parseInt(args[0].split("=")[1]));
            client.start();
        }
        else
            System.err.println("Usage hna: java -jar target/SocketClient.jar --port=8081");
    }
}

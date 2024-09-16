package com.ader.sockets.app;

/**
 * Hello world!
 *
 */
import com.ader.sockets.server.Server;
public class App 
{
    public static void main( String[] args )
    {
        if (args[0].startsWith("--port=") && args.length == 1)
        {
            Server server =  new Server(Integer.parseInt(args[0].split("=")[1]));
            server.start();
        }
        else
            System.err.println("Usage hna: java -jar target/SocketServer.jar --port=8081");
    }
}

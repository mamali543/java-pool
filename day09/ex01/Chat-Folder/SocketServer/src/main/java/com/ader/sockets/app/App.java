package com.ader.sockets.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ader.sockets.config.ApplicationConfig;


import com.ader.sockets.server.Server;
public class App 
{
    public static void main( String[] args )
    {
        if (args[0].startsWith("--port=") && args.length == 1)
        {
            try{
                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
                Server server =  context.getBean(Server.class);
                server.setPort(Integer.parseInt(args[0].split("=")[1]));
                server.init();
                server.start();
            }
            catch (Exception e)
            {
                System.err.println("Error: " + e.getMessage());
            }
        }
        else
            System.err.println("Usage hna: java -jar target/SocketServer.jar --port=8081");
    }
}

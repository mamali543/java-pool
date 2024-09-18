package com.ader.sockets.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ader.sockets.client.Client;
import com.ader.sockets.config.ApplicationConfig;
public class App 
{
    public static void main( String[] args )
    {
        if (args[0].startsWith("--port=") && args.length == 1)
        {
            try
            {
                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
                Client client =  context.getBean(Client.class);
                client.setPort(Integer.parseInt( args[0].split("=")[1]));
                client.init();
                client.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
            System.err.println("Usage hna: java -jar target/SocketClient.jar --port=8081");
    }
}

package main.java.server;

import main.java.server.view.ServerPanel;

import java.rmi.Naming;

/**
 * @Author Marco De Caria
 * Applicazione server
 * */


public class ServerApplication {

    public static void main(String... args) {
        try{
            ServerEngine serverEngine = ServerEngine.getInstance();
            Naming.rebind("server", serverEngine);
            new Thread(()->{
                new ServerPanel();
            }).start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

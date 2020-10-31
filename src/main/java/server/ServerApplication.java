package main.java.server;

import main.java.common.interfaces.ServerIF;

import java.rmi.Naming;

public class ServerApplication {

    public static void main(String... args) {
        try{
            ServerEngine serverEngine = new ServerEngine();
            Naming.rebind("server", serverEngine);
            //System.out.println(serverEngine.getAvailableExams());
            new Thread(()->{
                new ServerPanel(serverEngine);
            }).start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

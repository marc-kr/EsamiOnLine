package main.java.server;

import main.java.server.view.ExamMaker;
import main.java.server.view.ServerPanel;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ServerApplication {

    public static void main(String... args) {
        try{
            ServerEngine serverEngine = ServerEngine.getInstance();
            Naming.rebind("server", serverEngine);
            new Thread(()->{
                //new ServerPanel(serverEngine);
                new ExamMaker();
            }).start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

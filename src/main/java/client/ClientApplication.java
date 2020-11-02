package main.java.client;

import main.java.client.view.ClientPanel;
import main.java.common.interfaces.ServerIF;

import java.rmi.Naming;

public class ClientApplication {
    public static void main(String... args) {
        try {
            ServerIF server = (ServerIF) Naming.lookup("server");
            new Thread(()-> {
                new ClientPanel(server);
            }).start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

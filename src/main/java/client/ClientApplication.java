package main.java.client;

import main.java.common.interfaces.ServerIF;

import java.rmi.Naming;

public class ClientApplication {
    public static void main(String... args) {
        try {
            ServerIF server = (ServerIF) Naming.lookup("server");

        }catch(Exception ex) {

        }
    }
}

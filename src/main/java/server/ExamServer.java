package main.java.server;

import main.java.common.interfaces.ExamClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ExamServer extends UnicastRemoteObject implements main.java.common.interfaces.ExamServer {
    private ExamManager manager;

    protected ExamServer(ExamManager manager) throws RemoteException {
        this.manager = manager;
    }

    @Override
    public void joinExam(ExamClient client) throws RemoteException {
        manager.addStudent(client);
    }


}

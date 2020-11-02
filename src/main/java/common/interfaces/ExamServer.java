package main.java.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExamServer extends Remote {
    public void joinExam(ExamClient client) throws RemoteException;
}

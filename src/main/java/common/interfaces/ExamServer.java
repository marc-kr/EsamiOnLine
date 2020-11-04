package main.java.common.interfaces;

import main.java.common.exceptions.ExamInProgressException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExamServer extends Remote {
    void joinExam(ExamClient client) throws RemoteException, ExamInProgressException;
}

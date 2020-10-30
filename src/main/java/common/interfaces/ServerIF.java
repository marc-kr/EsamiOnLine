package main.java.common.interfaces;

import main.java.common.entities.Exam;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerIF extends Remote {
    List<Exam> getAvailableExams() throws RemoteException;
    void subscribeToExam(int studentId, int examId) throws RemoteException;
    void joinExam(int studentId, int examId) throws RemoteException;
}

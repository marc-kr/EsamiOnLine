package main.java.common.interfaces;

import main.java.common.entities.Answer;
import main.java.common.entities.Question;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ExamClient extends Remote {
    int getStudentId() throws RemoteException;
    void submitExam() throws RemoteException;
    Map<Question, Answer> getResult() throws RemoteException;
    void update(String state) throws RemoteException;
}

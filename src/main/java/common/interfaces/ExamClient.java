package main.java.common.interfaces;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ExamClient extends Remote {
    void setExam(Exam exam) throws RemoteException;
    int getStudentId() throws RemoteException;
    void submitExam() throws RemoteException;
    List<AnsweredQuestion> getResult() throws RemoteException;
    void update() throws RemoteException;
}

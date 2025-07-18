package main.java.common.interfaces;

import main.java.common.entities.Exam;
import main.java.common.exceptions.StudentAlreadySubscribedException;
import main.java.common.exceptions.StudentNotSubscribedException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerIF extends Remote {
    List<Exam> getAvailableExams() throws RemoteException;
    List<Exam> getOpenedExams() throws RemoteException;
    boolean subscribeToExam(int studentId, int examId) throws RemoteException, StudentAlreadySubscribedException;
    ExamServer joinExam(int studentId, int examId) throws RemoteException, StudentNotSubscribedException;
}

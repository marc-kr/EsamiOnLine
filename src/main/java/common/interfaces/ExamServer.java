package main.java.common.interfaces;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.exceptions.ExamInProgressException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ExamServer extends Remote {
    void joinExam(ExamClient client) throws RemoteException, ExamInProgressException;
    void submitResult(int studentId, Map<Question, Answer> result) throws RemoteException;
    Exam getExam() throws RemoteException;
}

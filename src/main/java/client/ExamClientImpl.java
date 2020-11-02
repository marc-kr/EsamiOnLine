package main.java.client;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.interfaces.ExamClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ExamClientImpl extends UnicastRemoteObject implements ExamClient {

    protected ExamClientImpl() throws RemoteException {

    }

    @Override
    public void setExam(Exam exam) {

    }

    @Override
    public int getStudentId() {
        return 0;
    }

    @Override
    public void submitExam() {

    }

    @Override
    public List<AnsweredQuestion> getResult() {
        return null;
    }

    @Override
    public void update() {

    }
}

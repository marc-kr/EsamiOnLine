package main.java.server;

import main.java.common.entities.Exam;
import main.java.common.interfaces.ServerIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerEngine extends UnicastRemoteObject implements ServerIF {

    protected ServerEngine() throws RemoteException {

    }

    @Override
    public List<Exam> getAvailableExams() {
        return DBService.getInstance().getAvailableExams();
    }

    @Override
    public void subscribeToExam(int studentId, int examId) {
        DBService.getInstance().subscribeStudent(studentId, examId);
    }

    @Override
    public void joinExam(int studentId, int examId) {
        if(!DBService.getInstance().isStudentSubscribed(studentId, examId)) {
            //TODO gestire la non iscrizione
        }
    }

    protected void openExam(int examId) {
        Exam e = DBService.getInstance().getExam(examId);
        //TODO implementare apertura esame
    }
}

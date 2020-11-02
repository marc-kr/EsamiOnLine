package main.java.server;

import main.java.common.entities.Exam;
import main.java.common.interfaces.ServerIF;
import main.java.server.services.DBService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerEngine extends UnicastRemoteObject implements ServerIF {
    private Map<Exam, ExamManager> openedExams;

    protected ServerEngine() throws RemoteException {
        openedExams = new HashMap<>();
    }

    @Override
    public List<Exam> getAvailableExams() {
        return DBService.getInstance().getAvailableExams();
    }

    @Override
    public List<Exam> getOpenedExams() throws RemoteException {
        return new ArrayList<Exam>(openedExams.keySet());
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

    public ExamManager openExam(int examId) {
        Exam e = DBService.getInstance().getExam(examId);
        ExamManager manager = new ExamManager(e);
        openedExams.put(e, manager);
        return manager;
    }

    public void endExam(int examId) {
        openedExams.remove(examId);
    }


}

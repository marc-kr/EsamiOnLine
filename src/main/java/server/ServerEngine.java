package main.java.server;

import main.java.common.entities.Exam;
import main.java.common.exceptions.StudentAlreadySubscribedException;
import main.java.common.exceptions.StudentNotSubscribedException;
import main.java.common.interfaces.ExamServer;
import main.java.common.interfaces.ServerIF;
import main.java.server.services.DBService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Marco De Caria
 * Contiene tutta la logica di funzionamento del server. Si occupa delle richieste da parte dei client
 * e avvia gli esami delegandone la gestione a istanze di ExamServer
 * */

public class ServerEngine extends UnicastRemoteObject implements ServerIF, ExamObserver {
    private Map<Exam, ExamServer> openedExams;
    private static ServerEngine instance;

    private ServerEngine() throws RemoteException {
        openedExams = new HashMap<>();
    }

    public synchronized static ServerEngine getInstance(){
        if(instance == null) {
            try {
                instance = new ServerEngine();
            } catch (RemoteException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return instance;
    }

    @Override
    public List<Exam> getAvailableExams() {
        return DBService.getInstance().getAvailableExams();
    }

    @Override
    public List<Exam> getOpenedExams() throws RemoteException {
        return new ArrayList<>(openedExams.keySet());
    }

    @Override
    public boolean subscribeToExam(int studentId, int examId) throws StudentAlreadySubscribedException {
        if(DBService.getInstance().isStudentSubscribed(studentId, examId)) throw new StudentAlreadySubscribedException();
        if(DBService.getInstance().subscribeStudent(studentId, examId) != null)
            return true;
        return false;
    }

    @Override
    public ExamServer joinExam(int studentId, int examId) throws StudentNotSubscribedException {
        if(!DBService.getInstance().isStudentSubscribed(studentId, examId)) {
            throw new StudentNotSubscribedException();
        }
        Exam e = DBService.getInstance().getExam(examId);
        return openedExams.get(e);
    }

    public ExamManager openExam(int examId) throws RemoteException {
        Exam e = DBService.getInstance().getExam(examId);
        ExamManager manager = new ExamManager(e);
        manager.attach(this);
        openedExams.put(e, manager);
        return manager;
    }

    @Override
    public void update(ExamManager examManager) {
        if(examManager.getState().equals("STARTED") || examManager.getState().equals("ENDED")) {
            System.out.println("L'esame " + examManager.getExam().getName() + " è iniziato/terminato");
            this.openedExams.remove(examManager.getExam());
            examManager.detach(this);
        }
    }
}

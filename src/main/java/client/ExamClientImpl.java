package main.java.client;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Marco De Caria
 * Si occupa della comunicazione con il server durante lo svolgimento della prova.
 * Mantiene le risposte fornite dallo studente mediante la prova e comunica al server il risultato al momento
 * della consegna o al termine dell'esame.
 * Riceve notifiche dal server per i cambiamenti di stato dell'esame.
 * */
public class ExamClientImpl implements ExamClient {
    private ExamServer server;
    private int studentId;
    private Exam exam;
    private Map<Question, Answer> answers;
    private List<ClientObserver> observers;

    public ExamClientImpl(int studentId, ExamServer server) throws RemoteException, ExamInProgressException {
        if(server == null) throw new ExamInProgressException();
        this.server = server;
        this.studentId = studentId;
        this.answers = new HashMap<>();
        this.observers = new ArrayList<>();
        UnicastRemoteObject.exportObject(this, 1098);
        server.joinExam(this);
        this.exam = server.getExam();
    }

    public Exam getExam() { return exam; }

    @Override
    public int getStudentId() {
        return studentId;
    }

    @Override
    public void submitExam() throws RemoteException {
        server.submitResult(studentId, answers);
    }

    @Override
    public Map<Question, Answer> getResult() {
        return answers;
    }

    @Override
    public void update(String state) throws RemoteException {
        System.out.println("Aggiornamento dal server " + state);
        if(state.equals("ENDED")){
            submitExam();
        }
        notifyObservers(state);
    }

    public void setAnswer(Question q, Answer a) {
        answers.put(q, a);
    }

    public void attach(ClientObserver observer) {
        this.observers.add(observer);
    }

    public void detach(ClientObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyObservers(String s) {
        for(ClientObserver observer : observers)
            observer.update(s);
    }
}

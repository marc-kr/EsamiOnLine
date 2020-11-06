package main.java.client;

import main.java.client.view.ExamWindow;
import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
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
    private ExamWindow window;

    public ExamClientImpl(int studentId, ExamServer server) throws RemoteException, ExamInProgressException {
        if(server == null) throw new ExamInProgressException();
        this.server = server;
        this.studentId = studentId;
        this.answers = new HashMap<>();
        UnicastRemoteObject.exportObject(this, 1098);
        server.joinExam(this);
        this.exam = server.getExam();
    }

    public void setWindow(ExamWindow window) {
        this.window = window;
    }

    public Exam getExam() { return exam; }

    @Override
    public int getStudentId() {
        return studentId;
    }

    @Override
    public void submitExam() throws RemoteException {
        System.out.println("Invio ");
        System.out.println(answers.size());
        for(Answer a : answers.values())
            System.out.println(a);
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
        window.update(state);
    }


    public void setAnswer(Question q, Answer a) {
        answers.put(q, a);
    }
}

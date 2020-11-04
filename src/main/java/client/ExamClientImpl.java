package main.java.client;

import main.java.client.view.ExamWindow;
import main.java.common.entities.Answer;
import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamClientImpl implements ExamClient {
    private ExamServer server;
    private int studentId;
    private Exam exam;
    private Map<Question, Answer> answers;
    private ExamWindow window;

    public ExamClientImpl(int studentId, ExamServer server) throws RemoteException, ExamInProgressException {
        this.server = server;
        this.studentId = studentId;
        this.window = window;
        UnicastRemoteObject.exportObject(this, 1098);
        server.joinExam(this);

    }

    public void setWindow(ExamWindow window) {
        this.window = window;
    }

    public Exam getExam() { return exam; }

    @Override
    public void setExam(Exam exam) {
        this.exam = exam;
        answers = new HashMap<>();
        for(Question question : exam.getQuestions()) {
            answers.put(question, null);
        }
        System.out.println("Ricevuto esame " + exam);
    }

    @Override
    public int getStudentId() {
        return studentId;
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
        System.out.println("Aggiornamento dal server");
        window.update();
    }


    public void setAnswer(Question q, Answer a) {
        answers.put(q, a);
    }
}

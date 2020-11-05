package main.java.server;

import main.java.common.entities.Answer;
import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Question;
import main.java.common.interfaces.ExamClient;
import main.java.server.services.DBService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExamServer extends UnicastRemoteObject implements main.java.common.interfaces.ExamServer {
    private ExamManager manager;

    protected ExamServer(ExamManager manager) throws RemoteException {
        this.manager = manager;
    }

    @Override
    public void joinExam(ExamClient client) throws RemoteException {
        manager.addStudent(client);
    }

    @Override
    public void submitResult(int studentId, Map<Question, Answer> result) throws RemoteException {
        int res=0;
        List<Answer> answers = new LinkedList<>();
        System.out.println(result.size());
        for(Answer answer: result.values()) {
            answers.add(answer);
            if(answer == null) res -=1;
            else if(answer.getCorrect()) res += 3;
        }
        DBService.getInstance().registerResult(manager.getExam().getId(), studentId, res, answers);
    }


}

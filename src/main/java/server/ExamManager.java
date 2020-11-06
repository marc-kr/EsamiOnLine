package main.java.server;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;
import main.java.server.services.DBService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * @Author Marco De Caria
 * Si occupa della gestione dell'esame, mantenendone lo stato e notificando i client al suo cambiamento.
 * */

public class ExamManager extends UnicastRemoteObject implements ExamServer {
    private interface ExamStateIF {
        void entryAction(ExamManager manager);
        void exitAction(ExamManager manager);
        void start(ExamManager manager);
        void end(ExamManager manager);
        void addStudent(ExamManager manager, ExamClient student) throws ExamInProgressException;
        String getName();
    }

    private enum ExamState implements ExamStateIF {
        /**
         * Stato in cui l'esame è aperto e gli studenti possono mettersi in attesa dell'avvio della prova
         * */
        OPENED{
            @Override
            public void entryAction(ExamManager manager) {
                System.out.println("L'esame " + manager.exam.getName() + " è stato aperto.");
            }

            @Override
            public void addStudent(ExamManager manager, ExamClient student){
                try {
                    manager.students.put(student.getStudentId(), student);
                    manager.notifyObservers();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void start(ExamManager manager) {
                manager.transition(STARTED);
            }
        },
        /**
         * Stato in cui l'esame è in svolgimento
         * */
        STARTED{
            @Override
            public void entryAction(ExamManager manager) {
                System.out.println("L'esame " + manager.exam.getName() + " è stato avviato.");
            }

            @Override
            public void end(ExamManager manager) {
                manager.transition(ENDED);
            }
        },
        /**
         * Stato in cui l'esame è terminato e i risultati vengono registrati
         * */
        ENDED{
            @Override
            public void entryAction(ExamManager manager) {
                System.out.println("L'esame " + manager.exam.getName() + " è stato terminato.");
            }
        }
        ;

        @Override
        public void entryAction(ExamManager manager) {
            manager.updateState();
        }

        @Override
        public void exitAction(ExamManager manager) {

        }

        @Override
        public void start(ExamManager manager) {

        }

        @Override
        public void end(ExamManager manager) {

        }

        @Override
        public void addStudent(ExamManager manager, ExamClient student) throws ExamInProgressException {
            throw new ExamInProgressException();
        }

        @Override
        public String getName() {
            return this.name();
        }
    }

    private Map<Integer, ExamClient> students;
    private Exam exam;
    private ExamStateIF currentState;
    private List<ExamObserver> observers;

    public ExamManager(Exam exam) throws RemoteException{
        super();
        this.exam = exam;
        students = new HashMap<>();
        observers = new ArrayList<>();
        transition(ExamState.OPENED);
    }

    @Override
    public void joinExam(ExamClient client) throws ExamInProgressException {
        currentState.addStudent(this, client);
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
        DBService.getInstance().registerResult(exam.getId(), studentId, res, answers);
        students.remove(studentId);
        notifyObservers();
    }

    public Exam getExam() {
        return exam;
    }

    public void start() {
        System.out.println(this);
        System.out.println("Avvio esame, studenti partecipanti: " + students.size());
        currentState.start(this);
    }

    public void end() {
        currentState.end(this);
    }

    public List<Integer> getStudents() {
        return new ArrayList<>(students.keySet());
    }

    private void updateClients(String state) {
        System.out.println(this);
        System.out.println("Notifico i " + students.size() + " studenti");
        for(ExamClient client : students.values()) {
            try {
                client.update(state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateState() {
        notifyObservers();
        updateClients(currentState.getName());
        DBService.getInstance().updateExamState(exam.getId(), currentState.getName());
    }

    private void transition(ExamStateIF nextState) {
        if(currentState != null)
            currentState.exitAction(this);
        currentState = nextState;
        currentState.entryAction(this);
    }

    public void attach(ExamObserver observer) {
        observers.add(observer);
    }

    public void detach(ExamObserver observer) {
        observers.remove(observer);
    }

    public String getState() {
        return currentState.getName();
    }

    void notifyObservers() {
        for(ExamObserver observer : observers)
            observer.update(this);
    }

}

package main.java.server;


import main.java.common.entities.Exam;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.interfaces.ExamClient;
import main.java.server.services.DBService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Marco De Caria
 * Si occupa della gestione dell'esame, mantenendone lo stato e notificando i client al suo cambiamento.
 * */


public class ExamManager {
    private enum State {
        OPENED, STARTED, ENDED;
    };

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
            public void addStudent(ExamManager manager, ExamClient student) throws ExamInProgressException {
                manager.students.add(student);
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
            public void end(ExamManager manager) {
                manager.transition(ENDED);
            }
        },
        /**
         * Stato in cui l'esame è terminato e i risultati vengono registrati
         * */
        ENDED{
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

    private final List<ExamClient> students;
    private Exam exam;
    private State state;
    private ExamStateIF currentState;

    public ExamManager(Exam exam) {
        this.exam = exam;
        students = new ArrayList<>();
        state = State.OPENED;
        transition(ExamState.OPENED);
        System.out.println("Creato oggetto" + this);
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

    public List<ExamClient> getStudents() {
        return students;
    }

    private void updateClients(String state) {
        System.out.println(this);
        System.out.println("Notifico i " + students.size() + " studenti");
        for(ExamClient client : students) {
            try {
                client.update(state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateState() {
        updateClients(currentState.getName());
        DBService.getInstance().updateExamState(exam.getId(), currentState.getName());
    }

    public void addStudent(ExamClient client) {
        try {
            currentState.addStudent(this, client);
        } catch (ExamInProgressException e) {
            e.printStackTrace();
        }
    }

    private void transition(ExamStateIF nextState) {
        if(currentState != null)
            currentState.exitAction(this);
        currentState = nextState;
        currentState.entryAction(this);
    }
}

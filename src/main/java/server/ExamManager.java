package main.java.server;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.interfaces.ExamClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ExamManager {
    private enum State {
        OPENED, STARTED, ENDED;
    };

    private List<ExamClient> students;
    private Exam exam;
    private State state;

    public ExamManager(Exam exam) {
        this.exam = exam;
        students = new ArrayList<>();
        state = State.OPENED;
    }

    public Exam getExam() {
        return exam;
    }

    public void start() {
        System.out.println("Avvio esame, studenti partecipanti: " + students.size());
        sendExam();
        state = State.STARTED;
        updateClients();
    }

    public void end() {
        state = State.ENDED;
        updateClients();
    }

    private void sendExam() {
        System.out.println("Invio esami ai " + students.size() + " studenti");
        for(ExamClient client : students) {
            try {
                client.setExam(exam);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateClients() {
        System.out.println("Notifico i " + students.size() + " studenti");
        for(ExamClient client : students) {
            try {
                client.update();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addStudent(ExamClient client) {
        if(state == State.OPENED) {
            try {
                students.add(client);
                System.out.println("Aggiunto studente " + client.getStudentId());
                System.out.println("Totale: " + students.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

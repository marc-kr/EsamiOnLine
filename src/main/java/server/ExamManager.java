package main.java.server;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;
import main.java.server.services.DBService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamManager extends UnicastRemoteObject implements ExamServer {
    private interface ExamStateIF{
        void entryAction(ExamManager examManager);
        void exitAction(ExamManager examManager);
        void start(ExamManager examManager);
        void end(ExamManager examManager);
        void addStudent(ExamManager manager, ExamClient student);
    }

    private enum ExamState implements ExamStateIF {
        OPENED{
            @Override
            public void entryAction(ExamManager examManager) {
                examManager.students = new HashMap<>();
            }

            @Override
            public void addStudent(ExamManager manager, ExamClient student) {
                try {
                    manager.students.put(student.getStudentId(), student);
                    student.setExam(manager.exam);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void start(ExamManager examManager) {
                examManager.transition(STARTED);
            }
        },
        STARTED{
            @Override
            public void entryAction(ExamManager examManager) {
                for(ExamClient student : examManager.students.values()) {
                    try {
                        student.setExam(examManager.exam);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                examManager.notifyStudents();
            }

            @Override
            public void exitAction(ExamManager examManager) {
                for(ExamClient student : examManager.students.values()){
                    List<AnsweredQuestion> result = null;
                    try {
                        result = student.getResult();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    int r=0;
                    for(AnsweredQuestion question : result) {
                        if(question.getAnswer() == null)
                            r-=1;
                        else if(question.getAnswer().getCorrect())
                            r+=3;
                    }
                    try {
                        DBService.getInstance().registerResult(examManager.exam.getId(), student.getStudentId(), r, result);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
        ENDED{
            @Override
            public void entryAction(ExamManager examManager) {
                examManager.notifyStudents();
            }
        }
        ;

        @Override
        public void entryAction(ExamManager examManager) {

        }

        @Override
        public void exitAction(ExamManager examManager) {

        }

        @Override
        public void start(ExamManager examManager) {

        }

        @Override
        public void end(ExamManager examManager) {

        }

        @Override
        public void addStudent(ExamManager manager, ExamClient student) {

        }
    }

    private Exam exam;
    private Map<Integer, ExamClient> students;
    private ExamStateIF currentState;

    public ExamManager(Exam exam) throws RemoteException {
        super();
        this.exam = exam;
        transition(ExamState.OPENED);
    }

    private void transition(ExamStateIF nextState) {
        if(currentState != null)
            currentState.exitAction(this);
        currentState = nextState;
        currentState.entryAction(this);
    }

    public void start() {
        currentState.start(this);
    }

    public void end() {
        currentState.end(this);
    }

    public Exam getExam() {
        return exam;
    }

    @Override
    public void joinExam(ExamClient client) throws RemoteException{
        currentState.addStudent(this, client);
    }

    void notifyStudents() {
        System.out.println("Aggiorno i client");
        for(ExamClient client : students.values()) {
            try {
                client.update();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}

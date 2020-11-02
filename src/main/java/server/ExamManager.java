package main.java.server;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.interfaces.ExamClient;
import main.java.server.services.DBService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamManager {

    private interface ExamStateIF{
        void entryAction(ExamManager examManager);
        void exitAction(ExamManager examManager);
        void start(ExamManager examManager);
        void end(ExamManager examManager);
    }

    private enum ExamState implements ExamStateIF {
        OPENED{
            @Override
            public void entryAction(ExamManager examManager) {
                examManager.students = new HashMap<>();
            }

        },
        STARTED{
            @Override
            public void entryAction(ExamManager examManager) {
                for(ExamClient student : examManager.students.values()) {
                    student.setExam(examManager.exam);
                }
                //TODO notifica client inizio esame
            }

            @Override
            public void exitAction(ExamManager examManager) {
                for(ExamClient student : examManager.students.values()){
                    List<AnsweredQuestion> result = student.getResult();
                    int r=0;
                    for(AnsweredQuestion question : result) {
                        if(question.getAnswer() == null)
                            r-=1;
                        else if(question.getAnswer().getCorrect())
                            r+=3;
                    }
                    DBService.getInstance().registerResult(examManager.exam.getId(), student.getStudentId(), r, result);
                }
                //TODO notifica client termine esame
            }
        },
        ENDED{

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

    }

    private Exam exam;
    private Map<Integer, ExamClient> students;
    private ExamStateIF currentState;

    public ExamManager(Exam exam) {
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

}

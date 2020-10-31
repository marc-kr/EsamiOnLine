package main.java.common.interfaces;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;

import java.rmi.Remote;
import java.util.List;

public interface ExamClient extends Remote {
    void setExam(Exam exam);
    int getStudentId();
    void submitExam();
    List<AnsweredQuestion> getResult();
}

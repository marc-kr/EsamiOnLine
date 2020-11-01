package main.java.server;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;

public class TestEntities {
    public static void main(String... args) {
        StringBuilder stringBuilder =  new StringBuilder();
        Exam e = DBService.getInstance().getExam(1);
        for(Question q : e.getQuestions()) {
            stringBuilder.append(q.getDescription() + "\n");
            for(Answer a : q.getAnswers()) {
                stringBuilder.append(a.getId() + " " +a.getDescription());
                if(a.getCorrect()) stringBuilder.append(" *\n");
                else stringBuilder.append("\n");
            }
        }
        System.out.println(stringBuilder.toString());
    }
}

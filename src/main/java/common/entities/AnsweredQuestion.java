package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;
/**
 * @Author Marco De Caria
 * */

@Entity
@Table(name = "answered_question", schema = "online_exams")
public class AnsweredQuestion implements Serializable {
    private Integer id;
    private Integer student;
    private Answer answer;
    private int exam;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "student", nullable = false)
    public Integer getStudent() {
        return student;
    }

    public void setStudent(Integer student) {
        this.student = student;
    }


    @ManyToOne
    @JoinColumn(name = "answer")
    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name="exam", nullable = false)
    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AnsweredQuestion)) return false;
        AnsweredQuestion answeredQuestion = (AnsweredQuestion) o;
        return student == answeredQuestion.student && answeredQuestion.answer.equals(answer);
    }

    @Override
    public int hashCode() {
        int result=0;
        result = 31 * student + answer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AnsweredQuestion{" +
                "id=" + id +
                ", student=" + student +
                ", answer=" + answer +
                ", exam=" + exam +
                '}';
    }
}

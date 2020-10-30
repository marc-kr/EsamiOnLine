package main.java.common.entities;

import javax.persistence.*;

@Entity
@Table(name = "exam_registration", schema = "online_exams")
public class ExamRegistration {
    private Integer id;
    private Integer student;
    private Integer exam;
    private Integer result;

    @Id
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

    @Basic
    @Column(name = "exam", nullable = false)
    public Integer getExam() {
        return exam;
    }

    public void setExam(Integer exam) {
        this.exam = exam;
    }

    @Basic
    @Column(name = "result", nullable = false)
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof ExamRegistration)) return false;
        ExamRegistration examRegistration = (ExamRegistration) o;
        return examRegistration.exam == exam && examRegistration.student == student;
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * exam + student;
        return result;
    }
}

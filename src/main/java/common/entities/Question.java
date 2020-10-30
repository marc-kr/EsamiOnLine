package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Question implements Serializable {
    private Integer id;
    private Integer number;
    private String description;
    private Exam exam;

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "number", nullable = false)
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Basic
    @Column(name = "description", nullable = false, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(! (o instanceof Question)) return false;
        Question q = (Question) o;
        return q.exam.equals(exam) && q.number == number;
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * exam.hashCode() + number;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "exam", referencedColumnName = "id", nullable = false)
    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}

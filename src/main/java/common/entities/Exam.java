package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class Exam implements Serializable {
    public enum State{
        CLOSED, OPENED, STARTED, ENDED;
    }
    private Integer id;
    private String name;
    private Timestamp examDate;
    private Integer duration;
    private String description;
    private Collection<Question> questions;
    private State state;

    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "exam_date", nullable = false)
    public Timestamp getExamDate() {
        return examDate;
    }

    public void setExamDate(Timestamp examDate) {
        this.examDate = examDate;
    }

    @Basic
    @Column(name = "duration", nullable = false)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return exam.id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @OneToMany(mappedBy = "exam")
    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return id + " - " + name + " " + examDate;
    }
}

package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Author Marco De Caria
 * */


@Entity
public class Exam implements Serializable {
    public enum State{
        CLOSED, OPENED, STARTED, ENDED;
    }

    private Integer id;
    private String name;
    private Date examDate;
    private Integer duration;
    private String description;
    private List<Question> questions;
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
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
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

    @OneToMany(mappedBy = "exam", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return id + " - " + name + " " + dateFormat.format(examDate);
    }
}

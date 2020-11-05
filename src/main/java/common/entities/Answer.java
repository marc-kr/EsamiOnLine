package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Answer implements Serializable {
    private Integer id;
    private String description;
    private Boolean isCorrect;
    private Question question;

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", nullable = false, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "is_correct", nullable = false)
    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return answer.id == id && answer.question.equals(question);
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * id + question.hashCode();
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "question", referencedColumnName = "id", nullable = false)
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", isCorrect=" + isCorrect +
                ", question=" + question +
                '}';
    }
}

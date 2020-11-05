package main.java.common.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Question implements Serializable {
    private Integer id;
    private Integer number;
    private String description;
    private Exam exam;
    private List<Answer> answers;

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
        return q.number == number && q.exam.equals(exam);
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 * id + exam.hashCode();
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

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Answer> getAnswers() { return answers; }

    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", number=" + number +
                ", description='" + description + '\'' +
                '}';
    }
}

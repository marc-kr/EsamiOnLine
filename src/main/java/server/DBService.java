package main.java.server;

import main.java.common.entities.AnsweredQuestion;
import main.java.common.entities.Exam;
import main.java.common.entities.ExamRegistration;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Servizio per la gestione della persistenza
 * @Author Marco De Caria
 * */

public class DBService {
    private static DBService dbService;
    private EntityManagerFactory entityManagerFactory;

    private DBService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("online_exams");

    }

    public static synchronized DBService getInstance() {
        if(dbService == null)
            dbService = new DBService();
        return dbService;
    }

    /**
     * Restituisce la lista degli esami disponibili per la prenotazione
     * */
    public List<Exam> getAvailableExams() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("from Exam e where examDate > ?1 and state = ?2 order by examDate");
        query.setParameter(1, new Date());
        query.setParameter(2, Exam.State.CLOSED);
        return query.getResultList();
    }

    /**
     * Registra uno studente per partecipare a un esame
     * */
    public void subscribeStudent(int studentId, int examId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            ExamRegistration registration = new ExamRegistration();
            registration.setExam(examId);
            registration.setStudent(studentId);
            registration.setId(0);
            registration.setResult(-1);
            entityManager.persist(registration);
            entityManager.getTransaction().commit();
        }finally {
            entityManager.close();
        }
    }

    /**
     * Registra il risultato dell'elaborato di uno studente
     * */
    public void registerResult(int examId, int studentId, int result, List<AnsweredQuestion> answeredQuestions) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            ExamRegistration registration = new ExamRegistration();
            for(AnsweredQuestion aq : answeredQuestions)
                entityManager.persist(aq);
            ExamRegistration examRegistration = getExamRegistration(entityManager, studentId, examId);
            examRegistration.setResult(result);
            entityManager.persist(examRegistration);
            entityManager.getTransaction().commit();
        }finally {
            entityManager.close();
        }
    }

    public boolean isStudentSubscribed(int studentId, int examId) {
        return getExamRegistration(entityManagerFactory.createEntityManager(), studentId, examId) != null;
    }

    public Exam getExam(int examId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("from Exam where id = ?1");
        query.setParameter(1, examId);
        return (Exam) query.getSingleResult();
    }

    private ExamRegistration getExamRegistration(EntityManager entityManager, int studentId, int examId) {
        Query q = entityManager.createQuery("from ExamRegistration er where er.exam = ?1 and er.student = ?2");
        q.setParameter(1, examId); q.setParameter(2, studentId);
        ExamRegistration examRegistration = (ExamRegistration) q.getSingleResult();
        return examRegistration;
    }

}

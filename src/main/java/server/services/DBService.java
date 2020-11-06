package main.java.server.services;

import main.java.common.entities.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    public void registerResult(int examId, int studentId, int result, List<Answer> answers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            for(Answer answer : answers) {
                AnsweredQuestion answeredQuestion = new AnsweredQuestion();
                answeredQuestion.setExam(examId); answeredQuestion.setStudent(studentId);
                answeredQuestion.setAnswer(answer);
                System.out.println(answeredQuestion);
                entityManager.persist(answeredQuestion);
            }
            ExamRegistration examRegistration = getExamRegistration(entityManager, studentId, examId);
            examRegistration.setResult(result);
            entityManager.merge(examRegistration);
            entityManager.getTransaction().commit();
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    public void updateExamState(int examId, String newState) {
        System.out.println("Aggiorno esame " + examId + " a " + newState);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query q = entityManager.createQuery("from Exam where id = ?1");
            q.setParameter(1, examId);
            Exam e = (Exam) q.getSingleResult();
            e.setState(Exam.State.valueOf(newState));
            entityManager.merge(e);
            entityManager.getTransaction().commit();

        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            entityManager.close();
        }
    }
    /**
     * Ritorna true se lo studente è registrato per l'esame, false altrimenti
     * */
    public boolean isStudentSubscribed(int studentId, int examId) {
        return getExamRegistration(entityManagerFactory.createEntityManager(), studentId, examId) != null;
    }

    /**
     * Restituisce un esame dal database
     * */
    public Exam getExam(int examId){
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Query query = entityManager.createQuery("from Exam where id = ?1");
            query.setParameter(1, examId);
            return (Exam) query.getSingleResult();
        }catch (NoResultException ex) {
            return null;
        }
    }

    private ExamRegistration getExamRegistration(EntityManager entityManager, int studentId, int examId){
        try {
            Query q = entityManager.createQuery("from ExamRegistration er where er.exam = ?1 and er.student = ?2");
            q.setParameter(1, examId);
            q.setParameter(2, studentId);
            ExamRegistration examRegistration = (ExamRegistration) q.getSingleResult();
            return examRegistration;
        }catch (NoResultException ex) {
            return null;
        }
    }


}

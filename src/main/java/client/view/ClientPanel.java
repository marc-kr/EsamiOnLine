package main.java.client.view;

import main.java.client.ExamClientImpl;
import main.java.common.entities.Exam;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.exceptions.StudentAlreadySubscribedException;
import main.java.common.exceptions.StudentNotSubscribedException;
import main.java.common.interfaces.ExamServer;
import main.java.common.interfaces.ServerIF;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @Author Marco De Caria
 * Interfaccia grafica del client dalla quale lo studente può iscriversi agli esami disponibili
 * e partecipare a quelli in corso.
 * */

public class ClientPanel extends JFrame {
    private JButton btnRefresh;
    private JPanel container;
    private JPanel availableExams;
    private JPanel openedExams;
    private ServerIF server;

    public ClientPanel(ServerIF server) {
        this.server = server;
        setTitle("EOL - Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        showExams();
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void initComponents() {
        btnRefresh = new JButton();
        btnRefresh.addActionListener((ev) -> {
            showExams();
        });
        container = new JPanel();
        JPanel panel3 = new JPanel();
        JLabel label2 = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        availableExams = new JPanel();
        JPanel panel4 = new JPanel();
        JLabel label3 = new JLabel();
        JScrollPane scrollPane2 = new JScrollPane();
        openedExams = new JPanel();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        btnRefresh.setText("Aggiorna");
        contentPane.add(btnRefresh, BorderLayout.SOUTH);
        container.setLayout(new GridLayout(2, 0));
        panel3.setLayout(new BorderLayout());
        label2.setText("Esami disponibili");
        panel3.add(label2, BorderLayout.NORTH);
        availableExams.setLayout(new BoxLayout(availableExams, BoxLayout.Y_AXIS));
        scrollPane1.setViewportView(availableExams);
        panel3.add(scrollPane1, BorderLayout.CENTER);
        container.add(panel3);
        panel4.setLayout(new BorderLayout());
        label3.setText("Esami aperti");
        panel4.add(label3, BorderLayout.NORTH);
        openedExams.setLayout(new BoxLayout(openedExams, BoxLayout.Y_AXIS));
        scrollPane2.setViewportView(openedExams);
        panel4.add(scrollPane2, BorderLayout.CENTER);
        container.add(panel4);
        contentPane.add(container, BorderLayout.CENTER);
        pack();
    }

    private void showExams() {
        try {
            List<Exam> exams = server.getAvailableExams();
            availableExams.removeAll();
            for(Exam e : exams) {
                JPanel panel = new JPanel();
                panel.add(new JLabel(e.toString()));
                JButton button = new JButton("Iscriviti");
                button.addActionListener((ev) -> {
                    System.out.println("Iscrizione esame " + e);
                    subscribeExam(e.getId());
                });
                panel.add(button);
                availableExams.add(panel);
            }
            openedExams.removeAll();
            exams = server.getOpenedExams();
            System.out.println("Esami aperti: " + exams.size());
            for(Exam e : exams) {
                JPanel panel = new JPanel();
                panel.add(new JLabel(e.toString()));
                JButton button = new JButton("Partecipa");
                button.addActionListener((ev) -> {
                    System.out.println("Partecipa all'esame " + e);
                    joinExam(e.getId());
                });
                panel.add(button);
                openedExams.add(panel);
            }
            revalidate();
            repaint();
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getStudentNumber() {
        String input = JOptionPane.showInputDialog(this, "Inserire matricola");
        if(input != null) {
            try {
                int number = Integer.parseInt(input);
                if(number < 1)
                    JOptionPane.showMessageDialog(this, "Matricola non valida", "Errore", JOptionPane.ERROR_MESSAGE);
                return number;
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(this, "Matricola non valida", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }

    private void joinExam(int examId) {
        int number = getStudentNumber();
        if(number > 0) {
            try{
                ExamServer examServer = server.joinExam(number, examId);
                ExamClientImpl examClient = new ExamClientImpl(number, examServer);
                new Thread(() -> {
                    examClient.setWindow(new ExamWindow(examClient));
                }).start();
            }catch(StudentNotSubscribedException ex) {
                JOptionPane.showMessageDialog(this, "Non sei iscritto a questo esame", "Errore", JOptionPane.ERROR_MESSAGE);
            }catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Errore di rete: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }catch (ExamInProgressException ex) {
                JOptionPane.showMessageDialog(this, "L'esame è già iniziato!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void subscribeExam(int examId) {
        int number = getStudentNumber();
        if(number > 0) {
            try {
                if(server.subscribeToExam(number, examId)){
                    JOptionPane.showMessageDialog(this, "Iscrizione effettuata!", "Messaggio", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (StudentAlreadySubscribedException e) {
                JOptionPane.showMessageDialog(this, "Risulti già iscritto!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}

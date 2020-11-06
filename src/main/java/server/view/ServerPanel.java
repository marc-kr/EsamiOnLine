package main.java.server.view;

import main.java.common.entities.Exam;
import main.java.server.ExamManager;
import main.java.server.ServerEngine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @Author Marco De Caria
 * Pannello di controllo dal quale il docente può avviare gli esami programmati per la giornata
 * */

public class ServerPanel extends JFrame {
    private JPanel contentPane;

    public ServerPanel() {
        setVisible(true);
        setTitle("EOL - Server");
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showExams();
    }

    private void showExams() {
        List<Exam> examList = ServerEngine.getInstance().getAvailableExams();
        contentPane.removeAll();
        for(Exam e : examList) {
            JPanel examInfo = new JPanel();
            examInfo.add(new JLabel(e.toString()));
            JButton btnStart = new JButton("Avvia esame");
            btnStart.addActionListener((ev) -> {
                try{
                    ExamManager manager = ServerEngine.getInstance().openExam(e.getId());
                    new Thread(() -> {
                        new ExamPanel(manager);
                    }).start();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            });
            examInfo.add(btnStart);
            contentPane.add(examInfo);
        }
        revalidate();
        repaint();
    }

    private void initComponents() {
        JLabel label1 = new JLabel();
        JButton btnRefresh = new JButton();
        JScrollPane scrollPane1 = new JScrollPane();
        contentPane = new JPanel();
        setTitle("EOL - Server");
        Container contentPane2 = getContentPane();
        contentPane2.setLayout(new BorderLayout());
        label1.setText("Esami programmati per oggi:");
        contentPane2.add(label1, BorderLayout.NORTH);
        btnRefresh.setText("Aggiorna");
        contentPane2.add(btnRefresh, BorderLayout.SOUTH);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        scrollPane1.setViewportView(contentPane);
        contentPane2.add(scrollPane1, BorderLayout.CENTER);
        btnRefresh.addActionListener((ev) -> {
            showExams();
        });
        pack();
        setLocationRelativeTo(getOwner());
    }
}

package main.java.server;

import main.java.common.entities.Exam;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerPanel extends JFrame {
    private JPanel contentPane;
    private ServerEngine serverEngine;
    public ServerPanel(ServerEngine serverEngine) {
        this.serverEngine = serverEngine;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        contentPane = new JPanel();
        showExams();
        setContentPane(contentPane);
    }

    private void showExams() {
        List<Exam> examList = serverEngine.getAvailableExams();
        contentPane.setLayout(new GridLayout(examList.size(), 1));
        for(Exam e : examList) {
            JPanel examInfo = new JPanel(new GridLayout(1, 2));
            examInfo.add(new JLabel(e.toString()));
            JButton btnStart = new JButton("Avvia esame");
            btnStart.addActionListener((ev) -> {
                ExamManager manager = serverEngine.openExam(e.getId());
                new Thread(() -> {
                    new ExamPanel(manager);
                }).start();
                //TODO avvio esame
                System.out.println("Avvio esame " + e);
            });
            examInfo.add(btnStart);
            contentPane.add(examInfo);
        }
    }
}

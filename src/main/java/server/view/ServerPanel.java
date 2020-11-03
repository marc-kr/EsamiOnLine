package main.java.server.view;

import main.java.common.entities.Exam;
import main.java.server.ExamManager;
import main.java.server.ServerEngine;

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
        setTitle("EOL - Server");
        contentPane = new JPanel(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        showExams();
        setContentPane(contentPane);
    }

    private void showExams() {
        List<Exam> examList = serverEngine.getAvailableExams();
        contentPane.setLayout(new GridLayout(examList.size(), 1));
        for(Exam e : examList) {
            JPanel examInfo = new JPanel();
            examInfo.add(new JLabel(e.toString()));
            JButton btnStart = new JButton("Avvia esame");
            btnStart.addActionListener((ev) -> {
                try{
                    ExamManager manager = serverEngine.openExam(e.getId());
                    new Thread(() -> {
                        new ExamPanel(manager);
                    }).start();
                    serverEngine.openExam(e.getId());
                    //TODO avvio esame
                    System.out.println("Avvio esame " + e);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }

            });
            examInfo.add(btnStart);
            contentPane.add(examInfo);
        }
    }
}

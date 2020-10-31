package main.java.server;

import javax.swing.*;

public class ExamPanel extends JFrame {
    private ExamManager manager;

    public ExamPanel(ExamManager manager) {
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setTitle(manager.getExam().getName());
        this.manager = manager;
    }
}

package main.java.client.view;

import main.java.client.ExamClientImpl;
import main.java.common.entities.Answer;
import main.java.common.entities.Question;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

/**
 * @author Marco De Caria
 * Interfaccia grafica per lo svolgimento dell'esame
 */
public class ExamWindow extends JFrame {
    private ExamClientImpl client;
    private Thread timer;
    private JLabel lblTimeLeft;
    private JButton btnSubmit;
    private JTabbedPane tabbedPane;

    public ExamWindow(ExamClientImpl client) {
        this.client = client;
        setExtendedState(MAXIMIZED_BOTH);
        initComponents();
        setTitle("Esame di " + client.getExam().getName());
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel2 = new JPanel();
        JLabel label1 = new JLabel();
        lblTimeLeft = new JLabel();
        btnSubmit = new JButton();
        tabbedPane = new JTabbedPane();
        tabbedPane.getModel().addChangeListener((ev) -> {
            ExamPanel panel = (ExamPanel) tabbedPane.getSelectedComponent();
            System.out.println("EVENT");
            panel.startTimer();
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        label1.setText("Tempo rimanente: ");
        panel2.add(label1);
        panel2.add(lblTimeLeft);
        contentPane.add(panel2, BorderLayout.NORTH);
        btnSubmit.setText("Consegna");
        contentPane.add(btnSubmit, BorderLayout.SOUTH);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void showQuestions() {
        System.out.println("Visualizzo domande");
        List<Question> questions = client.getExam().getQuestions();
        Collections.shuffle(questions);
        int i=1;
        for(Question question : questions) {
            JPanel panel = new ExamPanel(question);
            tabbedPane.add("" + i, panel);
            i++;
        }
    }

    private void startExam() {
        setTitle("Esame di " + client.getExam().getName());
        showQuestions();
        startTimer();
    }

    private void startTimer() {
        timer = new Thread(() -> {
            int timeLeft = client.getExam().getDuration();
            try {
                while(timeLeft > 0) {
                    lblTimeLeft.setText(String.format("%02d:%02d", timeLeft / 60,timeLeft%60));
                    if(timeLeft < 15) lblTimeLeft.setForeground(Color.RED);
                    Thread.sleep(60000);
                    timeLeft--;
                }
            }catch (InterruptedException ex) {

            }
        });
        timer.start();
    }

    public void update(String state) {
        switch (state) {
            case "STARTED":
                startExam();
                break;
            case "ENDED":
                endExam();
        }
    }

    private void endExam() {
        timer.interrupt();
        new ResultWindow(client.getExam(), client.getResult()).setTitle("Risultato esame di " + client.getExam().getName());
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Pannello per la visualizzazione della domanda con le relative risposte
     * */
    private final class ExamPanel extends JPanel {
        private final int GIVEN_TIME = 5;
        private Question question;
        private Thread timer;
        private JTextArea txtQuestion;
        private JPanel answersPanel;
        private JLabel lblTimeLeft;
        private ButtonGroup buttonGroup;

        public ExamPanel(Question question) {
            this.question = question;
            List<Answer> answerList = question.getAnswers();
            Collections.shuffle(answerList);
            question.setAnswers(answerList);
            initComponents();
        }

        private void initComponents() {
            JScrollPane scrollPane1 = new JScrollPane();
            txtQuestion = new JTextArea();
            answersPanel = new JPanel();
            JPanel panel1 = new JPanel();
            JLabel label1 = new JLabel();
            lblTimeLeft = new JLabel("Attendi l'inizio dell'esame");
            buttonGroup = new ButtonGroup();
            txtQuestion.setText(question.getDescription());
            for(Answer answer : question.getAnswers()) {
                JRadioButton button = new JRadioButton(answer.getDescription());
                button.addActionListener((ev) -> {
                    client.setAnswer(question, answer);
                });
                buttonGroup.add(button);
                answersPanel.add(button);
            }
            txtQuestion.setEditable(false);
            scrollPane1.setViewportView(txtQuestion);
            add(scrollPane1, BorderLayout.NORTH);
            answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
            add(answersPanel, BorderLayout.CENTER);
            panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
            label1.setText("Tempo rimanente per la risposta: ");
            panel1.add(label1);
            lblTimeLeft.setText("0");
            panel1.add(lblTimeLeft);
            add(panel1, BorderLayout.SOUTH);
        }

        private void disableButtons() {
            Iterator<AbstractButton> buttonIterator = buttonGroup.getElements().asIterator();
            while(buttonIterator.hasNext()) {
                AbstractButton button = buttonIterator.next();
                button.setEnabled(false);
            }
        }

        private void startTimer() {
            if(timer == null){
                timer = new Thread(() -> {
                    System.out.println("Timer partito");
                    int timeLeft = GIVEN_TIME-1;
                    while(timeLeft >= 0) {
                        try {
                            for(int i=59; i>=0; i--) {
                                lblTimeLeft.setText(String.format("%02d:%02d", timeLeft, i));
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        timeLeft--;
                        if(timeLeft < 2) lblTimeLeft.setForeground(Color.RED);
                    }
                    disableButtons();
                });
                timer.start();
            }
        }


    }
}


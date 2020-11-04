package main.java.client.view;

import main.java.client.ExamClientImpl;
import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.common.interfaces.ExamClient;

import javax.swing.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import javax.swing.*;
/*
 * Created by JFormDesigner on Mon Nov 02 17:46:21 CET 2020
 */



/**
 * @author Marco De Caria
 */
public class ExamWindow extends JFrame {
    private ExamClientImpl client;
    private Thread timer;
    private String examState;


    public ExamWindow(ExamClientImpl client) {
        this.client = client;
        initComponents();
        //setTitle("Esame di " + client.getExam().getName());
        setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        panel2 = new JPanel();
        label1 = new JLabel();
        lblTimeLeft = new JLabel();
        btnSubmit = new JButton();
        tabbedPane = new JTabbedPane();

        tabbedPane.getModel().addChangeListener((ev) -> {
            ExamPanel panel = (ExamPanel) tabbedPane.getSelectedComponent();
            System.out.println("EVENT");
            panel.startTimer();
        });
        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel2 ========
        {
            panel2.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0
                    ,0,0,0), "",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
                    ,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.red),
                    panel2. getBorder()));panel2. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
        ){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}});
            panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

            //---- label1 ----
            label1.setText("Tempo rimanente: ");
            panel2.add(label1);
            panel2.add(lblTimeLeft);
        }
        contentPane.add(panel2, BorderLayout.NORTH);

        //---- btnSubmit ----
        btnSubmit.setText("Consegna");
        contentPane.add(btnSubmit, BorderLayout.SOUTH);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
       // startExam();
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void showQuestions() {
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

    public void update() {
        switch (examState) {
            case "started":
                startExam();
                break;
            case "ended":
                endExam();
        }
        startExam();
    }

    public void setExamState(String newState) {
        examState = newState;
    }

    private void endExam() {
        timer.interrupt();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Marco De Caria
    private JPanel panel2;
    private JLabel label1;
    private JLabel lblTimeLeft;
    private JButton btnSubmit;
    private JTabbedPane tabbedPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private final class ExamPanel extends JPanel {
        private final int GIVEN_TIME = 5;
        private Question question;
        private Thread timer;
        public ExamPanel(Question question) {
            this.question = question;
            List<Answer> answerList = question.getAnswers();
            Collections.shuffle(answerList);
            question.setAnswers(answerList);
            initComponents();
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - Marco De Caria
            scrollPane1 = new JScrollPane();
            txtQuestion = new JTextArea();
            answersPanel = new JPanel();
            panel1 = new JPanel();
            label1 = new JLabel();
            lblTimeLeft = new JLabel();
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

            //======== this ========
            setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
                    ( 0, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border
                    . TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
                    . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
            propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( )
                    ; }} );
            setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //---- txtQuestion ----
                txtQuestion.setEditable(false);
                scrollPane1.setViewportView(txtQuestion);
            }
            add(scrollPane1, BorderLayout.NORTH);

            //======== answersPanel ========
            {
                answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
            }
            add(answersPanel, BorderLayout.CENTER);

            //======== panel1 ========
            {
                panel1.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- label1 ----
                label1.setText("Tempo rimanente per la risposta: ");
                panel1.add(label1);

                //---- lblTimeLeft ----
                lblTimeLeft.setText("0");
                panel1.add(lblTimeLeft);
            }
            add(panel1, BorderLayout.SOUTH);
            // JFormDesigner - End of component initialization  //GEN-END:initComponents

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

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        private JScrollPane scrollPane1;
        private JTextArea txtQuestion;
        private JPanel answersPanel;
        private JPanel panel1;
        private JLabel label1;
        private JLabel lblTimeLeft;
        private ButtonGroup buttonGroup;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }

}


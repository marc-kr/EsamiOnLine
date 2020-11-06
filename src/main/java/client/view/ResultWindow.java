package main.java.client.view;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @Author Marco De Caria
 * Interfaccia grafica per la visualizzazione del risultato dell'esame. La finestra mostra il
 * punteggio totalizzato e la soluzione dell'esame.
 * */
public class ResultWindow extends JFrame {
    private Exam exam;
    private Map<Question, Answer> answers;
    private JLabel lblResult;
    private JPanel solutionPanel;

    public ResultWindow(Exam exam, Map<Question, Answer> answers) {
        this.exam = exam;
        this.answers = answers;
        setVisible(true);
        initComponents();
        showSolution();
        setExtendedState(MAXIMIZED_BOTH);
    }

    private void initComponents() {
        JPanel panel1 = new JPanel();
        JLabel label1 = new JLabel();
        lblResult = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        solutionPanel = new JPanel();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        panel1.setLayout(new FlowLayout());
        label1.setText("Punteggio ottenuto: ");
        lblResult.setText("0");
        panel1.add(label1);
        panel1.add(lblResult);
        contentPane.add(panel1, BorderLayout.NORTH);
        solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));
        scrollPane1.setViewportView(solutionPanel);
        contentPane.add(scrollPane1, BorderLayout.CENTER);
        pack();
    }

    private void showSolution() {
        int result=0;
        for(Question q : exam.getQuestions()) {
            if(answers.get(q) == null) result -= 1;
            else if (answers.get(q).getCorrect()) result += 3;
            SolutionPanel panel = new SolutionPanel(q, answers.get(q));
            solutionPanel.add(panel);
        }
        if(result<0) result = 0;
        lblResult.setText("" + result);
        if(result < 18) lblResult.setForeground(Color.RED);
        else lblResult.setForeground(Color.GREEN);
    }

    public class SolutionPanel extends JPanel {
        public SolutionPanel(Question q, Answer a) {
            initComponents(q, a);
        }

        private void initComponents(Question q, Answer a) {
            JScrollPane scrollPane1 = new JScrollPane();
            JPanel panel1 = new JPanel();
            JScrollPane scrollPane2 = new JScrollPane();
            JTextArea txtQuestion = new JTextArea();
            JPanel answersPanel = new JPanel();
            txtQuestion.setEditable(false);
            txtQuestion.setText(q.getDescription());
            for(Answer answer : q.getAnswers()) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setEnabled(false);
                checkBox.setText(answer.getDescription());
                if(answer.getCorrect()) checkBox.setBackground(Color.GREEN);
                if(a != null && a.equals(answer)) checkBox.setSelected(true);
                answersPanel.add(checkBox);
            }
            setLayout(new BorderLayout());
            panel1.setLayout(new BorderLayout());
            scrollPane2.setViewportView(txtQuestion);
            panel1.add(scrollPane2, BorderLayout.NORTH);
            answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
            panel1.add(answersPanel, BorderLayout.CENTER);
            scrollPane1.setViewportView(panel1);
            add(scrollPane1, BorderLayout.CENTER);
        }
    }
}

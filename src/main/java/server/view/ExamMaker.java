package main.java.server.view;
import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;
import main.java.server.services.DBService;

import java.awt.*;
import javax.swing.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Marco De Caria
 */
public class ExamMaker extends JFrame {
    private JButton btnAddQuestion;
    private JButton btnSave;
    private JPanel panelInfos;
    private JLabel lblName;
    private JTextField txtExamName;
    private JLabel lblDuration;
    private JTextField txtDuration;
    private JLabel lblDate;
    private JTextField txtDate;
    private JLabel lblDescription;
    private JTextArea txtDescription;
    private JLabel lblQuestions;
    private JPanel questionsPanel;
    private List<QuestionPanel> questionList;

    public ExamMaker() {
        initComponents();
        questionList = new LinkedList<>();
    }

    private void initComponents() {
        JPanel panel5 = new JPanel();
        btnAddQuestion = new JButton();
        btnSave = new JButton();
        JPanel panel2 = new JPanel();
        panelInfos = new JPanel();
        lblName = new JLabel();
        txtExamName = new JTextField();
        lblDuration = new JLabel();
        txtDuration = new JTextField();
        lblDate = new JLabel();
        txtDate = new JTextField();
        JPanel panel3 = new JPanel();
        lblDescription = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        txtDescription = new JTextArea();
        JPanel panel4 = new JPanel();
        lblQuestions = new JLabel();
        JScrollPane scrollPane2 = new JScrollPane();
        questionsPanel = new JPanel();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        panel5.setLayout(new GridLayout(1, 1));
        btnAddQuestion.setText("Aggiungi domanda");
        panel5.add(btnAddQuestion);
        btnSave.setText("Salva");
        panel5.add(btnSave);
        contentPane.add(panel5, BorderLayout.SOUTH);
        panel2.setLayout(new BorderLayout());
        panelInfos.setLayout(new FlowLayout(FlowLayout.LEFT));
        lblName.setText("Nome: ");
        panelInfos.add(lblName);
        txtExamName.setColumns(50);
        panelInfos.add(txtExamName);
        lblDuration.setText("Durata(min.): ");
        panelInfos.add(lblDuration);
        txtDuration.setColumns(4);
        panelInfos.add(txtDuration);
        lblDate.setText("Data: ");
        panelInfos.add(lblDate);
        txtDate.setColumns(12);
        txtDate.setToolTipText("gg:mm:aaaa");
        txtDate.setText("gg:mm:aaaa");
        panelInfos.add(txtDate);
        panel2.add(panelInfos, BorderLayout.NORTH);
        panel3.setLayout(new BorderLayout());
        lblDescription.setText("Descrizione:");
        panel3.add(lblDescription, BorderLayout.NORTH);
        txtDescription.setRows(2);
        scrollPane1.setViewportView(txtDescription);
        panel3.add(scrollPane1, BorderLayout.CENTER);
        panel2.add(panel3, BorderLayout.CENTER);contentPane.add(panel2, BorderLayout.NORTH);
        panel4.setLayout(new BorderLayout());
        lblQuestions.setText("Domande:");
        panel4.add(lblQuestions, BorderLayout.PAGE_START);
        //questionsPanel.setLayout(new FlowLayout());
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        scrollPane2.setViewportView(questionsPanel);
        panel4.add(scrollPane2, BorderLayout.CENTER);
        contentPane.add(panel4, BorderLayout.CENTER);
        setListeners();
        setVisible(true);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void setListeners() {
        btnAddQuestion.addActionListener((ev) -> {
            QuestionPanel questionPanel = new QuestionPanel();
            questionsPanel.add(questionPanel);
            questionList.add(questionPanel);
            validate();
            repaint();
        });
        btnSave.addActionListener((ev) -> {
            Exam e = getExam();
            if(e != null) {
                DBService.getInstance().createExam(e);
            }
        });
    }

    private Exam getExam() {
        String name = txtExamName.getText();
        if(name.isEmpty()) JOptionPane.showMessageDialog(this, "Inserire un nome per l'esame", "Attenzione", JOptionPane.WARNING_MESSAGE);
        String description = txtDescription.getText();
        String strDate = txtDate.getText();
        if(strDate.isEmpty() || !strDate.matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}")) JOptionPane.showMessageDialog(this, "Inserire una data valida gg/mm/aaaa", "Attenzione", JOptionPane.WARNING_MESSAGE);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Data non valida!", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
        if(txtDuration.getText().isEmpty()) JOptionPane.showMessageDialog(this, "Inserire una durata", "Attenzione", JOptionPane.WARNING_MESSAGE);
        int duration=0;
        try {
            duration = Integer.parseInt(txtDuration.getText());
        }catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Durata non valida", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
        List<Question> questions = new LinkedList<>();
        Exam e = new Exam();
        for(QuestionPanel questionPanel : questionList) {
            Question q = questionPanel.getQuestion();
            q.setExam(e);
            questions.add(q);
            System.out.println(q);
        }

        e.setName(name); e.setDescription(description); e.setExamDate(date); e.setDuration(duration);
        e.setQuestions(questions);
        return e;
    }

    private class QuestionPanel extends JPanel {
        private JTextArea txtQuestion;
        private JPanel answersPanel;
        private JButton btnAdd;
        private JButton btnRemove;
        private List<AnswerPanel> answers;
        private ButtonGroup buttonGroup;
        public QuestionPanel() {
            answers = new LinkedList<>();
            initComponents();
        }

        private void initComponents() {
            JScrollPane scrollPane2 = new JScrollPane();
            JPanel panel4 = new JPanel();
            JPanel panel1 = new JPanel();
            JLabel label1 = new JLabel();
            buttonGroup = new ButtonGroup();
            JScrollPane scrollPane1 = new JScrollPane();
            txtQuestion = new JTextArea();
            JPanel panel2 = new JPanel();
            JLabel label2 = new JLabel();
            JScrollPane scrollPane3 = new JScrollPane();
            answersPanel = new JPanel();
            JPanel panel3 = new JPanel();
            btnAdd = new JButton();
            btnRemove = new JButton();
            setLayout(new BorderLayout());
            panel4.setLayout(new BorderLayout());
            panel1.setLayout(new BorderLayout());
            label1.setText("Domanda");
            panel1.add(label1, BorderLayout.NORTH);
            txtQuestion.setRows(2);
            scrollPane1.setViewportView(txtQuestion);
            panel1.add(scrollPane1, BorderLayout.CENTER);
            panel4.add(panel1, BorderLayout.NORTH);
            panel2.setLayout(new BorderLayout());
            label2.setText("Risposte");
            panel2.add(label2, BorderLayout.PAGE_START);
            answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
            scrollPane3.setViewportView(answersPanel);
            panel2.add(scrollPane3, BorderLayout.CENTER);
            panel3.setLayout(new GridLayout(1, 2));
            btnAdd.setText("Aggiungi");
            panel3.add(btnAdd);
            btnRemove.setText("Rimuovi ultima");
            panel3.add(btnRemove);
            panel2.add(panel3, BorderLayout.PAGE_END);
            panel4.add(panel2, BorderLayout.CENTER);
            scrollPane2.setViewportView(panel4);
            add(scrollPane2, BorderLayout.CENTER);
            setListeners();
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        private void setListeners() {
            btnAdd.addActionListener(e -> {
                AnswerPanel answerPanel = new AnswerPanel();
                answers.add(answerPanel);
                buttonGroup.add(answerPanel.radioButton);
                answersPanel.add(answerPanel);
                this.revalidate();
                this.repaint();
            });
            btnRemove.addActionListener(e -> {
                AnswerPanel answerPanel = answers.get(answers.size()-1);
                answersPanel.remove(answerPanel);
                buttonGroup.remove(answerPanel.radioButton);
                answers.remove(answers.size()-1);
                this.revalidate();
                this.repaint();
            });
        }

        public Question getQuestion() {
            List<Answer> answerList = new LinkedList<>();
            Question q = new Question();
            for(AnswerPanel panel : answers) {
                Answer a = panel.getAnswer();
                a.setQuestion(q);
                answerList.add(a);
                System.out.println(a);
            }
            q.setId(0);
            q.setDescription(txtDescription.getText());
            q.setAnswers(answerList);
            return q;
        }
    }

    private class AnswerPanel extends JPanel{
        private JRadioButton radioButton;
        private JTextField txtAnswer;

        public AnswerPanel() {
            radioButton = new JRadioButton();
            txtAnswer = new JTextField();
            txtAnswer.setColumns(40);
            this.add(radioButton);
            this.add(txtAnswer);
        }

        public Answer getAnswer() {
            if(txtAnswer.getText().isEmpty()) return null;
            Answer a = new Answer();
            a.setDescription(txtAnswer.getText());
            a.setCorrect(radioButton.isSelected());
            a.setId(0);
            return a;
        }
    }
}

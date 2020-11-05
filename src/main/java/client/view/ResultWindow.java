package main.java.client.view;

import main.java.common.entities.Answer;
import main.java.common.entities.Exam;
import main.java.common.entities.Question;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ResultWindow extends JFrame {
    private Exam exam;
    private Map<Question, Answer> answers;

    public ResultWindow(Exam exam, Map<Question, Answer> answers) {
        this.exam = exam;
        this.answers = answers;
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        initComponents();
        showSolution();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        panel1 = new JPanel();
        label1 = new JLabel();
        lblResult = new JLabel();
        scrollPane1 = new JScrollPane();
        solutionPanel = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
                    0, 0 ,0 , 0) ,  "" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
                    . BOTTOM, new java. awt .Font ( "Dialo\u0067", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
                    red ) ,panel1. getBorder () ) ); panel1. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
                                                                                                                                                                                   beans. PropertyChangeEvent e) { if( "borde\u0072" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
            panel1.setLayout(new FlowLayout());

            //---- label1 ----
            label1.setText("Punteggio ottenuto: ");
            panel1.add(label1);

            //---- lblResult ----
            lblResult.setText("0");
            panel1.add(lblResult);
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== scrollPane1 ========
        {

            //======== solutionPanel ========
            {
                solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));
            }
            scrollPane1.setViewportView(solutionPanel);
        }
        contentPane.add(scrollPane1, BorderLayout.CENTER);
        pack();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Marco De Caria
    private JPanel panel1;
    private JLabel label1;
    private JLabel lblResult;
    private JScrollPane scrollPane1;
    private JPanel solutionPanel;


    private void showSolution() {
        int result=0;
        for(Question q : answers.keySet()) {
            if(answers.get(q) == null) result -= 1;
            else if(answers.get(q).getCorrect()) result += 3;
            SolutionPanel panel = new SolutionPanel(q, answers.get(q));
            solutionPanel.add(panel);
        }
        if(result<0) result = 0;
        lblResult.setText("" + result);
        if(result < 18) lblResult.setForeground(Color.RED);
    }

    public class SolutionPanel extends JPanel {
        public SolutionPanel(Question q, Answer a) {
            initComponents(q, a);
        }

        private void initComponents(Question q, Answer a) {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - Marco De Caria
            scrollPane1 = new JScrollPane();
            panel1 = new JPanel();
            scrollPane2 = new JScrollPane();
            txtQuestion = new JTextArea();
            answersPanel = new JPanel();
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
            //======== this ========
            setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax
                    . swing. border. EmptyBorder( 0, 0, 0, 0) , "", javax. swing
                    . border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .
                    Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red
            ) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override
            public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName (
            ) )) throw new RuntimeException( ); }} );
            setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //======== panel1 ========
                {
                    panel1.setLayout(new BorderLayout());

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setViewportView(txtQuestion);
                    }
                    panel1.add(scrollPane2, BorderLayout.NORTH);

                    //======== panel2 ========
                    {
                        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
                    }
                    panel1.add(answersPanel, BorderLayout.CENTER);
                }
                scrollPane1.setViewportView(panel1);
            }
            add(scrollPane1, BorderLayout.CENTER);
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        private JScrollPane scrollPane1;
        private JPanel panel1;
        private JScrollPane scrollPane2;
        private JTextArea txtQuestion;
        private JPanel answersPanel;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}

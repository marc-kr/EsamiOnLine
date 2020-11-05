package main.java.server.view;

import main.java.server.ExamManager;

import javax.swing.*;
import java.awt.*;

public class ExamPanel extends JFrame {
    private ExamManager manager;
    private int timeLeft;
    private Thread timer;
    private int studentCount=0;
    public ExamPanel(ExamManager manager) {
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setTitle(manager.getExam().getName());
        initComponents();
        this.manager = manager;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        scrollPane1 = new JScrollPane();
        studentsPanel = new JPanel();
        panel1 = new JPanel();
        label1 = new JLabel();
        lblStudentsNumber = new JLabel();
        scrollPane2 = new JScrollPane();
        studentListPanel = new JPanel();
        panel2 = new JPanel();
        btnStart = new JButton();
        btnEnd = new JButton();
        panel3 = new JPanel();
        label2 = new JLabel();
        lblTimeLeft = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== scrollPane1 ========
        {

            //======== studentsPanel ========
            {
                studentsPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder
                        (0,0,0,0), "",javax.swing.border.TitledBorder.CENTER,javax.swing.border
                        .TitledBorder.BOTTOM,new java.awt.Font("D\u0069al\u006fg",java.awt.Font.BOLD,12),java.awt
                        .Color.red),studentsPanel. getBorder()));studentsPanel. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void
            propertyChange(java.beans.PropertyChangeEvent e){if("\u0062or\u0064er".equals(e.getPropertyName()))throw new RuntimeException()
                    ;}});
                studentsPanel.setLayout(new BorderLayout());

                //======== panel1 ========
                {
                    panel1.setLayout(new FlowLayout(FlowLayout.LEFT));

                    //---- label1 ----
                    label1.setText("Partecipanti: ");
                    panel1.add(label1);

                    //---- lblStudentsNumber ----
                    lblStudentsNumber.setText("0");
                    panel1.add(lblStudentsNumber);
                }
                studentsPanel.add(panel1, BorderLayout.NORTH);

                //======== scrollPane2 ========
                {

                    //======== studentListPanel ========
                    {
                        studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));
                    }
                    scrollPane2.setViewportView(studentListPanel);
                }
                studentsPanel.add(scrollPane2, BorderLayout.CENTER);
            }
            scrollPane1.setViewportView(studentsPanel);
        }
        contentPane.add(scrollPane1, BorderLayout.WEST);

        //======== panel2 ========
        {
            panel2.setLayout(new GridLayout(1, 2));

            //---- btnStart ----
            btnStart.setText("Avvia");
            btnStart.addActionListener((ev) -> {

                startExam();
            });
            panel2.add(btnStart);


            //---- btnEnd ----
            btnEnd.setText("Termina");
            btnEnd.addActionListener((ev) -> {
                if(JOptionPane.showConfirmDialog(this, "Sicuro di voler terminare l'esame?",
                        "Conferma", JOptionPane.OK_CANCEL_OPTION) == 0)
                    stopExam();
            });
            btnEnd.setEnabled(false);
            panel2.add(btnEnd);
        }
        contentPane.add(panel2, BorderLayout.SOUTH);

        //======== panel3 ========
        {
            panel3.setLayout(new FlowLayout());

            //---- label2 ----
            label2.setText("Tempo mancante: ");
            panel3.add(label2);

            //---- lblTimeLeft ----
            lblTimeLeft.setText("Esame non iniziato");
            panel3.add(lblTimeLeft);
        }
        contentPane.add(panel3, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void startExam() {
        manager.start();
        btnStart.setEnabled(false);
        btnEnd.setEnabled(true);
        startTimer();
    }
    private void stopExam() {
        manager.end();
        timer.interrupt();
        btnEnd.setEnabled(false);
        lblTimeLeft.setText("Esame concluso");
        lblTimeLeft.setForeground(Color.RED);
    }

    private void startTimer() {
        int duration = manager.getExam().getDuration();
        timeLeft = duration;
        timer = new Thread(() -> {
            while(timeLeft > 0) {
                System.out.println(timeLeft);
                lblTimeLeft.setText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));
                lblTimeLeft.repaint();
                timeLeft--;
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timer.start();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Marco De Caria
    private JScrollPane scrollPane1;
    private JPanel studentsPanel;
    private JPanel panel1;
    private JLabel label1;
    private JLabel lblStudentsNumber;
    private JScrollPane scrollPane2;
    private JPanel studentListPanel;
    private JPanel panel2;
    private JButton btnStart;
    private JButton btnEnd;
    private JPanel panel3;
    private JLabel label2;
    private JLabel lblTimeLeft;
}

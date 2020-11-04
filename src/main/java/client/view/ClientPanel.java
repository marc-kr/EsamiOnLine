package main.java.client.view;

import main.java.client.ExamClientImpl;
import main.java.common.entities.Exam;
import main.java.common.exceptions.ExamInProgressException;
import main.java.common.exceptions.StudentNotSubscribedException;
import main.java.common.interfaces.ExamClient;
import main.java.common.interfaces.ExamServer;
import main.java.common.interfaces.ServerIF;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class ClientPanel extends JFrame {
    ServerIF server;

    public ClientPanel(ServerIF server) {
        this.server = server;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setVisible(true);
        initComponents();
        showExams();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Marco De Caria
        btnRefresh = new JButton();
        container = new JPanel();
        panel3 = new JPanel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        availableExams = new JPanel();
        panel4 = new JPanel();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        openedExams = new JPanel();

        //======== this ========
        setTitle("EOL - Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- btnRefresh ----
        btnRefresh.setText("Aggiorna");
        contentPane.add(btnRefresh, BorderLayout.SOUTH);

        //======== container ========
        {
            container.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder( 0
                    , 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
                    , new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,
                    container. getBorder( )) ); container. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            container.setLayout(new GridLayout(2, 0));

            //======== panel3 ========
            {
                panel3.setLayout(new BorderLayout());

                //---- label2 ----
                label2.setText("Esami disponibili");
                panel3.add(label2, BorderLayout.NORTH);

                //======== scrollPane1 ========
                {

                    //======== availableExams ========
                    {

                        availableExams.setLayout(new BoxLayout(availableExams, BoxLayout.Y_AXIS));
                    }
                    scrollPane1.setViewportView(availableExams);
                }
                panel3.add(scrollPane1, BorderLayout.CENTER);
            }
            container.add(panel3);

            //======== panel4 ========
            {
                panel4.setLayout(new BorderLayout());

                //---- label3 ----
                label3.setText("Esami aperti");
                panel4.add(label3, BorderLayout.NORTH);

                //======== scrollPane2 ========
                {

                    //======== openedExams ========
                    {
                        openedExams.setLayout(new BoxLayout(openedExams, BoxLayout.Y_AXIS));
                    }
                    scrollPane2.setViewportView(openedExams);
                }
                panel4.add(scrollPane2, BorderLayout.CENTER);
            }
            container.add(panel4);
        }
        btnRefresh.addActionListener((ev) -> {
            showExams();
        });
        contentPane.add(container, BorderLayout.CENTER);
        pack();
       // setLocationRelativeTo(getOwner());
    }

    private void showExams() {
        try {
            List<Exam> exams = server.getAvailableExams();
            availableExams.removeAll();
            for(Exam e : exams) {
                JPanel panel = new JPanel();
                panel.add(new JLabel(e.toString()));
                JButton button = new JButton("Iscriviti");
                button.addActionListener((ev) -> {
                    System.out.println("Iscrizione esame " + e);
                    subscribeExam(e.getId());
                });
                panel.add(button);
                availableExams.add(panel);
            }
            openedExams.removeAll();
            exams = server.getOpenedExams();
            System.out.println("Esami aperti: " + exams.size());
            for(Exam e : exams) {
                JPanel panel = new JPanel();
                panel.add(new JLabel(e.toString()));
                JButton button = new JButton("Partecipa");
                button.addActionListener((ev) -> {
                    System.out.println("Partecipa all'esame " + e);
                    joinExam(e.getId());
                });
                panel.add(button);
                openedExams.add(panel);
            }
            repaint();
            pack();
        }catch(Exception ex) {

        }
    }

    private int getStudentNumber() {
        String input = JOptionPane.showInputDialog(this, "Inserire matricola");
        if(input != null) {
            try {
                int number = Integer.parseInt(input);
                if(number < 1)
                    JOptionPane.showMessageDialog(this, "Matricola non valida", "Errore", JOptionPane.ERROR_MESSAGE);
                return number;
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(this, "Matricola non valida", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }

    private void joinExam(int examId) {
        int number = getStudentNumber();
        if(number > 0) {
            try{
                ExamServer examServer = server.joinExam(number, examId);
                ExamClientImpl examClient = new ExamClientImpl(number, examServer);
                new Thread(() -> {
                    examClient.setWindow(new ExamWindow(examClient));
                }).start();
            }catch(StudentNotSubscribedException ex) {
                JOptionPane.showMessageDialog(this, "Non sei iscritto a questo esame", "Errore", JOptionPane.ERROR_MESSAGE);
            }catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Errore di rete: \" + ex.getMessage(), JOptionPane.ERROR_MESSAGE", "Errore", JOptionPane.ERROR_MESSAGE);
            }catch (ExamInProgressException ex) {
                JOptionPane.showMessageDialog(this, "L'esame è già iniziato!", "Errore", JOptionPane.ERROR_MESSAGE);
            }

            /*try {
                ExamServer examServer = null;
                try {
                    examServer = server.joinExam(number, examId);
                } catch (StudentNotSubscribedException e) {
                    e.printStackTrace();
                }
                if(examServer != null) {
                    ExamClientImpl examClient = new ExamClientImpl(number, examServer);
                    new Thread(()-> {
                        examClient.setWindow( new ExamWindow(examClient));
                    }).start();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void subscribeExam(int examId) {
        int number = getStudentNumber();
        if(number > 0) {
            //TODO iscrivi all'esame
        }
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Marco De Caria
    private JButton btnRefresh;
    private JPanel container;
    private JPanel panel3;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JPanel availableExams;
    private JPanel panel4;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JPanel openedExams;
}

package main.java.server;

import main.java.client.ExamClientImpl;
import main.java.client.view.ExamWindow;
import main.java.common.entities.Exam;
import main.java.server.services.DBService;
import main.java.server.view.ExamPanel;

public class ExamTest {
    public static void main(String... args) {
        try {
            Exam e = DBService.getInstance().getExam(1);
            ExamManager manager = new ExamManager(e);
            ExamServer server = new ExamServer(manager);
            ExamClientImpl examClient = new ExamClientImpl(189621, server);
            ExamClientImpl examClient1 = new ExamClientImpl(189622, server);
            new Thread(()->{
               new ExamPanel(manager);
            }).start();
            new Thread(()->{
                examClient.setWindow(new ExamWindow(examClient));
            }).start();
            new Thread(()->{
                examClient1.setWindow(new ExamWindow(examClient1));
            }).start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

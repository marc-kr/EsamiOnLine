package main.java.server;

import main.java.client.ExamClientImpl;
import main.java.common.entities.Exam;
import main.java.server.services.DBService;

public class ExamTest {
    public static void main(String... args) {
        try {
            Exam e = DBService.getInstance().getExam(1);
            ExamManager manager = new ExamManager(e);
            ExamServer server = new ExamServer(manager);
            ExamClientImpl examClient = new ExamClientImpl(189621, server);
            ExamClientImpl examClient1 = new ExamClientImpl(189622, server);
            manager.start();
            Thread.sleep(3000);
            manager.end();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

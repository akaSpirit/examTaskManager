package exam;

import exam.manager.TaskManagerServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            new TaskManagerServer("localhost", 9889).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

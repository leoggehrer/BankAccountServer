package at.htlleonding;

import socket.Server;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Application.getInsatnce().setRunning(true);
        Server server = new Server(2222);
        Thread serverThread = new Thread(() ->
        {
            try {
                server.listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

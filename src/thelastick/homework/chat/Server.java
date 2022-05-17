package thelastick.homework.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        // Здесь лежат все сообщения. Сами сообщения уже имеют автора.
        List<String> messages = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
            System.out.println("Server ready");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");
                Thread thread = new ServerThread(socket, messages);
                thread.start();
            }
        } catch (IOException ex) {
            System.out.println("Connection lost from Server");
        }
    }
}



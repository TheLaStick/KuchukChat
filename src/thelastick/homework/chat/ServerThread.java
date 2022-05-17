package thelastick.homework.chat;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ServerThread extends Thread {
    final Socket clientSocket;
    final List<String> messages;

    ServerThread(Socket socket, List<String> messages) {
        this.clientSocket = socket;
        this.messages = messages;
    }

    public void run() {
        try {
            sendMessages();
            String messageValue = "";

            while (!messageValue.equals(Constants.EXIT_COMMAND)) {
                Optional<String> message = getMessage();
                if (message.isPresent()) {
                    messageValue = message.get();
                    System.out.println("Message received");
                    messages.add(messageValue);
                }
                sendMessages();
            }

            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Connection lost from ServerThread");
        }
    }

    public void sendMessages() {
        try {
            System.out.println("Sending messages");
            PrintStream serverOutput = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            if (messages.size() == 0) {
                serverOutput.println("Empty chat\n");
                return;
            }

            int length = Math.min(Constants.PREVIOUS_MESSAGES_COUNT, messages.size());
            for (int i = 0; i < length; ++i) {
                serverOutput.println(messages.get(i));
            }
        } catch (IOException e) {
            System.out.println("Connection lost in sending messages from Server");
        }
    }

    public Optional<String> getMessage() {
        try {
            System.out.println("Receiving messages");
            InputStream is = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String receivedMessage = br.readLine();

            return Optional.of(receivedMessage);
        } catch (IOException e) {
            System.out.println("Connection lost in reading message to Server");
        }

        return Optional.empty();
    }
}

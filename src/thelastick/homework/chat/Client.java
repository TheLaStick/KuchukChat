package thelastick.homework.chat;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            String name = getClientName();
            Socket clientSocket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            System.out.println("connected");
            String message = "";
            while (!message.equals(Constants.EXIT_COMMAND)) {
                showMessages(clientSocket);
                message = sendMessage(clientSocket, name);
            }

            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Connection lost from Client");
        }
    }

    public static String getClientName() {
        System.out.println("Please enter your name");
        String name = Constants.SCANNER.nextLine();
        while (name.equals("")) {
            System.out.println("You cannot go any further with empty name");
            System.out.println("Please enter your name");
            name = Constants.SCANNER.nextLine();
        }

        return name.trim();
    }

    public static void showMessages(Socket clientSocket) throws IOException {
        System.out.println("receiving data");
        InputStream is = clientSocket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String receivedData = br.readLine();
        if (!receivedData.equals("Empty chat")) {
            StringBuilder builder = new StringBuilder();
            while (receivedData != null) {
                builder.append(receivedData).append('\n');
                receivedData = br.readLine();
            }

            receivedData = builder.toString();
        }
        System.out.println(Constants.CHAT_SEPARATOR);
        System.out.println(receivedData);
        System.out.println(Constants.CHAT_SEPARATOR);
    }

    public static String sendMessage(Socket clientSocket, String name) throws IOException {
        System.out.print("Your message: ");
        String message = name + ": " + Constants.SCANNER.nextLine();
        PrintStream ps = new PrintStream(clientSocket.getOutputStream());
        ps.println(message);
        return message;
    }
}

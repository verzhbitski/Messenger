package ru.verzhbitski_vladislav.server;

import ru.verzhbitski_vladislav.transfer.Message;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private String username;
    private Socket clientSocket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    public ClientHandler(Socket clientSocket, ObjectInputStream inputStream, ObjectOutputStream outputStream, String username) throws IOException {
        this.clientSocket = clientSocket;
        this.input = inputStream;
        this.output = outputStream;
        this.username = username;
        for (ClientHandler client: Server.clients.values()) {
            if (!client.equals(this)) {
                Message message = new Message(
                        Message.MessageType.USER_CONNECTED,
                        username,
                        null,
                        null
                );
                client.output.writeObject(message);
                message = new Message(
                        Message.MessageType.USER_CONNECTED,
                        client.username,
                        null,
                        null
                );
                output.writeObject(message);
            }
        }
    }

    @Override
    public void run() {
        Message message;
        try {
            while (true) {
                message = (Message) input.readObject();
                String receiverName = message.getReceiverName();
                ClientHandler client = Server.clients.get(receiverName);
                client.output.writeObject(message);
            }
        } catch (IOException e) {
            Server.clients.values().remove(this);
            for (ClientHandler client: Server.clients.values()) {
                Message disconnected = new Message(
                        Message.MessageType.USER_DISCONNECTED,
                        username,
                        null,
                        null
                );
                try {
                    client.output.writeObject(disconnected);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

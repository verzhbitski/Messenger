package ru.verzhbitski_vladislav.client;

import ru.verzhbitski_vladislav.transfer.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final static Client instance = new Client();

    private String username;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public static Client getInstance() {
        return instance;
    }

    private Client() {}

    public void connect(String host, int port, String username) throws Exception {
        this.username = username;
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(username);
        inputStream = new ObjectInputStream(socket.getInputStream());
        String response = (String) inputStream.readObject();
        if (response.equals("Error")) {
            throw new Exception();
        }
    }

    public void sendMessage(Message message) throws IOException {
        outputStream.writeObject(message);
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public String getUsername() {
        return username;
    }
}

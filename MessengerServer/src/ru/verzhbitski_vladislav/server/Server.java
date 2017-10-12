package ru.verzhbitski_vladislav.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class Server {
    static Map<String, ClientHandler> clients;

    public Server(int port) {
        clients = new Hashtable<>();
        try (
            ServerSocket server = new ServerSocket(port)
        ) {
                while (true) {
                    try {
                        Socket clientSocket = server.accept();
                        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                        String username = (String) inputStream.readObject();
                        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                        if (clients.keySet().contains(username)) {
                            outputStream.writeObject("Error");
                            continue;
                        } else {
                            outputStream.writeObject("Ok");
                        }
                        ClientHandler client = new ClientHandler(clientSocket, inputStream, outputStream, username);
                        clients.put(username, client);
                        client.start();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

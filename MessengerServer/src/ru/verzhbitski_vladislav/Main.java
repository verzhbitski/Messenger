package ru.verzhbitski_vladislav;

import ru.verzhbitski_vladislav.server.Server;

public class Main {

    public static void main(String[] args) {
        int port;
        if (args.length != 1) {
            System.err.println("Invalid parameters");
        } else {
            port = Integer.parseInt(args[0]);
            if (port >= 0 || port <= 65535) {
                Server server = new Server(port);
            } else {
                System.err.println("Invalid port");
            }
        }
    }
}

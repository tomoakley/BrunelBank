package com.tom;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Tom on 06/12/2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        int serverPort = 4444;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server started on http://localhost:" + serverPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(-1);
        }

        new Database();
        System.out.println("SQLite3 database connected");

        while (true) {
            new BrunelBank(serverSocket.accept()).start();
        }

    }

}

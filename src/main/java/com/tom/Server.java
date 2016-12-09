package com.tom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Created by Tom on 06/12/2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        /* InetAddress computerAddress = null;
        try {
            computerAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println(e);
        } */

        ServerSocket serverSocket = null;
        boolean listening = true;
        int serverPort = 4444;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server started");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(-1);
        }

        while (listening) {
            new BrunelBank(serverSocket.accept()).start();
        }
        serverSocket.close();
    }

}

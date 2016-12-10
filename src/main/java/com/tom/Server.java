package com.tom;

import com.tom.utils.json;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Tom on 06/12/2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        boolean listening = true;
        int serverPort = 4444;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server started on http://localhost:" + serverPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(-1);
        }

        List<Account> accountsList = json.getAccountsJson();

        while (listening) {
            new BrunelBank(accountsList, serverSocket.accept()).start();
        }
        serverSocket.close();
    }

}

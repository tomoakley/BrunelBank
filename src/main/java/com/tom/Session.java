package com.tom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Tom on 01/12/2016.
 */
public class Session {

    private PrintWriter out;
    private static ArrayList<String> activeSessions = new ArrayList<>();

    Session(Account account, Socket socket) {
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addActiveSession(account.getAccountName());
        Server.log(account.getAccountName() + " has logged in");
        this.out.println("Logging you in to account: " + account.getAccountName());
        new Menu(account.getAccountName(), socket);
    }

    public static void addActiveSession(String accountName) {
        activeSessions.add(accountName);
    }
    public static ArrayList<String> getActiveSessions() {
        return activeSessions;
    }
}

package com.tom;

import com.google.common.collect.ImmutableMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 01/12/2016.
 */
public class Session {

    private Account account;
    private PrintWriter out;
    private BufferedReader in;
    private static ArrayList<String> activeSessions = new ArrayList<>();

    Session(Account account, Socket socket) {
        this.account = account;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addActiveSession(this.account.getAccountName());
        this.out.println("Logging you in to account: " + this.account.getAccountName());
        new Menu(account.getAccountName(), socket);
    }

    public static void addActiveSession(String accountName) {
        activeSessions.add(accountName);
    }

    public static ArrayList<String> getActiveSessions() {
        return activeSessions;
    }



}

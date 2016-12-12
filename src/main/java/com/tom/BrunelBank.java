package com.tom;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank extends Thread {

    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    private Account account;

    public BrunelBank(Socket socket) {
        super("BrunelBank");
        this.socket = socket;
        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Brunel Bank running!");
        login("Welcome to the Brunel Bank! Type your account name below to get started: ");
    }

    private void login(String message) {
        out.println(message);
        try {
            String input = in.readLine();
            checkIfAccountExists(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfAccountExists(String accountName) {
        Account targetAccount = Database.getAccount(accountName);
        ArrayList<String> activeSessions = Session.getActiveSessions();
        if (targetAccount == null) {
            accountExistsFalse(accountName);
        } else if (activeSessions.contains(accountName)) {
            login("That account is already logged in! Try another account: ");
            out.println("hello! you can see me...");
        } else {
            this.account = targetAccount;
            new Session(targetAccount, socket);
        }
    }

    private void accountExistsFalse(String accountName) {
        out.println("Looks like that account doesn't exist. Either enter another account name or press 'y' to sign up.");
        try {
            String input = in.readLine();
            if (input.equals("y")) {
                Account newAccount = new Account(accountName);
                out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
                new Session(newAccount, socket);
            } else {
                checkIfAccountExists(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

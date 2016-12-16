package com.tom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Tom on 16/12/2016.
 */
public class Login {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Login(Socket socket, String message) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        attempt(message);
    }

    public void attempt(String message) {
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
            Server.log(accountName + " attempted login but already logged in");
            attempt("That account is already logged in! Try another account: ");
        } else {
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

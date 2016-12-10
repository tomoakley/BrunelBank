package com.tom;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.tom.utils.account;
import com.tom.utils.json;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank extends Thread {

    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    private List<Account> accounts;

    public BrunelBank(List<Account> accounts, Socket socket) {
        super("BrunelBank");
        this.socket = socket;
        this.accounts = accounts;
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
        Account targetAccount = account.findAccount(accountName);
        ArrayList<String> loggedInAccounts = account.getLoggedInAccounts();
        if (targetAccount == null) {
            accountExistsFalse(accountName);
        } else if (loggedInAccounts.contains(accountName)) {
            System.out.println(loggedInAccounts);
            login("That account is already logged in! Try another account: ");
        } else {
            new Session(targetAccount, accounts, socket);
        }
    }

    private void accountExistsFalse(String accountName) {
        out.println("Looks like that account doesn't exist. Either enter another account name or press 'y' to sign up.");
        try {
            String input = in.readLine();
            if (input.equals("y")) {
                Account newAccount = new Account(accountName);
                List<Account> existingAccountsList = json.getAccountsJson();
                assert existingAccountsList != null;
                existingAccountsList.add(newAccount);
                json.writeToJson(existingAccountsList);
                out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
                new Session(newAccount, accounts, socket);
            } else {
                checkIfAccountExists(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

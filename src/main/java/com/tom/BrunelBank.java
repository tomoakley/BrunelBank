package com.tom;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.tom.utils.account;
import com.tom.utils.json;
import com.tom.utils.readwrite;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank extends Thread {

    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;

    public BrunelBank(Socket socket) {
        super("BrunelBank");
        this.socket = socket;
        readwrite.setSocket(this.socket);
        this.out = readwrite.getWriter();
        this.in = readwrite.getReader();
    }

    public void run() {
        System.out.println("Brunel Bank running!");
        try {
            out.println("Welcome to the Brunel Bank! Type your account name below to get started: ");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                checkIfAccountExists(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfAccountExists(String accountName) {
        Account targetAccount = account.findAccount(accountName);
        if (targetAccount == null) {
            accountExistsFalse(accountName);
        } else {
            new Session(targetAccount);
        }
    }

    private void accountExistsFalse(String accountName) {
        out.print("Looks like that account doesn't exist. Either enter another account name or press 'y' to sign up.");
        try {
            String input = in.readLine();
            if (input.equals("y")) {
                Account newAccount = new Account(accountName);
                List<Account> existingAccountsList = json.getAccountsJson();
                assert existingAccountsList != null;
                existingAccountsList.add(newAccount);
                json.writeToJson(existingAccountsList);
                out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
                new Session(newAccount);
            } else {
                checkIfAccountExists(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

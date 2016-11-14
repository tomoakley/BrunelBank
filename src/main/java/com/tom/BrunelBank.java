package com.tom;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * Created by Tom on 14/11/2016.
 */

class Accounts {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }
}

public class BrunelBank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        tryLogin("Welcome to the Brunel Bank! Type your account name below to get started: ", scanner);
    }

    private static Accounts getAccountsJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader("src/accounts.json"));
            Gson gson = new Gson();
            Accounts accountsList = gson.fromJson(reader, Accounts.class);
            return accountsList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return new Accounts();
    }

    private static void tryLogin(String welcome, Scanner scanner) {
        System.out.println(welcome);
        String accountName = scanner.next();
        boolean accountExists = checkIfAccountExists(accountName);
        if (!accountExists) {
            accountExistsFalse(accountName, scanner);
        }
    }

    private static boolean checkIfAccountExists(String accountName) {
        Accounts accountsList = getAccountsJson();
        for (Account account : accountsList.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                System.out.println("Logging you in to " + account.getAccountName());
                return true;
            }
        }
        return false;
    }

    private static void accountExistsFalse(String accountName, Scanner scanner) {
        System.out.println("Looks like that account doesn't exist. Do you want to sign up as a new user? [y/n]");
        String input = scanner.next();
        if (input.equals("y")) {
            System.out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
            Account newAccount = new Account(accountName);
            List<Account> existingAccountsList = getAccountsJson().getAccounts();
            existingAccountsList.add(newAccount);
            Gson gson = new Gson();
            String newAccountsList = gson.toJson(existingAccountsList);
            try {
                FileOutputStream outputStream = new FileOutputStream("src/accounts.json");
                outputStream.write(newAccountsList.getBytes());
                outputStream.close();
            } catch(Exception e) {
                System.out.println("File not found!");
            }
        } else if (input.equals("n")) {
            tryLogin("Ok, enter another account name: ", scanner);
        } else {
            System.out.println("Please enter 'y' or 'n'");
        }
    }
}

package com.tom;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.tom.utils.json;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        tryLogin("Welcome to the Brunel Bank! Type your account name below to get started: ", scanner);
    }

    private static void tryLogin(String welcome, Scanner scanner) {
        System.out.print(welcome);
        String accountName = scanner.next();
        boolean accountExists = checkIfAccountExists(accountName);
        if (!accountExists) {
            accountExistsFalse(accountName, scanner);
        }
    }

    private static boolean checkIfAccountExists(String accountName) {
        List<Account> accountsList = json.getAccountsJson();
        assert accountsList != null;
        for (Account account : accountsList) {
            if (account.getAccountName().equals(accountName)) {
                new Session(account);
                return true;
            }
        }
        return false;
    }

    private static void accountExistsFalse(String accountName, Scanner scanner) {
        System.out.print("Looks like that account doesn't exist. Either enter another account name or press 'y' to sign up.");
        String input = scanner.next();
        if (input.equals("y")) {
            Account newAccount = new Account(accountName);
            List<Account> existingAccountsList = json.getAccountsJson();
            assert existingAccountsList != null;
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
            System.out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
            new Session(newAccount);
        } else {
            boolean accountExists = checkIfAccountExists(input);
            if (!accountExists) {
                accountExistsFalse(accountName, scanner);
            }
        }
    }
}

package com.tom;

import java.io.*;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.tom.utils.account;
import com.tom.utils.json;

/**
 * Created by Tom on 14/11/2016.
 */

public class BrunelBank {

    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        tryLogin("Welcome to the Brunel Bank! Type your account name below to get started: ");
    }

    private static void tryLogin(String welcome) {
        System.out.print(welcome);
        String accountName = scanner.next();
        checkIfAccountExists(accountName);
    }

    private static void checkIfAccountExists(String accountName) {
        Account targetAccount = account.findAccount(accountName);
        if (targetAccount == null) {
            accountExistsFalse(accountName);
        } else {
            new Session(targetAccount);
        }
    }

    private static void accountExistsFalse(String accountName) {
        System.out.print("Looks like that account doesn't exist. Either enter another account name or press 'y' to sign up.");
        String input = scanner.next();
        if (input.equals("y")) {
            Account newAccount = new Account(accountName);
            List<Account> existingAccountsList = json.getAccountsJson();
            assert existingAccountsList != null;
            existingAccountsList.add(newAccount);
            json.writeToJson(existingAccountsList);
            System.out.println("Great, we've signed you up! You're all set to deposit some money into your account.");
            new Session(newAccount);
        } else {
            checkIfAccountExists(input);
        }
    }
}

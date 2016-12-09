package com.tom;

import com.google.common.collect.ImmutableMap;
import com.tom.utils.account;
import com.tom.utils.json;
import com.tom.utils.readwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 05/12/2016.
 */
public class Action {

    int action;
    private PrintWriter out;
    private BufferedReader in;

    Action(int action) {
        this.action = action;
        this.out = readwrite.getWriter();
        this.in = readwrite.getReader();
        switch (this.action) {
            case 1:
                actionTransfer();
                break;
            case 2:
                actionDeposit();
                break;
            case 3:
                actionCheckBalance();
                break;
            case 4:
                actionLogout();
                break;
            default:
                System.out.println("That action doesn't exist");
        }
    }

    private void actionDeposit() {
        out.println("How much would you like to deposit? ");
        String amount;
        try {
            amount = in.readLine();
            List<Account> existingAccountsList = json.getAccountsJson();
            assert existingAccountsList != null;
            Account currentAccount = State.getAccount();
            int newBalance = currentAccount.getBalance() + parseInt(amount);
            int accountId = currentAccount.getId();
            Account storedDetails = existingAccountsList.get(accountId);
            storedDetails.setBalance(newBalance);
            existingAccountsList.remove(accountId);
            existingAccountsList.add(storedDetails);
            State.setAccount(storedDetails);
            json.writeToJson(existingAccountsList);
            out.println(amount + " deposited to account " + currentAccount.getAccountName() + ". Your new balance is " + State.getAccount().getBalance() + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionTransfer() {
        List<Account> accountsList = json.getAccountsJson();
        boolean receiverAccountValid = false;
        boolean amountToSendValid = false;
        String receiverAccountName;
        Account receiverAccount;
        out.println("Your balance is " + State.getAccount().getBalance() + ".");
        do {
            out.println("Enter the account name you would like to send money to: ");
            try {
                receiverAccountName = in.readLine();
                receiverAccount = account.findAccount(receiverAccountName);
                if (receiverAccount == null) {
                    out.println("The account " + receiverAccountName + " doesn't exist.");
                } else {
                    receiverAccountValid = true;
                }
            } catch (IOException e) {
                out.println("Something went wrong. Returning to main account menu.");
                e.printStackTrace();
                return;
            }
        } while (!receiverAccountValid);
        do {
            out.println("Enter the amount you want to send to " + receiverAccountName + " (enter 0 to return to main menu): ");
            int amountToSend = 0;
            try {
                amountToSend = parseInt(in.readLine());
                Account senderAccount = State.getAccount();
                if (senderAccount.getBalance() >= amountToSend && amountToSend > 0) {
                    amountToSendValid = true;
                    int newSenderBalance = senderAccount.getBalance() - amountToSend;
                    int newReceiverBalance = receiverAccount.getBalance() + amountToSend;
                    senderAccount.setBalance(newSenderBalance);
                    receiverAccount.setBalance(newReceiverBalance);
                    accountsList.remove(senderAccount.getId());
                    accountsList.remove(receiverAccount.getId());
                    accountsList.add(senderAccount);
                    accountsList.add(receiverAccount);
                    State.setAccount(senderAccount);
                    json.writeToJson(accountsList);
                    out.println(amountToSend + " sent to " + receiverAccount.getAccountName() + ". You have a new balance of " + senderAccount.getBalance() + ".");
                } else if (amountToSend == 0) {
                    return; // return to main menu by ending method with empty return
                } else if (amountToSend < 0) {
                    out.println("The amount to send must be bigger than 0.");
                } else {
                    out.println("Your account does not have enough balance to send that.");
                }
            } catch (IOException e) {
                out.println("Something went wrong. Returning to main account menu.");
                e.printStackTrace();
                return;
            }
        } while(!amountToSendValid);
    }

    private void actionCheckBalance() {
        Account account = State.getAccount();
        out.println("The balance of account " + account.getAccountName() + " is " + account.getBalance());
    }

    private void actionLogout() {
        out.println("Thanks for using the Brunel Bank. Goodbye!");
        State.setAccount(null);
    }

}

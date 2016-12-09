package com.tom;

import com.tom.utils.account;
import com.tom.utils.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 05/12/2016.
 */
public class Action {

    int action;
    private PrintWriter out;
    private BufferedReader in;
    private Account account;
    private Socket socket;

    Action(int action, Socket socket, Account account) {
        this.socket = socket;
        this.action = action;
        this.account = account;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (this.action) {
            case 0:
                actionTransfer();
                break;
            case 1:
                actionDeposit();
                break;
            case 2:
                actionWithdraw();
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
            int newBalance = account.getBalance() + parseInt(amount);
            int accountId = account.getId();
            account.setBalance(newBalance);
            existingAccountsList.get(accountId).setBalance(newBalance);
            json.writeToJson(existingAccountsList);
            out.println(amount + " deposited to account " + account.getAccountName() + ". Your new balance is " + existingAccountsList.get(accountId).getBalance() + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Menu(account, socket);
    }

    private void actionWithdraw() { // could be combined with actionDeposit
        out.println("How much would you like to withdraw? ");
        int amount;
        try {
            amount = parseInt(in.readLine());
            List<Account> existingAccountsList = json.getAccountsJson();
            assert existingAccountsList != null;
            int newBalance = account.getBalance() - amount;
            int accountId = account.getId();
            account.setBalance(newBalance);
            existingAccountsList.get(accountId).setBalance(newBalance);
            out.println(amount + " withdrawn from account " + account.getAccountName() + ". Your new balance is " + account.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Menu(account, socket);
    }

    private void actionTransfer() {
        List<Account> accountsList = json.getAccountsJson();
        boolean receiverAccountValid = false;
        boolean amountToSendValid = false;
        String receiverAccountName;
        Account receiverAccount;
        out.println("Your balance is " + account.getBalance() + ".");
        do {
            out.println("Enter the account name you would like to send money to: ");
            try {
                receiverAccountName = in.readLine();
                receiverAccount = com.tom.utils.account.findAccount(receiverAccountName);
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
                Account senderAccount = account;
                if (senderAccount.getBalance() >= amountToSend && amountToSend > 0) {
                    amountToSendValid = true;
                    int newSenderBalance = senderAccount.getBalance() - amountToSend;
                    int newReceiverBalance = receiverAccount.getBalance() + amountToSend;
                    senderAccount.setBalance(newSenderBalance);
                    receiverAccount.setBalance(newReceiverBalance);
                    accountsList.get(senderAccount.getId()).setBalance(newSenderBalance);
                    accountsList.get(receiverAccount.getId()).setBalance(newReceiverBalance);
                    json.writeToJson(accountsList);
                    out.println(amountToSend + " sent to " + receiverAccount.getAccountName() + ". You have a new balance of " + senderAccount.getBalance() + ".");
                } else if (amountToSend == 0) {
                    new Menu(account, socket);
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
        new Menu(account, socket);
    }

    private void actionCheckBalance() {
        out.println("The balance of account " + account.getAccountName() + " is " + account.getBalance());
        new Menu(account, socket);
    }

    private void actionLogout() {
        out.println("Thanks for using the Brunel Bank. Goodbye!");
        com.tom.utils.account.getLoggedInAccounts().remove(account.getAccountName());
        System.exit(1);
    }

}

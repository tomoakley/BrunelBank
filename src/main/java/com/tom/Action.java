package com.tom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 05/12/2016.
 */
public class Action {

    int action;
    private PrintWriter out;
    private BufferedReader in;
    private Account account;

    Action(int action, Socket socket, Account account) {
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
        new Menu(account.getAccountName(), socket);
    }

    private void actionDeposit() {
        out.println("How much would you like to deposit? ");
        String amount;
        try {
            amount = in.readLine();
            double newBalance = account.getBalance() + Double.parseDouble(amount);
            account.setBalance(newBalance);
            out.println(amount + " deposited to account " + account.getAccountName() + ". Your new balance is " + account.getBalance() + ".");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void actionWithdraw() { // could be combined with actionDeposit
        out.println("How much would you like to withdraw? ");
        double amount;
        try {
            amount = Double.parseDouble(in.readLine());
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            out.println(amount + " withdrawn from account " + account.getAccountName() + ". Your new balance is " + account.getBalance());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void actionTransfer() {
        boolean receiverAccountValid = false;
        boolean amountToSendValid = false;
        Account senderAccount = account;
        String receiverAccountName;
        Account receiverAccount;
        out.println("Your balance is " + account.getBalance() + ".\n");
        do {
            out.println("Enter the account name you would like to send money to: ");
            try {
                receiverAccountName = in.readLine();
                receiverAccount = Database.getAccount(receiverAccountName);
                if (receiverAccount == null) {
                    out.println("The account " + receiverAccountName + " doesn't exist.\n");
                } else if (Objects.equals(receiverAccount.getAccountName(), senderAccount.getAccountName())) {
                    out.println("You cannot send money to yourself.\n");
                } else {
                    receiverAccountValid = true;
                }
            } catch (IOException e) {
                out.println("Something went wrong. Returning to main account menu.\n");
                e.printStackTrace();
                return;
            }
        } while (!receiverAccountValid);
        do {
            out.println("Enter the amount you want to send to " + receiverAccountName + " (enter 0 to return to main menu): ");
            double amountToSend = 0;
            try {
                amountToSend = Double.parseDouble(in.readLine());
                if (senderAccount.getBalance() >= amountToSend && amountToSend > 0) {
                    amountToSendValid = true;
                    try {
                        double newSenderBalance = senderAccount.getBalance() - amountToSend;
                        double newReceiverBalance = receiverAccount.getBalance() + amountToSend;
                        Thread thisThread = Thread.currentThread();
                        senderAccount.setLock(thisThread);
                        receiverAccount.setLock(thisThread);
                        senderAccount.setBalance(newSenderBalance);
                        receiverAccount.setBalance(newReceiverBalance);
                        senderAccount.releaseLock();
                        receiverAccount.releaseLock();
                        out.println(amountToSend + " sent to " + receiverAccount.getAccountName() + ". You have a new balance of " + senderAccount.getBalance() + ".");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (amountToSend == 0) {
                    return;
                } else if (amountToSend < 0) {
                    out.println("The amount to send must be bigger than 0.\n");
                } else {
                    out.println("Your account does not have enough balance to send that.\n");
                }
            } catch (IOException e) {
                out.println("Something went wrong. Returning to main account menu.\n");
                e.printStackTrace();
                return;
            }
        } while(!amountToSendValid);
    }

    private void actionCheckBalance() {
        out.println("The balance of account " + account.getAccountName() + " is " + account.getBalance());
    }

    private void actionLogout() {
        out.println("Thanks for using the Brunel Bank. Goodbye!");
        com.tom.utils.account.getLoggedInAccounts().remove(account.getAccountName());
        System.exit(1);
    }

}

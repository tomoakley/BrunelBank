package com.tom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by Tom on 05/12/2016.
 */
public class Action {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Account account;

    Action(int action, Socket socket, Account account) {
        this.account = account;
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (action) {
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
        if (action != 4) {
            new Menu(account.getAccountName(), socket);
        }
    }

    private void actionDeposit() {
        out.println("How much would you like to deposit? ");
        String amount;
        try {
            account.lock();
            amount = in.readLine();
            double newBalance = account.getBalance() + Double.parseDouble(amount);
            account.setBalance(newBalance);
            out.println(amount + " deposited to account " + account.getAccountName() + ". Your new balance is " + account.getBalance() + ".");
            account.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionWithdraw() { // could be combined with actionDeposit
        out.println("How much would you like to withdraw? ");
        double amount;
        try {
            account.lock();
            amount = Double.parseDouble(in.readLine());
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            out.println(amount + " withdrawn from account " + account.getAccountName() + ". Your new balance is " + account.getBalance());
            account.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionTransfer() {
        boolean receiverAccountValid = false;
        boolean amountToSendValid = false;
        Account senderAccount = account;
        String receiverAccountName;
        Account receiverAccount;
        senderAccount.lock();
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
                    receiverAccount.lock();
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
                    double newSenderBalance = senderAccount.getBalance() - amountToSend;
                    double newReceiverBalance = receiverAccount.getBalance() + amountToSend;
                    senderAccount.setBalance(newSenderBalance);
                    receiverAccount.setBalance(newReceiverBalance);
                    out.println(amountToSend + " sent to " + receiverAccount.getAccountName() + ". You have a new balance of " + senderAccount.getBalance() + ".");
                    senderAccount.release();
                    receiverAccount.release();
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
        account.lock();
        out.println("The balance of account " + account.getAccountName() + " is " + account.getBalance());
        account.release();
    }

    private void actionLogout() {
        out.println("Thanks for using the Brunel Bank. Goodbye!\n");
        Server.log(account.getAccountName() + " logged out");
        Session.getActiveSessions().remove(account.getAccountName());
        Server.log("Active sessions: " + Session.getActiveSessions());
        account.logout();
        new Login(socket, "Welcome to the Brunel Bank! Type your account name below to get started: ");
    }

}

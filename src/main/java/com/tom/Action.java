package com.tom;

import com.google.common.collect.ImmutableMap;
import com.tom.utils.account;
import com.tom.utils.json;

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
    Scanner scan;

    Action(int action) {
        this.action = action;
        this.scan = new Scanner(System.in);
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
        System.out.print("How much would you like to deposit? ");
        String amount = scan.next();
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
        System.out.println(amount + " deposited to account " + currentAccount.getAccountName() + ". Your new balance is " + State.getAccount().getBalance() + ".");
    }

    private void actionTransfer() {
        List<Account> accountsList = json.getAccountsJson();
        boolean receiverAccountValid = false;
        boolean amountToSendValid = false;
        String receiverAccountName;
        Account receiverAccount;
        do {
            System.out.print("Enter the account name you would like to send money to: ");
            receiverAccountName = scan.next();
            receiverAccount = account.findAccount(receiverAccountName);
            if (receiverAccount == null) {
                System.out.println("The account " + receiverAccountName + " doesn't exist.");
            } else {
                receiverAccountValid = true;
            }
        } while (!receiverAccountValid);
        do {
            System.out.print("Enter the amount you want to send to " + receiverAccountName + " (enter 0 to return to main menu): ");
            int amountToSend = parseInt(scan.next());
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
                System.out.println(amountToSend + " sent to " + receiverAccount.getAccountName() + ". You have a new balance of " + senderAccount.getBalance() + ".");
            } else if (amountToSend == 0) {
                return;
            } else if (amountToSend < 0) {
                System.out.println("The amount to send must be bigger than 0.");
            } else {
                System.out.println("Your account does not have enough balance to send that.");
            }
        } while(!amountToSendValid);
    }

    private void actionCheckBalance() {
        Account account = State.getAccount();
        System.out.println("The balance of account " + account.getAccountName() + " is " + account.getBalance());
    }

    private void actionLogout() {
        System.out.println("Thanks for using the Brunel Bank. Goodbye!");
        State.setAccount(null);
    }

}

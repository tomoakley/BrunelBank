package com.tom;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 01/12/2016.
 */
public class Session {

    Account account;
    int accNumber;
    Scanner scanner;
    Map<Integer, String> menuOptions = ImmutableMap.of(
            1, "Transfer Money",
            2, "Deposit Money",
            3, "View Balance",
            4, "Logout"
    );
    int chosenOption;

    Session(int accNumber, Account account) {
        this.account = account;
        this.accNumber = accNumber;
        this.scanner = new Scanner(System.in);
        this.chosenOption = 0;
        State.setAccount(this.accNumber, this.account);
        System.out.println("Logging you in to account: " + this.account.getAccountName());
        mainMenu();
    }

    private void mainMenu() {
        System.out.println("Account Menu");
        for (int i = 1; i <= menuOptions.size(); ++i) {
            String option = "(" + i + ") " + menuOptions.get(i);
            System.out.println(option);
        }
        boolean isOptionValid = false;
        do {
            System.out.print("Please choose an option: ");
            try {
                chosenOption = parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("You must choose a number between 1 and " + menuOptions.size());
                isOptionValid = false;
            }
            if (chosenOption > 0 && chosenOption <= menuOptions.size()) {
                isOptionValid = true;
            }
        } while (!isOptionValid);
        new Action("deposit");
    }



}

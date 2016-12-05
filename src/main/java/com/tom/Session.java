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

    String account;
    String action;
    Scanner scanner;
    Map<Integer, String> menuOptions = ImmutableMap.of(
            1, "Transfer Money",
            2, "Deposit Money",
            3, "View Balance",
            4, "Logout"
    );
    int chosenOption;

    Session(String account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
        this.chosenOption = 0;
        System.out.println("Logging you in to account: " + account);
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
        System.out.println("Woo you passed!");
    }



}

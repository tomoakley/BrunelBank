package com.tom;

import com.google.common.collect.ImmutableMap;
import com.tom.utils.readwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 01/12/2016.
 */
public class Session {

    Account account;
    Scanner scanner;
    Map<Integer, String> menuOptions = ImmutableMap.of(
            1, "Transfer Money",
            2, "Deposit Money",
            3, "View Balance",
            4, "Logout"
    );
    private PrintWriter out;
    private BufferedReader in;

    Session(Account account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
        this.out = readwrite.getWriter();
        this.in = readwrite.getReader();
        State.setAccount(this.account);
        com.tom.utils.account.addLoggedInAccount(this.account.getAccountName());
        this.out.println("Logging you in to account: " + this.account.getAccountName());
        mainMenu();
    }

    private void mainMenu() {
        String mainMenu = "Account Menu \n";
        int chosenOption = 0;
        for (int i = 1; i <= menuOptions.size(); ++i) {
            mainMenu += "(" + i + ") " + menuOptions.get(i)+ "\n";
        }
        boolean isOptionValid = false;
        do {
            out.println(mainMenu);
            out.println("Please choose an option: ");
            try {
                chosenOption = parseInt(in.readLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.println("You must choose a number between 1 and " + menuOptions.size());
                isOptionValid = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (chosenOption > 0 && chosenOption <= menuOptions.size()) {
                isOptionValid = true;
            }
        } while (!isOptionValid);
        new Action(chosenOption);
        if (chosenOption != 4) {
            mainMenu();
        }
    }



}

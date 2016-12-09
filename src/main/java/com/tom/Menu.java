package com.tom;

import com.google.common.collect.ImmutableMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 09/12/2016.
 */
public class Menu {

    private ArrayList<String> menuOptions = new ArrayList<>();
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private Account account;

    public Menu(Account account, Socket socket) {
        this.socket = socket;
        this.account = account;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.menuOptions.add("Transfer Money");
        this.menuOptions.add("Deposit Money");
        this.menuOptions.add("Withdraw Money");
        this.menuOptions.add("View Balance");
        this.menuOptions.add("Logout");
        mainMenu();
    }

    private void mainMenu() {
        boolean isOptionValid = false;
        int chosenOption = 0;
        String mainMenu = "";
        do {
            mainMenu = "Account Menu \n";
            for (int i = 0; i < menuOptions.size(); ++i) {
                mainMenu += "(" + i + ") " + menuOptions.get(i) + "\n";
            }
            out.println(mainMenu);
            out.println("Please choose an option: ");
            try {
                chosenOption = parseInt(in.readLine());
                if (chosenOption <= menuOptions.size()) {
                    isOptionValid = true;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.println("You must choose a number between 0 and " + menuOptions.size());
                isOptionValid = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!isOptionValid);
        new Action(chosenOption, socket, account);
    }

}

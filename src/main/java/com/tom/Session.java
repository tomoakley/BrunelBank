package com.tom;

import com.google.common.collect.ImmutableMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by Tom on 01/12/2016.
 */
public class Session {

    private Account account;
    private PrintWriter out;
    private BufferedReader in;
    private List<Account> accounts;

    Session(Account account, List<Account> accounts, Socket socket) {
        this.account = account;
        this.accounts = accounts;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        com.tom.utils.account.addLoggedInAccount(this.account.getAccountName());
        this.out.println("Logging you in to account: " + this.account.getAccountName());
        new Menu(account, accounts, socket);
    }

}

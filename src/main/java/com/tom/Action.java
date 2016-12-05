package com.tom;

import com.google.common.collect.ImmutableMap;
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

   String action;

   Action(String action) {
      this.action = action;
      switch (this.action) {
          case "deposit":
              actionDeposit();
              break;
          default:
              System.out.println("That action doesn't exist");
      }
   }

    private void actionDeposit() {
        Scanner msg = new Scanner(System.in);
        System.out.print("How much would you like to deposit? ");
        String amount = msg.next();
        List<Account> existingAccountsList = json.getAccountsJson();
        assert existingAccountsList != null;
        Account currentAccount = State.getAccount();
        int newBalance = currentAccount.getBalance() + parseInt(amount);
        Account storedDetails = existingAccountsList.get(State.getAccountNumber());
        storedDetails.setBalance(newBalance);
        existingAccountsList.remove(State.getAccountNumber());
        existingAccountsList.add(storedDetails);
        State.setAccount(existingAccountsList.size() - 1, storedDetails);
        json.writeToJson(existingAccountsList);
        System.out.println(amount + " deposited to account " + currentAccount.getAccountName() + ". Your new balance is " + State.getAccount().getBalance() + ".");
    }

}

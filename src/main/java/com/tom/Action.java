package com.tom;

import java.util.Scanner;

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
        /* Scanner msg = new Scanner(System.in);
        System.out.print("How much would you like to deposit?");
        String amount = msg.next(); */
        Account account = State.getAccount();
        System.out.println("Account name: " + account.getAccountName());

    }

}

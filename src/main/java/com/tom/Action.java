package com.tom;

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
        System.out.println(State.getAccountName());
    }

}

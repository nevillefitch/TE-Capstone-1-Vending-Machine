package com.techelevator.view;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class VendingMachine {

    private static final int QUARTER = 25;
    private static final int DIME = 10;
    private static final int NICKEL = 5;

    private static final int BEGINNING_STOCK = 5;
    public static final String FEED_MONEY = "FEED MONEY";
    public static final String GIVE_CHANGE = "GIVE CHANGE";

    private Menu menu;
    private double previousCustBalance = 0.0;
    private double currentCustBalance = 0.0;

    String snackItemFileName = "vendingmachine.csv"; //Where is the best place for this? here? or
    // in CLI?
    private List<SnackItem> snackItemsList = new ArrayList<>();
    private Map<String, Integer> snackItemQuantity = new HashMap<>();

    //should the snackItemFileName be passed into the constructor?
    public VendingMachine() {
        populateSnackItemList(snackItemFileName);
        setSnackItemQuantity();
    }

    //Display snack items and quantity
    public void displaySnackItemsList() {
        int lineCounter = 0;
        for (SnackItem items : snackItemsList) {
            int quantity = snackItemQuantity.get(items.getID());
            String strQuantity;
            if (quantity == 0) {
                strQuantity = "SOLD OUT";
            } else {
                strQuantity = String.valueOf(quantity);
            }
            System.out.printf("%4s %-25s %2.2f\t", items.getID(),
                    (items.getName() + " (" + strQuantity + ")"), items.getPrice());
            lineCounter++;
            if (lineCounter % 4 == 0)
                System.out.println();
        }
    }

    public void feedMoney(double money){
        updateCustomerBalance(money);
        logTransaction(VendingMachine.FEED_MONEY);
    }

    // If snack items available (quantity > 0) and customer has money to cover it, dispense it.
    public void purchaseSnack(String ID) {
        double price = 0.0;
        int itemIDTest = 0;

        //Validate snack ID exists and if not, ask the user to re-enter
        while (!snackItemQuantity.containsKey(ID)) {
            Scanner input = new Scanner(System.in); //Is this a duplicate scanner? should inputs
            // only be in menu?
            System.out.println("Please enter valid item ID: ");
            ID = input.nextLine().toUpperCase(Locale.ROOT);
        }

            //Get quantity of snack available.
            for (int i = 0; i < snackItemsList.size(); i++) {
                if (ID.equals(snackItemsList.get(i).getID())) {
                    price = snackItemsList.get(i).getPrice();
                    SnackItem snack = snackItemsList.get(i);
                    itemIDTest = i;
                    break;
                }
            }


            /*
            If item is available (quantity >0) and the customer has money to cover the
            transaction, vend item, update customer balance, and write to log. */
            int startingQuantity = snackItemQuantity.get(ID);
            if(startingQuantity > 0) {
                if (price <= currentCustBalance) {
                    updateCustomerBalance(-price);

                    snackItemQuantity.put(ID, startingQuantity);
                    snackItemsList.get(itemIDTest).getSound();
                    System.out.println("\n\n" +  snackItemsList.get(itemIDTest).getSound() + "\n" +
                            "\n");
                    System.out.println("Customer Balance: $" + String.format("%.2f", currentCustBalance));

                    //log transaction
                    String nameOfSnack = snackItemsList.get(itemIDTest).getName();
                    logTransaction(nameOfSnack);

                } else {
                    System.out.println("You do not have the funds to cover this transaction.");
                }
            } else {
                System.out.println("Item is sold out.");
            }
    }

    //Returns customer's change after purchasing a snack.
    public void makeChange() {

        int balance = (int) (currentCustBalance * 100);

        int numOfQuarters = balance/QUARTER;
       balance -= numOfQuarters * QUARTER;
        int numOfDimes = balance/DIME;
        balance -= numOfDimes * DIME;
        int numOfNickels = balance/NICKEL;
        balance -= numOfNickels * NICKEL;

        System.out.println("Your change is " + numOfQuarters + " quarters, " + numOfDimes + " dimes, and " + numOfNickels + " nickels.");

         previousCustBalance = 0;
         currentCustBalance = 0;

         logTransaction(GIVE_CHANGE);
    }


    //Populate the map of snack items and starting quantities.
    public void setSnackItemQuantity() {
        String key = "";
        int quantity = 5;
        for (int i = 0; i < snackItemsList.size(); i++) {
            key = snackItemsList.get(i).getID();
            snackItemQuantity.put(key, BEGINNING_STOCK);
        }
    }

    //Populate the list of snack items from a text file.
    public void populateSnackItemList(String snackItemFileName) {
        File path = new File(snackItemFileName);
        String nextLine;
        String[] tokens;
        SnackItem snack = null;
        try (Scanner input = new Scanner(path)) {
            while (input.hasNext()) {
                nextLine = input.nextLine();
                tokens = nextLine.split("\\|");
                String snackType = tokens[3];
                if (snackType.equals("Chip")) {
                    snack = new Chips(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
                } else if (snackType.equals("Gum")) {
                    snack = new Gum(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
                } else if (snackType.equals("Candy")) {
                    snack = new Candy(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
                } else if (snackType.equals("Drink")) {
                    snack = new Drink(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
                }
                snackItemsList.add(snack);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Write transaction to log file for audits.
    public void logTransaction(String transaction ){
        String logFileName = "src/main/resources/log.txt";
        File path = new File(logFileName);
        try(PrintWriter log = new PrintWriter((new FileOutputStream(path, true)))) {
            LocalDateTime transactionDateTime = LocalDateTime.now() ;
            log.write(transactionDateTime + "\t" + transaction + " " + previousCustBalance + " " + currentCustBalance + "\n");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(404);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(13);
        }catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(14);
        }
    }

    public void updateCustomerBalance(double inputMoney) {
        previousCustBalance = currentCustBalance;
        currentCustBalance += inputMoney;
    }

    public double getPreviousCustBalance() {
        return previousCustBalance;
    }

    public double getCustomerBalance() {
        return currentCustBalance;
    }
}

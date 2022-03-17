package com.techelevator.view;

public class Drink extends SnackItem{

    private String sound = "Glug Glug, Yum!";

    public final String getSound() {
        return "Glug Glug, Yum!";
    }
    public Drink(String ID, String name, Double price) {
        super(ID, name, price);
    }

    @Override
    public String toString() {
        return super.toString() + ", sound='" + sound + '\'' + "Drink";
    }
}

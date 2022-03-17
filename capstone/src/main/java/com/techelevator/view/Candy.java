package com.techelevator.view;

public class Candy extends SnackItem{


     private String sound =  "Munch Munch, Yum!";
    public final String getSound() {
        return "Munch Munch, Yum!";
    }

    public Candy(String ID, String name, Double price) {
        super(ID, name, price);
    }

    @Override
    public String toString() {
        return super.toString() + ", sound='" + sound + '\'' + "Candy";
    }
}

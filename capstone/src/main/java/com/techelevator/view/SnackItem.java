package com.techelevator.view;

import java.util.ArrayList;
import java.util.List;

public abstract class SnackItem {

    private String ID;
    private String name;
    private Double price;
    private String sound;

    public String getSound() {
        return sound;
    }

    //    List<SnackItem> sounds = new ArrayList<>();

    public SnackItem(String ID, String name, Double price) {
        this.ID = ID;
        this.name = name;
        this.price = price;
    }


    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "SnackItems{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price + "}";
    }
}

package com.app.hungerhelp.models;

public class FoodItem {
    private String name;
    private String category;

    public FoodItem(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}


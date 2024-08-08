package com.app.hungerhelp.models;

import java.util.List;

public class AddDonationModel {

    private String foodItem;
    private String description;
    private String quantity;
    private String location;
    private String availableTill;
    private String notes;
    private List<String> imagePaths;

    public AddDonationModel(String foodItem, String description, String quantity, String location, String availableTill, String notes, List<String> imagePaths) {
        this.foodItem = foodItem;
        this.description = description;
        this.quantity = quantity;
        this.location = location;
        this.availableTill = availableTill;
        this.notes = notes;
        this.imagePaths = imagePaths;
    }

    // Getters and Setters

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailableTill() {
        return availableTill;
    }

    public void setAvailableTill(String availableTill) {
        this.availableTill = availableTill;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}


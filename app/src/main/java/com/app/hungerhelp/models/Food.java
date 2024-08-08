package com.app.hungerhelp.models;

import java.io.Serializable;
import java.util.List;

public class Food implements Serializable {
    private String _id;
    private String foodItem;
    private String description;
    private String quantity;
    private String location;
    private String availableTill;
    private String notes;
    private List<String> images;
    private String status;
    private List<Object> requests;  // Adjust the type based on your actual data

    // Getters and setters for all fields


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getRequests() {
        return requests;
    }

    public void setRequests(List<Object> requests) {
        this.requests = requests;
    }

    // Add a serialVersionUID
    private static final long serialVersionUID = 1L;
}


//package com.app.hungerhelp.models;
//
//import java.io.Serializable;
//import java.util.List;
//
//public class Food implements Serializable {
//    private String _id;
//    private String foodItem;
//    private String description;
//    private String quantity;
//    private String location;
//    private String availableTill;
//    private String notes;
////    private String[] images;
//    private List<String> images;  // Ensure your list is serializable
//    private String status;
//
//    public String get_id() {
//        return _id;
//    }
//
//    public void set_id(String _id) {
//        this._id = _id;
//    }
//
//    public String getFoodItem() {
//        return foodItem;
//    }
//
//    public void setFoodItem(String foodItem) {
//        this.foodItem = foodItem;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getAvailableTill() {
//        return availableTill;
//    }
//
//    public void setAvailableTill(String availableTill) {
//        this.availableTill = availableTill;
//    }
//
//    public String getNotes() {
//        return notes;
//    }
//
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
//
//    public List<String> getImages() {
//        return images;
//    }
//
//    public void setImages(List<String> images) {
//        this.images = images;
//    }
//
//
////    public String[] getImages() {
////        return images;
////    }
////
////    public void setImages(String[] images) {
////        this.images = images;
////    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}

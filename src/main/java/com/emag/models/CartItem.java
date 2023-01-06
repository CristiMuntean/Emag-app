package com.emag.models;

public class CartItem {
    private String userEmail;
    private String itemName;
    private String itemQuantity;

    public CartItem() {
    }

    public CartItem(String userEmail, String itemName, String itemQuantity) {
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    @Override
    public String toString() {
        return this.userEmail + "item name: " + this.itemName + ", quantity: " + this.itemQuantity;
    }
}

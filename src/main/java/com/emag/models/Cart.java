package com.emag.models;

import java.util.ArrayList;

public class Cart {
    private User user;
    private ArrayList<CartItem> cartItems;

    public Cart(User user, ArrayList<CartItem> cartItems) {
        this.user = user;
        this.cartItems = cartItems;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }
}

package com.emag.services;

import com.emag.models.Cart;
import com.emag.models.CartItem;
import com.emag.models.Product;
import com.emag.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CartService {
    private static final String COL_NAME = "cart_items";

    public String createCartItem(CartItem cartItem) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(cartItem.getUserEmail() + cartItem.getItemName()).set(cartItem);
        return "Cart item " + cartItem + " added successfully at time: " + collectionsApiFuture.get().getUpdateTime();
    }

    public ArrayList<CartItem> getCartItems() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<CartItem> cartItems = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                CartItem cartItem = document.toObject(CartItem.class);
                cartItems.add(cartItem);
            }
            return cartItems;
        } catch (InterruptedException | ExecutionException e) {
            return new ArrayList<>();
        }
    }

    public Cart getCartForUser(User user) {
        ArrayList<CartItem> cartItems = getCartItems();
        cartItems.removeIf(cartItem -> !cartItem.getUserEmail().equals(user.getEmail()));
        return new Cart(user,cartItems);
    }

    public String increaseCartItemQuantity(User user, Product product) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CartItem item = containsItemInCartForUser(user,product);
        int quantity = Integer.parseInt(item.getItemQuantity()) +1;
        item.setItemQuantity(Integer.toString(quantity));
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getEmail() + item.getItemName())
                .set(item);
        return "Cart item " + item + " updated successfully at time: " + collectionsApiFuture.get().getUpdateTime();
    }

    public CartItem containsItemInCartForUser(User user, Product product) {
        ArrayList<CartItem> cartItems = getCartForUser(user).getCartItems();
        for(CartItem cartItem: cartItems) {
            if (cartItem.getItemName().equals(product.getProductName()))return cartItem;
        }
        return null;
    }

    public String deleteItem(CartItem cartItem) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COL_NAME).document(cartItem.getUserEmail() + cartItem.getItemName()).delete();
        return "Document with email " + cartItem.getUserEmail() + cartItem.getItemName() + " has been deleted.";
    }

}

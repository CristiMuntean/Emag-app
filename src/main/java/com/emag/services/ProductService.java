package com.emag.services;


import com.emag.models.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {
    private static final String COL_NAME = "products";

    public ArrayList<Product> getProducts() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<Product> products = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                Product product = document.toObject(Product.class);
                products.add(product);
            }
            return products;
        } catch (InterruptedException | ExecutionException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<Product> getProductsWithContainFilter(String filter){
        ArrayList<Product> products = getProducts();
        products.removeIf(product -> !product.getProductName().contains(filter));
        return products;
    }

    public Product getProductWithName(String name) {
        ArrayList<Product> products = getProducts();
        for(Product product: products) {
            if (product.getProductName().equals(name))return product;
        }
        return null;
    }
}

package com.emag.services;

import com.emag.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final String COL_NAME = "users";

    public String saveUserDetails(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getEmail())
                .set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public User getUserDetails(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(email);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();
        User user = null;
        if (document.exists()) {
            user = document.toObject(User.class);
        }
        return user;
    }

    public String updateUserDetails(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getEmail())
                .set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String email) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COL_NAME).document(email).delete();
        return "Document with email " + email + " has been deleted.";
    }

    public ArrayList<User> getUsers() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("users").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                users.add(user);
            }
            return users;
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }
}

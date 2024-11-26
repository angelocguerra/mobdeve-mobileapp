package com.example.mobdevemobileapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirestoreManager {
    private static FirebaseFirestore instance;

    private FirestoreManager() {
        // Private constructor to prevent instantiation
    }

    public static FirebaseFirestore getInstance() {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }

    public void addUser(String userId, Map<String, Object> user) {
        instance.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreManager", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("FirestoreManager", "Error writing document", e));
    }

    public void getUser(String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        instance.collection("users").document(userId).get().addOnCompleteListener(onCompleteListener);
    }
}
// FirestoreManager.java
package com.example.mobdevemobileapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private static FirestoreManager instance;
    private FirebaseFirestore db;

    private FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreManager getInstance() {
        if (instance == null) {
            instance = new FirestoreManager();
        }
        return instance;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void addUser(String userId, Map<String, Object> user) {
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreManager", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("FirestoreManager", "Error writing document", e));
    }

    public void getUser(String userName, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("users").document(userName).get().addOnCompleteListener(onCompleteListener);
    }

    public void checkIfUserExists(String field, String value, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("users").whereEqualTo(field, value).get().addOnCompleteListener(onCompleteListener);
    }

    public void getUserProfile(String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("users").document(userId).get().addOnCompleteListener(onCompleteListener);
    }

    public void addReviewToUser(String username, Review review, OnCompleteListener<Void> onCompleteListener) {

        //add test to reviews which is a field in the user document
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("title", "Review Title");
        reviewMap.put("rating", "5");
        reviewMap.put("content", "I hate this company");
        // Add other review fields as needed

        db.collection("users").document(username).collection("reviews")
                .document("Review Title").set(reviewMap)
                .addOnCompleteListener(onCompleteListener);

    }

    public void fetchReviews(String username, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("users").document(username).collection("reviews").get().addOnCompleteListener(onCompleteListener);
    }
}
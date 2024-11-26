// FirestoreManager.java
package com.example.mobdevemobileapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.Tasks;
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
    public void updateUsername(String currentUsername, String newUsername, OnCompleteListener<Void> listener) {
        getUser(currentUsername, task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Check if the new username already exists
                getUser(newUsername, usernameTask -> {
                    if (usernameTask.isSuccessful() && !usernameTask.getResult().exists()) {
                        // Get the current document data
                        Map<String, Object> currentData = task.getResult().getData();
                        if (currentData != null) {
                            // Add the new username to the data
                            currentData.put("username", newUsername);

                            // Create a new document with the new username
                            db.collection("users").document(newUsername)
                                    .set(currentData)
                                    .addOnCompleteListener(newDocTask -> {
                                        if (newDocTask.isSuccessful()) {
                                            // Delete the old document
                                            db.collection("users").document(currentUsername)
                                                    .delete()
                                                    .addOnCompleteListener(deleteTask -> {
                                                        if (deleteTask.isSuccessful()) {
                                                            listener.onComplete(Tasks.forResult(null));
                                                        } else {
                                                            listener.onComplete(Tasks.forException(deleteTask.getException()));
                                                        }
                                                    });
                                        } else {
                                            listener.onComplete(Tasks.forException(newDocTask.getException()));
                                        }
                                    });
                        } else {
                            listener.onComplete(Tasks.forException(new Exception("Failed to retrieve current user data")));
                        }
                    } else {
                        listener.onComplete(Tasks.forException(new Exception("New username already exists")));
                    }
                });
            } else {
                listener.onComplete(Tasks.forException(new Exception("User does not exist")));
            }
        });
    }



    public void addReviewToUser(String username, Review review, OnCompleteListener<Void> onCompleteListener) {

        //add test to reviews which is a field in the user document
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("workEnvironment", review.getWorkEnvironment());
        reviewMap.put("mentorship", review.getMentorship());
        reviewMap.put("workload", review.getWorkload());
        reviewMap.put("internshipType", review.getInternshipType());
        reviewMap.put("allowanceProvision", review.getAllowanceProvision());
        reviewMap.put("reviewTitle", review.getReviewTitle());
        reviewMap.put("user", review.getUser().getUsername());
        reviewMap.put("datePosted", review.getDatePosted());
        reviewMap.put("reviewText", review.getReviewText());
        reviewMap.put("helpful", review.getHelpful());

        // Add other review fields as needed

        db.collection("users").document(username).collection("reviews")
                .document(review.getReviewTitle()).set(reviewMap)
                .addOnCompleteListener(onCompleteListener);

    }

    public void fetchReviews(String username, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("users").document(username).collection("reviews").get().addOnCompleteListener(onCompleteListener);
    }

    public User getUserFromDocument(DocumentSnapshot document) {
        String username = document.getString("username");
        String email = document.getString("email");
        String password = document.getString("password");
        String profileCreated = document.getString("profileCreated");
        return new User(username, email, password, profileCreated);
    }
}
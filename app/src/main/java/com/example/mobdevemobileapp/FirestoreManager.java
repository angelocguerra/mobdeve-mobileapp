package com.example.mobdevemobileapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public void addUser(String userId, Map<String, Object> user, OnCompleteListener<Void> onCompleteListener) {
        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(onCompleteListener);
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


    public void addReview(Review review, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("workEnvironment", review.getWorkEnvironment());
        reviewMap.put("mentorship", review.getMentorship());
        reviewMap.put("workload", review.getWorkload());
        reviewMap.put("internshipType", review.getInternshipType());
        reviewMap.put("allowanceProvision", review.getAllowanceProvision());
        reviewMap.put("reviewTitle", review.getReviewTitle());
        reviewMap.put("user", review.getUser() != null ? review.getUser().getUsername() : review.getUsername());
        reviewMap.put("datePosted", review.getDatePosted());
        reviewMap.put("reviewText", review.getReviewText());
        reviewMap.put("helpful", review.getHelpful());
        reviewMap.put("companyName", review.getCompanyName());
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (db.collection("reviews").document(uuid).get().isSuccessful());
        reviewMap.put("uuid", uuid);
        Log.d("FirestoreManager", "Adding review with UUID: " + uuid);
        // Add other review fields as needed

        db.collection("reviews").document(review.getReviewTitle()).set(reviewMap)
                .addOnCompleteListener(onCompleteListener);
    }

    public void addReviewToUser(String username, Review review, OnCompleteListener<Void> onCompleteListener) {

        //add test to reviews which is a field in the user document
        Map<String, Object> reviewMap = new HashMap<>();



        reviewMap.put("internshipType", review.getInternshipType());
        reviewMap.put("allowanceProvision", review.getAllowanceProvision());
        reviewMap.put("reviewTitle", review.getReviewTitle());
        reviewMap.put("user", review.getUser().getUsername());
        reviewMap.put("datePosted", review.getDatePosted());
        reviewMap.put("reviewText", review.getReviewText());
        reviewMap.put("helpful", review.getHelpful());
        reviewMap.put("companyName", review.getCompanyName());
        String uuid;
        reviewMap.put("workEnvironment", Math.round(review.getWorkEnvironment() * 100.0) / 100.0);
        reviewMap.put("mentorship", Math.round(review.getMentorship() * 100.0) / 100.0);
        reviewMap.put("workload", Math.round(review.getWorkload() * 100.0) / 100.0);
        do {
            uuid = UUID.randomUUID().toString();
        } while (db.collection("reviews").document(uuid).get().isSuccessful());
        reviewMap.put("uuid", uuid);
        Log.d("FirestoreManager", "Adding review with UUID: " + uuid);

        // Add other review fields as needed
        db.collection("reviews").document(review.getReviewTitle()).set(reviewMap)
                .addOnCompleteListener(onCompleteListener);

    }

    public void fetchReviews(String username, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("reviews").whereEqualTo("user", username).get().addOnCompleteListener(onCompleteListener);
    }

    public void deleteReview(String reviewTitle, String requestingUser, OnCompleteListener<Void> onCompleteListener) {
        db.collection("reviews").document(reviewTitle).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String reviewAuthor = task.getResult().getString("user");
                if (reviewAuthor != null && reviewAuthor.equals(requestingUser)) {
                    db.collection("reviews").document(reviewTitle).delete().addOnCompleteListener(onCompleteListener);
                } else {
                    onCompleteListener.onComplete(Tasks.forException(new Exception("User is not the author of the review")));
                }
            } else {
                onCompleteListener.onComplete(Tasks.forException(new Exception("Review does not exist")));
            }
        });

    }

    public void editReview(String reviewTitle, User user, Review review, OnCompleteListener<Void> onCompleteListener) {
        db.collection("reviews").document(reviewTitle).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String reviewAuthor = task.getResult().getString("user");
                if (reviewAuthor != null && reviewAuthor.equals(user.getUsername())) {
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
                    db.collection("reviews").document(reviewTitle).delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            db.collection("reviews").document(review.getReviewTitle()).set(reviewMap)
                                    .addOnCompleteListener(onCompleteListener);
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(deleteTask.getException()));
                        }
                    });
                } else {
                    onCompleteListener.onComplete(Tasks.forException(new Exception("User is not the author of the review")));
                }
            } else {
                onCompleteListener.onComplete(Tasks.forException(new Exception("Review does not exist")));
            }
        });
    }


    public User getUserFromDocument(DocumentSnapshot document) {
        String username = document.getString("username");
        String email = document.getString("email");
        String password = document.getString("password");
        String profileCreated = document.getString("profileCreated");
        return new User(username, email, password, profileCreated);
    }


    public void getCompanyNamesFilter(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("companies")
                .whereGreaterThanOrEqualTo("companyName", name)
                .whereLessThanOrEqualTo("companyName", name + '\uf8ff')
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getCompanyDetailsGivenReview(Review review, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("companies").document(review.getCompanyName()).get().addOnCompleteListener(onCompleteListener);
    }
    public void getCompanyDetailsGivenReview(String UUID, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        //get the company name from the review which has the UUID
        db.collection("reviews").document(UUID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String companyName = task.getResult().getString("companyName");
                db.collection("companies").document(companyName).get().addOnCompleteListener(onCompleteListener);
            } else {
                onCompleteListener.onComplete(Tasks.forException(new Exception("Review does not exist")));
            }
        });
    }

    public void retrieveAllReviewsGivenCompany(String companyName, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("reviews").whereEqualTo("companyName", companyName).get().addOnCompleteListener(onCompleteListener);
    }


    public void getCompanies(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("companies")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getCompanies(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("companies")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public Company getCompanyFromDocument(DocumentSnapshot document) {
        String companyName = document.getString("companyName");
        String companyIndustry = document.getString("companyIndustry");
        String companyLocation = document.getString("companyLocation");
        float rating = Float.parseFloat(String.valueOf(document.getDouble("rating")));
        int image;
        if (document.contains("companyImage") && document.get("companyImage") != null) {
            image = document.getLong("companyImage").intValue(); // Assuming it's stored as a long in Firestore
        } else {
            image = R.drawable.default_company_image; // Replace with your default drawable resource ID
        }
        return new Company(companyIndustry, companyName, image, companyLocation, rating);
    }

    public void deleteUser(String username, OnCompleteListener<Void> onCompleteListener) {
        db.collection("users").document(username)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    public void updateReview(Review review, String oldReviewTitle, OnCompleteListener<Void> onCompleteListener) {
        //delete the old review
        db.collection("reviews").document(oldReviewTitle).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //add the new review
                addReview(review, onCompleteListener);
            } else {
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }


    public Task<DocumentSnapshot> getUserData(String username) {
        return db.collection("users").document(username).get();
    }

    public Task<QuerySnapshot> fetchReviewData(String username) {
        return db.collection("reviews").whereEqualTo("user", username).get();
    }
}
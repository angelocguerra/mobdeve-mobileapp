package com.example.mobdevemobileapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BrevoApi {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            // Replace with your actual Brevo API key
    })
    @POST("smtp/email")
    Call<Void> sendEmail(@Body EmailRequest emailRequest);
}

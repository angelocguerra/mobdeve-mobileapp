package com.example.mobdevemobileapp;

import java.util.List;
import java.util.Map;

public class EmailRequest {
    private Map<String, String> sender;
    private List<Map<String, String>> to;
    private String subject;
    private String htmlContent;

    // Constructor
    public EmailRequest(Map<String, String> sender, List<Map<String, String>> to, String subject, String htmlContent) {
        this.sender = sender;
        this.to = to;
        this.subject = subject;
        this.htmlContent = htmlContent;
    }
}
// Getters
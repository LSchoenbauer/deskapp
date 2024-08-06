package com.example.invoiceclientapp.model;

public class Invoice {
    private String id;
    private String recipientId;
    private String date;
    private String description;
    private String amount;
    private String encodedData;

    // Default constructor (required by Jackson)
    public Invoice() {
    }

    // Parameterized constructor
    public Invoice(String id, String recipientId, String date, String description, String amount, String encodedData) {
        this.id = id;
        this.recipientId = recipientId;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.encodedData = encodedData;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEncodedData() {
        return encodedData;
    }

    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
    }
}

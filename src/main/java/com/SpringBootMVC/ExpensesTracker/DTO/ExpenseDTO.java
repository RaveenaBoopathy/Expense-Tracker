package com.SpringBootMVC.ExpensesTracker.DTO;

import java.time.LocalDateTime;

public class ExpenseDTO {
    private int expenseId;
    private int clientId;
    private String category;
    private int amount;
    private LocalDateTime dateTime;
    private String description;

    public ExpenseDTO() {
    }

    public ExpenseDTO(int expenseId, int clientId, String category, int amount, LocalDateTime dateTime, String description) {
        this.expenseId = expenseId;
        this.clientId = clientId;
        setCategory(category);  // Using the setter for validation
        setAmount(amount);  // Using the setter to apply validation
        setDateTime(dateTime);  // Using the setter for validation
        setDescription(description);  // Using the setter for validation
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            // Optionally, check if category belongs to a predefined list of categories
            // Example: check if it's in an enum or list
            this.category = category.trim();
        } else {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            // Optionally, validate the date (e.g., no future date)
            if (dateTime.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("DateTime cannot be in the future.");
            }
            this.dateTime = dateTime;
        } else {
            throw new IllegalArgumentException("DateTime cannot be null.");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            this.description = description.trim();
        } else {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
    }

    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "expenseId=" + expenseId +
                ", clientId=" + clientId +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                '}';
    }
}
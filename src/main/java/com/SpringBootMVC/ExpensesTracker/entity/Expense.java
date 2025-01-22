package com.SpringBootMVC.ExpensesTracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount")
    private int amount;

    // Change the dateTime to LocalDateTime
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "description", length = 400)
    private String description;

    @Transient
    private String categoryName;

    @Transient
    private String date;

    @Transient
    private String time;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    public Expense() {
    }

    // Updated constructor to handle LocalDateTime
    public Expense(int amount, LocalDateTime dateTime, String description, Client client, Category category) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.description = description;
        this.client = client;
        this.category = category;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Ensure this accepts LocalDateTime directly
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            this.dateTime = dateTime;
        } else {
            throw new IllegalArgumentException("DateTime cannot be null.");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "No Category";
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", categoryName='" + getCategoryName() + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", client=" + client +
                ", category=" + category +
                '}';
    }

    // Method to set formatted date and time from LocalDateTime
    public void setExpenseDateAndTime() {
        if (this.dateTime != null) {
            this.date = dateTime.toLocalDate().toString();
            this.time = dateTime.toLocalTime().toString();
        } else {
            this.date = "Invalid date";
            this.time = "Invalid time";
        }
    }
}

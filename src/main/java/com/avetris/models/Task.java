package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Task {
    @JsonProperty("id")
    private int id = 0;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private double price;
    
    @JsonProperty("count")
    private int count = 1;

    public Task(){}
    
    public Task(String title, String description, double price, int count) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = count;
    }

    public Task(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = 1;
    }

    public Task(int id, String title, String description, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.count = 1;
    }
    
    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void copy(Task newTask) {
        this.title = newTask.getTitle();
        this.description = newTask.getDescription();
        this.price = newTask.getPrice();
        this.count = newTask.getCount();
    }
}

package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.utils.PropertyNames;

public class Task extends AbstractModel {
    @JsonProperty("id")
    private int id = 0;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private float price;    
    
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

    public float getPrice() {
        return price;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        firePropertyChange(PropertyNames.Task.TITLE, oldTitle, title);
    }

    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        firePropertyChange(PropertyNames.Task.DESCRIPTION, oldDescription, description);
    }

    public void setPrice(float price) {
        float oldPrice = this.price;
        this.price = price;
        firePropertyChange(PropertyNames.Task.PRICE, oldPrice, price);
    }
}

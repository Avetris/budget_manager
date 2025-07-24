package com.avetris.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Budget {

    @JsonProperty("id")
    private String id;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("project")
    private String project;

    @JsonProperty("client")
    private Client client = new Client();

    @JsonProperty("iva")
    private int iva = 21;

    @JsonProperty("tasks")
    private List<Task> tasks = new ArrayList<Task>();

    
    public Budget() {
        this.date = LocalDate.now();
    }

    public Budget(String id, String project, LocalDate date, Client client, List<Task> tasks) {
        this.id = id;
        this.project = project;
        this.client = client;
        this.date = date;
        this.tasks = tasks;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public Client getClient() {
        return client;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public int getIva() {
        return iva;
    }

    public double getTotal() {
        double total = 0;
        for(var t : tasks) {
            if (t.getCount() > 0) {
                total += t.getPrice() * t.getCount();
            }
        }
        return total;
    }

    public double getTotalIva() {
        return getTotal() * (iva / 100.0);
    }

    public double getTotalWithIva() {
        return getTotal() + getTotalIva(); 
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void copy(Budget newBudget) {
        this.id = newBudget.getId();
        this.project = newBudget.getProject();
        this.client.copy(newBudget.getClient());
        this.date = newBudget.getDate();
        this.tasks = newBudget.getTasks();
    }
}

package com.avetris.models;

import java.time.LocalDate;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.pdf.PdfManager;

public class Bill {

    @JsonProperty("id")
    private String id;

    @JsonProperty("date")
    private String date;

    @JsonProperty("project")
    private String project;

    @JsonProperty("client")
    private Client client = new Client();

    @JsonProperty("tasks")
    private ArrayList<Task> tasks = new ArrayList<Task>();

    
    public Bill() {
        this.date = LocalDate.now().toString();
    }

    public Bill(String id, String project, String date, Client client) {
        this.id = id;
        this.project = project;
        this.client = client;
        this.date = date;
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
    
    public String getDate() {
        return date;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void generatePDF(String path) {
        PdfManager.createPDF(path, this);
    }

    public void copy(Bill newBill) {
        this.id = newBill.getId();
        this.project = newBill.getProject();
        this.client.copy(client);
        this.date = newBill.getDate();
        this.tasks = newBill.getTasks();
    }
}

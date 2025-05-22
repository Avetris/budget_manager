package com.avetris.models;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.pdf.PdfManager;

public class Bill {

    @JsonProperty("id")
    private String id;

    @JsonProperty("project")
    private String project;

    @JsonProperty("client")
    private Client client = new Client();

    @JsonProperty("date")
    private Date date;

    @JsonProperty("tasks")
    private ArrayList<Task> tasks = new ArrayList<Task>();

    
    public Bill() {
        this.date = new Date();
    }

    public Bill(String id, String project, Client client, Date date) {
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
    
    public Date getDate() {
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDate(Date date) {
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

package com.avetris.models;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.pdf.PdfManager;
import com.avetris.utils.PropertyNames;

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
}

package com.avetris.models;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.pdf.PdfManager;
import com.avetris.utils.PropertyNames;

public class Bill extends AbstractModel{

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
        String oldId = this.id;
        this.id = id;
        firePropertyChange(PropertyNames.Bill.ID, oldId, id);
    }

    public void setProject(String project) {
        String oldProject = this.project;
        this.project = project;
        firePropertyChange(PropertyNames.Bill.PROJECT, oldProject, project);
    }

    public void setClient(Client client) {
        Client oldClient = this.client;
        this.client = client;
        firePropertyChange(PropertyNames.Bill.CLIENT, oldClient, client);
    }

    public void setDate(Date date) {
        Date oldDate = this.date;
        this.date = date;
        firePropertyChange(PropertyNames.Bill.DATE, oldDate, date);
    }

    public void setTasks(ArrayList<Task> tasks) {
        ArrayList<Task> oldTasks = this.tasks;
        this.tasks = tasks;
        firePropertyChange(PropertyNames.Bill.TASKS, oldTasks, tasks);
    }

    public void generatePDF(String path) {
        PdfManager.createPDF(path, this);
    }
}

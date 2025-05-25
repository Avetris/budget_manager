package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Client {
    @JsonProperty("nif")
    private String nif;

    @JsonProperty("isCompany")
    private boolean isCompany;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    public String getNif() {
        return nif;
    }
    
    public boolean isCompany() {
        return isCompany;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Client() {
    }

    public Client(String nif, boolean isCompany, String name, String address){
        this.nif = nif;
        this.isCompany = isCompany;
        this.name = name;
        this.address = address;
    }

    // Setters
    public void setNif(String nif) {
        this.nif = nif;
    }
    public void setCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void copy(Client newClient) {
        this.nif = newClient.getNif();
        this.isCompany = newClient.isCompany();
        this.name = newClient.getName();
        this.address = newClient.getAddress();
    }

    public boolean containsFilter(String filter) {
        return getName().contains(filter) ||  getNif().contains(filter);
    }
}

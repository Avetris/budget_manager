package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Client {
    @JsonProperty("nif")
    private String nif;

    @JsonProperty("dni")
    private String dni;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    public String getNif() {
        return nif;
    }
    
    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Client() {
    }

    public Client(String nif, String dni, String name, String address){
        this.nif = nif;
        this.dni = dni;
        this.name = name;
        this.address = address;
    }

    // Setters
    public void setNif(String nif) {
        this.nif = nif;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void copy(Client newClient) {
        this.nif = newClient.getNif();
        this.dni = newClient.getDni();
        this.name = newClient.getName();
        this.address = newClient.getAddress();
    }

    public boolean containsFilter(String filter) {
        return getName().contains(filter) || getDni().contains(filter) || getNif().contains(filter);
    }
}

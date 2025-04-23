package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.utils.PropertyNames;

public class Client extends AbstractModel {
    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("nif")
    private String nif;

    @JsonProperty("email")
    private String email;
    
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getNif() {
        return nif;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange(PropertyNames.Client.NAME, oldName, name);
    }

    public void setAddress(String address) {
        String oldAddress = this.address;
        this.address = address;
        firePropertyChange(PropertyNames.Client.ADDRESS, oldAddress, address);
    }

    public void setPhone(String phone) {
        String oldPhone = this.phone;
        this.phone = phone;
        firePropertyChange(PropertyNames.Client.PHONE, oldPhone, phone);
    }

    public void setNif(String nif) {
        String oldNif = this.nif;
        this.nif = nif;
        firePropertyChange(PropertyNames.Client.NIF, oldNif, nif);
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        firePropertyChange(PropertyNames.Client.EMAIL, oldEmail, email);
    }
}

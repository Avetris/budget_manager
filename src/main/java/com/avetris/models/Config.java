package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.utils.PropertyNames;

public class Config {
    @JsonProperty("web")
    private String web;
    
    @JsonProperty("logo")
    private String logo;

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
    
    @JsonProperty("info")
    private String[] info;
    
    @JsonProperty("conditions")
    private String conditions;

    @JsonProperty("garanty")
    private String garanty;
    
    // Getters
    public String getWeb() {
        return web;
    }
    
    public String getLogo() {
        return logo;
    }

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

    public String[] getInfo() {
        return info;
    }

    public String getConditions() {
        return conditions;
    }

    public String getGaranty() {
        return garanty;
    }

    // Setters    
    public void setWeb(String web) {
        this.web = web;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public void setGaranty(String garanty) {
        this.garanty = garanty;
    }
}

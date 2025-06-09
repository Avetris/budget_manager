package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Config {
    @JsonProperty("web")
    private String web;

    @JsonProperty("name")
    private String name;

    @JsonProperty("street")
    private String street;

    @JsonProperty("ciudad")
    private String city;

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
    public Config() {}

    public Config(String name, String street, String city, String web, String phone, String nif, String email, String[] info, String conditions, String garanty) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.web = web;
        this.phone = phone;
        this.nif = nif;
        this.email = email;
        this.info = info;
        this.conditions = conditions;
        this.garanty = garanty;
    }


    // Getters
    public String getWeb() {
        return web;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
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
        if(info == null) {
            return new String[0];
        }
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

    public void setName(String name) {
        this.name = name;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
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

package com.avetris.models;

import org.codehaus.jackson.annotate.JsonProperty;

import com.avetris.utils.PropertyNames;

public class Config extends AbstractModel {
    @JsonProperty("web")
    private String web;
    
    @JsonProperty("icon")
    private String icon;

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
    
    public String getIcon() {
        return icon;
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
        String oldWeb = this.web;
        this.web = web;
        firePropertyChange(PropertyNames.Config.WEB, oldWeb, web);
    }

    public void setIcon(String icon) {
        String oldIcon = this.icon;
        this.icon = icon;
        firePropertyChange(PropertyNames.Config.Icon, oldIcon, icon);
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange(PropertyNames.Config.NAME, oldName, name);
    }

    public void setAddress(String address) {
        String oldAddress = this.address;
        this.address = address;
        firePropertyChange(PropertyNames.Config.ADDRESS, oldAddress, address);
    }

    public void setPhone(String phone) {
        String oldPhone = this.phone;
        this.phone = phone;
        firePropertyChange(PropertyNames.Config.PHONE, oldPhone, phone);
    }

    public void setNif(String nif) {
        String oldNif = this.nif;
        this.nif = nif;
        firePropertyChange(PropertyNames.Config.NIF, oldNif, nif);
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        firePropertyChange(PropertyNames.Config.EMAIL, oldEmail, email);
    }

    public void setInfo(String[] info) {
        String[] oldInfo = this.info;
        this.info = info;
        firePropertyChange(PropertyNames.Config.INFO, oldInfo, info);
    }

    public void setConditions(String conditions) {
        String oldConditions = this.conditions;
        this.conditions = conditions;
        firePropertyChange(PropertyNames.Config.CONDITIONS, oldConditions, conditions);
    }

    public void setGaranty(String garanty) {
        String oldGaranty = this.garanty;
        this.garanty = garanty;
        firePropertyChange(PropertyNames.Config.GARANTY, oldGaranty, garanty);
    }
}

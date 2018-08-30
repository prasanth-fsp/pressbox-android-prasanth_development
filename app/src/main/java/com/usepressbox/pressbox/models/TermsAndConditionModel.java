package com.usepressbox.pressbox.models;

/**
 * Created by kruno on 11.05.16..
 * This model class is used to set and get the terms and conditions
 */
public class TermsAndConditionModel {

    private String header;
    private String description;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

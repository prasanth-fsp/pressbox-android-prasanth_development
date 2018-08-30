package com.usepressbox.pressbox.models;

/**
 * Created by Prasanth.S on 8/21/2018.
 */
public class LocationModel {
    private String location_id;
    private String companyName;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
    private String lat;
    private String lng;
    private String serviceType;
    private String public_type;
    private String locationType;
    private String isHomeDelivery;


    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPublic_type() {
        return public_type;
    }

    public void setPublic_type(String public_type) {
        this.public_type = public_type;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getIsHomeDelivery() {
        return isHomeDelivery;
    }

    public void setIsHomeDelivery(String isHomeDelivery) {
        this.isHomeDelivery = isHomeDelivery;
    }
}

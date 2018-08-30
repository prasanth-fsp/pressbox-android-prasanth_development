package com.usepressbox.pressbox.models;

/**
 * Created by kruno on 18.05.16..
 * This model class is used to set and get the placed order values
 */
public class GetOrdersModel {
    private String date;
    private String address;
    private String lockerId;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLockerId() {
        return lockerId;
    }

    public void setLockerId(String lockerId) {
        this.lockerId = lockerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

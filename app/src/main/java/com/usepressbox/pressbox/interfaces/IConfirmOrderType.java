package com.usepressbox.pressbox.interfaces;

import com.usepressbox.pressbox.models.LocationModel;

import java.util.ArrayList;

/**
 * Created by Prasanth.S on 8/20/2018.
 */
public interface IConfirmOrderType {
    void addressMatchCase(String value,LocationModel locationModel);
    void promoCodeStatus(String status,String message);
    void nearByLocations(ArrayList<LocationModel> locationModels);
    void updateUI(LocationModel locationModel);
    void updateLockerNumber(String value);
}

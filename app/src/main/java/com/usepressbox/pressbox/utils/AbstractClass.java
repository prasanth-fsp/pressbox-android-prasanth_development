package com.usepressbox.pressbox.utils;

import android.content.Context;

import com.usepressbox.pressbox.interfaces.IConfirmOrderType;
import com.usepressbox.pressbox.models.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Prasanth.S on 8/23/2018.
 */
public class AbstractClass {



    public void saveLocationData(Context context, JSONObject jsonObject, String from, IConfirmOrderType iConfirmOrderType,String nearBylocation) {
        switch (from) {
            case "getOrderType":

                boolean isAddressMachCase = true;

                try {
                    JSONArray locationArray = jsonObject.optJSONArray("locations");

                    ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();
                    for (int i = 0; i < locationArray.length(); i++) {
                        JSONObject locationObject = locationArray.optJSONObject(i).optJSONObject("location");

                        LocationModel locationModel=saveLocationModel(locationObject);

                        if(nearBylocation == null || nearBylocation.equalsIgnoreCase("null")) {
                            if (new SessionManager(context).getUserAddres().toLowerCase().contains(locationObject.optString("address").toLowerCase())) {
//                                if ("30 Park Avenue".toLowerCase().contains(locationObject.optString("address").toLowerCase())) {
                                iConfirmOrderType.addressMatchCase("true", locationModel);
                                isAddressMachCase = false;
                                break;
                            }
                        }
                        else{
                            if (nearBylocation.toLowerCase().contains(locationObject.optString("address").toLowerCase())) {
//                                if ("30 Park Avenue".toLowerCase().contains(locationObject.optString("address").toLowerCase())) {
                                iConfirmOrderType.addressMatchCase("true", locationModel);
                                isAddressMachCase = false;
                                break;
                            }
                        }
                        locationModelArrayList.add(locationModel);

                    }
                    /*UpdateUI*/
                    if (isAddressMachCase)
                        iConfirmOrderType.addressMatchCase("false", null);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case "getNearByLocation":

                try {
                    JSONArray locationJsonArray = jsonObject.optJSONObject("data").optJSONArray("locations");

                    ArrayList<LocationModel> locationModelsList = new ArrayList<>();
                    for (int i = 0; i < locationJsonArray.length(); i++) {
                        JSONObject locationObject = locationJsonArray.optJSONObject(i).optJSONObject("location");
                        LocationModel locationModel=saveLocationModel(locationObject);
                        locationModelsList.add(locationModel);
                    }

                    /*UpdateUI*/
                    iConfirmOrderType.nearByLocations(locationModelsList);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;


            case "getLockerNumber":

                try {
                    ArrayList<JSONObject> jsonObjects=new ArrayList<>();
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        if(key.equalsIgnoreCase("status"))
                            continue;

                        JSONObject value = jsonObject.optJSONObject(key);
                        jsonObjects.add(value);
                    }
                    String lockerName= null;
                    for (int i = 0; i <jsonObjects.size()    ; i++) {
                        JSONObject lockerObject = jsonObjects.get(i);
                        lockerName=lockerObject.optString("lockerName");
                    }
                    /*UpdateUI*/
                    iConfirmOrderType.updateLockerNumber(lockerName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private LocationModel saveLocationModel(JSONObject locationObject) {
        LocationModel locationModel = null;
        try {
            locationModel = new LocationModel();
            locationModel.setLocation_id(locationObject.optString("location_id"));
            locationModel.setCompanyName(locationObject.optString("companyName"));
            locationModel.setAddress(locationObject.optString("address"));
            locationModel.setAddress2(locationObject.optString("address2"));
            locationModel.setCity(locationObject.optString("city"));
            locationModel.setState(locationObject.optString("state"));
            locationModel.setZipcode(locationObject.optString("zipcode"));
            locationModel.setLat(locationObject.optString("lat"));
            locationModel.setLng(locationObject.optString("lng"));
            locationModel.setServiceType(locationObject.optString("serviceType"));
            locationModel.setPublic_type(locationObject.optString("public"));
            locationModel.setLocationType(locationObject.optString("locationType"));
            locationModel.setIsHomeDelivery(String.valueOf(locationObject.optBoolean("isHomeDelivery")));
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return locationModel;
    }
}

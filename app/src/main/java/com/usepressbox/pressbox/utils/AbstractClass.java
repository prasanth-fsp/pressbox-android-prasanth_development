package com.usepressbox.pressbox.utils;

import android.content.Context;

import com.usepressbox.pressbox.interfaces.IConfirmOrderTypeListener;
import com.usepressbox.pressbox.interfaces.ISignUpListener;
import com.usepressbox.pressbox.models.LocationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Prasanth.S on 8/23/2018.
 */
public class AbstractClass {


    public void saveLocationData(Context context, JSONObject jsonObject, String from, IConfirmOrderTypeListener iConfirmOrderType, ISignUpListener iSignUpListener, String nearBylocation, String fromScreen) {
        switch (from) {
            case "getOrderType":

                boolean isAddressMachCase = true;

                SessionManager sessionManager = new SessionManager(context);
                try {
                    JSONArray locationArray = jsonObject.optJSONArray("locations");

                    ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();
                    for (int i = 0; i < locationArray.length(); i++) {
                        JSONObject locationObject = locationArray.optJSONObject(i).optJSONObject("location");

                        LocationModel locationModel = saveLocationModel(locationObject);

                        if (nearBylocation == null || nearBylocation.equalsIgnoreCase("null")) {
                            if (sessionManager.getUserAddress() != null
                                    && sessionManager.getUserShortAddress() != null) {

                                /*Comparing user address with API address*/
                                if ((sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("address").toLowerCase())
                                        || (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("address").toLowerCase())))
                                        && ((sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("city").toLowerCase()))
                                        || (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("city").toLowerCase())))
                                        && ((sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("state").toLowerCase()))
                                        || (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("state").toLowerCase())))) {

                                    if(fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                                        iConfirmOrderType.addressMatchCase("true", locationModel);
                                        isAddressMachCase = false;
                                    }else {
                                        iSignUpListener.addressMatchCase("true", locationModel);
                                        isAddressMachCase = false;
                                    }
                                    break;
                                }

                            }
                            else {
                                if(sessionManager.getUserShortAddress() != null) {
                                    if (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("address").toLowerCase())
                                            && (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("city").toLowerCase()))
                                            && (sessionManager.getUserShortAddress().toLowerCase().contains(locationObject.optString("state").toLowerCase()))) {

                                        if(fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                                            iConfirmOrderType.addressMatchCase("true", locationModel);
                                            isAddressMachCase = false;
                                        }else {
                                            iSignUpListener.addressMatchCase("true", locationModel);
                                            isAddressMachCase = false;
                                        }
                                        break;
                                    }
                                }else {
                                    if (sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("address").toLowerCase())
                                            && (sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("city").toLowerCase()))
                                            && (sessionManager.getUserAddress().toLowerCase().contains(locationObject.optString("state").toLowerCase()))) {

                                        if(fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                                            iConfirmOrderType.addressMatchCase("true", locationModel);
                                            isAddressMachCase = false;
                                        }else {
                                            iSignUpListener.addressMatchCase("true", locationModel);
                                            isAddressMachCase = false;
                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (nearBylocation.toLowerCase().contains(locationObject.optString("address").toLowerCase())
                                    && (nearBylocation.toLowerCase().contains(locationObject.optString("city").toLowerCase()))
                                    && (nearBylocation.toLowerCase().contains(locationObject.optString("state").toLowerCase()))) {

                                if(fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                                    iConfirmOrderType.addressMatchCase("true", locationModel);
                                    isAddressMachCase = false;
                                }else {
                                    iSignUpListener.addressMatchCase("true", locationModel);
                                    isAddressMachCase = false;
                                }
                                break;
                            }
                        }
                        locationModelArrayList.add(locationModel);

                    }
                    /*UpdateUI*/
                    if (isAddressMachCase) {
                        if (fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                            iConfirmOrderType.addressMatchCase("false", null);
                        } else {
                            iSignUpListener.addressMatchCase("false", null);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (fromScreen.equalsIgnoreCase("NewLockerFragment")) {
                        iConfirmOrderType.addressMatchCase("false", null);
                    } else {
                        iSignUpListener.addressMatchCase("false", null);
                    }
//                    iConfirmOrderType.addressMatchCase("false", null);
//                    UtilityClass.showAlertWithOk(context, "Alert!", "Please try again", "place-order");
                }

                break;
            case "getNearByLocation":

                try {
                    JSONArray locationJsonArray = jsonObject.optJSONObject("data").optJSONArray("locations");

                    ArrayList<LocationModel> locationModelsList = new ArrayList<>();
                    for (int i = 0; i < locationJsonArray.length(); i++) {
                        JSONObject locationObject = locationJsonArray.optJSONObject(i).optJSONObject("location");
                        if(locationObject.has("locationType")) {
                            if(locationObject.optString("locationType").equalsIgnoreCase("Lockers") ||
                            (locationObject.optString("locationType").equalsIgnoreCase("Kiosk"))){
                                LocationModel locationModel = saveLocationModel(locationObject);
                                locationModelsList.add(locationModel);
                            }
                        }
                    }

                    /*UpdateUI*/
                    iConfirmOrderType.nearByLocations(locationModelsList);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;


            case "getLockerNumber":

                try {
                    ArrayList<JSONObject> jsonObjects = new ArrayList<>();
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        if (key.equalsIgnoreCase("status"))
                            continue;

                        JSONObject value = jsonObject.optJSONObject(key);
                        jsonObjects.add(value);
                    }
                    String lockerName = null;
                    for (int i = 0; i < jsonObjects.size(); i++) {
                        JSONObject lockerObject = jsonObjects.get(i);
                        if(i == 0) {
                            lockerName = lockerObject.optString("lockerName");
                        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationModel;
    }
}

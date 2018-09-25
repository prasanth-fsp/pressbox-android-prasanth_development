package com.usepressbox.pressbox.models;

import android.content.Context;

import com.usepressbox.pressbox.utils.ApiUrlGenerator;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.Signature;

import java.util.HashMap;

/**
 * Created by kruno on 21.04.16..
 * This model class is used to set and get the placed order values with params
 */
public class Order {

    private String lockerName;
    private String orderNotes;
    private String orderType;


    public Order() {

        this.lockerName = "";
        this.orderNotes = "";
        this.orderType = "";

    }

    public Order(String lockerName, String orderNotes, String orderType) {

        this.lockerName = lockerName;
        this.orderNotes = orderNotes;
        this.orderType = orderType;

    }

    public String getLockerName() {
        return lockerName;
    }

    public void setLockerName(String lockerName) {
        this.lockerName = lockerName;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public ApiCallParams claimsCreate(Context context) {

        String endpoint = "claims/create";
        String url = "http://droplocker.com/api/v2_0/claims/create";
//        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("lockerName", new SessionManager(context).getLockerNumber());
        namevaluepair.put("orderType".trim(), orderType);
        namevaluepair.put("orderNotes", orderNotes);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }


    public ApiCallParams getClaims(Context context) {
        /*Please dont touch this before confirming from Architect*/
        String url = "http://droplocker.com/api/v2_0/customers/getClaims";

        String endpoint = "customers/getClaims";

//        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));
        return new ApiCallParams(namevaluepair, url, endpoint);
    }
    public ApiCallParams getOrders(Context context) {
        String url = "http://droplocker.com/api/v2_0/customers/getOrders";

        String endpoint = "customers/getOrders";

//        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));
        return new ApiCallParams(namevaluepair, url, endpoint);
    }


    public ApiCallParams getServiceType(Context context) {

        String endpoint = "businesses/get_serviceTypes";

        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));
        return new ApiCallParams(namevaluepair, url, endpoint);
    }


/*    public ApiCallParams confirmOrderType(Context context) {

        String endpoint = "locations/search";

        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", Constants.TOKEN);
        params.put("address", new SessionManager(context).getUserAddress());
        params.put("geolocation", new SessionManager(context).getUserGeoLocation());

//        params.put("address","30 Park Avenue");
//        params.put("geolocation", "41.8829607, -87.63414829999999");

//        params.put("address", "West 2nd Street, KK Nagar, Mellur, Tallakulam, Madurai, Tamil Nadu 625002");
//        params.put("geolocation", "9.936859, 78.140233");

        params.put("sessionToken", new SessionManager(context).getSessionToken());
//        params.put("business_id", Constants.BUSINESS_ID);
        params.put("signature", Signature.getUrlConversion(params));
        return new ApiCallParams(params, url, endpoint);
    }*/

    public ApiCallParams confirmOrderType() {

        String endpoint = "locations/search";

        String url = ApiUrlGenerator.getApiUrl(endpoint);


        return new ApiCallParams(null, url, endpoint);
    }

    public ApiCallParams getLockerType() {

        String endpoint = "lockers/get";

        String url = ApiUrlGenerator.getApiUrl(endpoint);


        return new ApiCallParams(null, url, endpoint);
    }

    public ApiCallParams getNearByLocation() {

        String endpoint = "locations/get_by_distance_for_business";

        String url = ApiUrlGenerator.getApiUrl(endpoint);


        return new ApiCallParams(null, url, endpoint);
    }
}

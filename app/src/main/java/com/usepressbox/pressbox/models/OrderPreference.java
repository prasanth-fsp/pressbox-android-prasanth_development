package com.usepressbox.pressbox.models;

import android.content.Context;

import com.usepressbox.pressbox.utils.ApiUrlGenerator;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.Signature;

import java.util.HashMap;

/**
 * Created by kruno on 27.04.16..
 * This model class is used to set and get the orderpreference values
 */
public class OrderPreference {

    private String detergentID;
    private String dryerSheetID;
    private String fabricSoftnerID;

    public String getFabricSoftnerID() {
        return fabricSoftnerID;
    }

    public void setFabricSoftnerID(String fabricSoftnerID) {
        this.fabricSoftnerID = fabricSoftnerID;
    }

    public String getDryerSheetID() {
        return dryerSheetID;
    }

    public void setDryerSheetID(String dryerSheetID) {
        this.dryerSheetID = dryerSheetID;
    }

    public String getDetergentID() {
        return detergentID;
    }

    public void setDetergentID(String detergentID) {
        this.detergentID = detergentID;
    }

    public OrderPreference() {

        this.detergentID = "3616";
        this.dryerSheetID = "";
        this.fabricSoftnerID = "";
    }

    public ApiCallParams laundryPreferences(Context context){

        String endpoint= "customers/laundryPreferences";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams getBusinessSettings(Context context){

        String endpoint= "businesses/settings";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }


    public ApiCallParams updateDryerSheet(Context context){

        String endpoint= "customers/updateLaundryPreference";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("productCategory_slug", "dryersheets");
        namevaluepair.put("productID", dryerSheetID);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams updateFabricSoftner(Context context){

        String endpoint= "customers/updateLaundryPreference";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("productCategory_slug", "fabsoft");
        namevaluepair.put("productID", fabricSoftnerID);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams updateDetergent(Context context){

        String endpoint= "customers/updateLaundryPreference";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("productCategory_slug", "detergent");
        namevaluepair.put("productID", detergentID);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }
}

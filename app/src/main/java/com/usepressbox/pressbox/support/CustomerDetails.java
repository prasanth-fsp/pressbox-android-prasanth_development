package com.usepressbox.pressbox.support;

import com.usepressbox.pressbox.models.OrderPreference;
import com.usepressbox.pressbox.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kruno on 06.05.16..
 * This class is used to set and get customer details with params
 */
public class CustomerDetails {

    private JSONObject jsonObject;

    public CustomerDetails(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        getCustomerObject();
        setOrderPreferences();
        setCreditCardLastFourNumber();
    }

    public void getCustomerObject(){

        JSONObject customerObject = null;
        try {
            customerObject = jsonObject.getJSONObject("data").getJSONObject("customerDetails");
            SessionManager.CUSTOMER.setName(customerObject.getString("firstName"));
            SessionManager.CUSTOMER.setLastName(customerObject.getString("lastName"));
            SessionManager.CUSTOMER.setEmail(customerObject.getString("email"));
            SessionManager.CUSTOMER.setCity(customerObject.getString("city"));
            SessionManager.CUSTOMER.setPhone(customerObject.getString("phone"));
            SessionManager.CUSTOMER.setStarchOnShirtsId(customerObject.getString("starchOnShirts_id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOrderPreferences(){

        SessionManager.ORDER_PREFERENCE = new OrderPreference();

        JSONObject customerPreferencesObject = null;
        try {
            customerPreferencesObject = jsonObject.getJSONObject("data").getJSONObject("customerPreferences");
            Object check = customerPreferencesObject.get("preferences");

            if (check instanceof JSONObject) {
                if (customerPreferencesObject.has("preferences")) {
                    JSONObject preferences = customerPreferencesObject.getJSONObject("preferences");

                    if (preferences.has("dryersheet")) {
                        SessionManager.ORDER_PREFERENCE.setDryerSheetID(preferences
                                .getJSONObject("dryersheet").getString("productID"));
                    }

                    if (preferences.has("fabsoft")) {
                        SessionManager.ORDER_PREFERENCE.setFabricSoftnerID(preferences
                                .getJSONObject("fabsoft").getString("productID"));
                    }

                    if (preferences.has("detergent")) {
                        SessionManager.ORDER_PREFERENCE.setDetergentID(preferences
                                .getJSONObject("detergent").getString("productID"));
                    }
                }
            }


            customerPreferencesObject = jsonObject.getJSONObject("data").getJSONObject("customerDetails");
            if(customerPreferencesObject.has("starchOnShirts_id"))
            {
                SessionManager.CUSTOMER.setStarchOnShirtsId(customerPreferencesObject.getString("starchOnShirts_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void setCreditCardLastFourNumber(){
        JSONObject creditCard = null;
        try {
            creditCard = jsonObject.getJSONObject("data").getJSONObject("creditCard");
            if (creditCard.getString("lastFourCardNumber")!= null){
                if (creditCard.getString("lastFourCardNumber").equals("null")){
                    SessionManager.CUSTOMER.setCardNumber("0000");
                }else{
                    SessionManager.CUSTOMER.setCardNumber(creditCard.getString("lastFourCardNumber"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

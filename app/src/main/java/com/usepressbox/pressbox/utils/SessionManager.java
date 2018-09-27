package com.usepressbox.pressbox.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.models.OrderPreference;

/**
 * Created by kruno on 13.04.16..
 * This class handles the session management of existing user
 */
public class SessionManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
    }

    public void saveSessionToken(String sessionToken){
        sharedPreferences.edit().putString("sessionToken", sessionToken).commit();
    }

    public String getSessionToken() {
        return sharedPreferences.getString("sessionToken", "");
    }

    public void saveUserName(String s){
        sharedPreferences.edit().putString("userName", s).commit();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", "");
    }

    public void savePassword(String s){
        sharedPreferences.edit().putString("password", s).commit();
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public void saveSessionTokenExpiryDate(String s){
        sharedPreferences.edit().putString("sessionTokenExpiryDate", s).commit();
    }

    public String getSessionTokenExpiryDate() {
        return sharedPreferences.getString("sessionTokenExpiryDate", "");
    }

    public void saveBussinesId(String s){
        sharedPreferences.edit().putString("bussinesID", s).commit();
    }

    public String getBussinesId(){
        return sharedPreferences.getString("bussinesID", "");
    }


    public void savePercentage(String s){
        sharedPreferences.edit().putString("notificationPercent", s).commit();
    }

    public String getPerentage() {
        return sharedPreferences.getString("notificationPercent", "");
    }
    public void saveCode(String s){
        sharedPreferences.edit().putString("notificationCode", s).commit();
    }

    public String getCode() {
        return sharedPreferences.getString("notificationCode", "");
    }

    public void saveUserAddress(String s){
        sharedPreferences.edit().putString("userAddress", s).commit();
    }
    public String getUserAddress() {
        return sharedPreferences.getString("userAddress", null);
    }

    public String getUserShortAddress() {
        return sharedPreferences.getString("userShortAddress", null);
    }


    public void saveUserShortAddress(String s){
        sharedPreferences.edit().putString("userShortAddress", s).commit();
    }


    public void saveUserGeoLocation(String s){
        sharedPreferences.edit().putString("UserGeoLocation", s).commit();
    }

    public String getUserGeoLocation() {
        return sharedPreferences.getString("UserGeoLocation", null);
    }


    public void saveLocationId(String s){
        sharedPreferences.edit().putString("LocationId", s).commit();
    }

    public String getUserLocationId() {
        return sharedPreferences.getString("LocationId", null);
    }

    public Boolean getOnboardStatus() {
        return sharedPreferences.getBoolean("isFirst", false);
    }

    public void SetOnboard(Boolean s){
        sharedPreferences.edit().putBoolean("isFirst", s).commit();
    }

    public void saveLockerNumber(String s){
        sharedPreferences.edit().putString("LockerNumber", s).commit();
    }

    public String getLockerNumber() {
        return sharedPreferences.getString("LockerNumber", null);
    }


    public String getUserFlow() {
        return sharedPreferences.getString("userFlow", null);
    }


    public void saveUserFlow(String s){
        sharedPreferences.edit().putString("userFlow", s).commit();
    }

    public String getCity() {
        return sharedPreferences.getString("serviceAvailableCity", null);
    }


    public void saveCity(String s){
        sharedPreferences.edit().putString("serviceAvailableCity", s).commit();
    }
    @SuppressLint("CommitPrefEdits")
    public void clearSession(){

        savePassword("");
        saveUserName("");
        saveSessionToken("");
        SessionManager.CUSTOMER = null;
        SessionManager.ORDER_PREFERENCE = null;
        sharedPreferences.edit().clear().apply();
    }



    public static Customer CUSTOMER;
    public static Order ORDER;
    public static OrderPreference ORDER_PREFERENCE;
}

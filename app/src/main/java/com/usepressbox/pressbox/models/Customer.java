package com.usepressbox.pressbox.models;

import android.content.Context;

import com.usepressbox.pressbox.utils.ApiUrlGenerator;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.Signature;

import java.util.HashMap;

/**
 * Customer is the end user of the mobile app.
 */
public class Customer {


    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String promoCode;
    private int id;
    private String address;

    private String starchOnShirtsId = "1";
    private String expYear;
    private String expMonth;
    private String cardNumber;
    private String csc;
    private String streetAddress;
    private String streetLongAddress;
    private String userCity;
    private String state;
    private String country;
    private String zipcode;

    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStarchOnShirtsId() {
        return starchOnShirtsId;
    }

    public void setStarchOnShirtsId(String starchOnShirtsId) {
        this.starchOnShirtsId = starchOnShirtsId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {

        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetLongAddress() {
        return streetLongAddress;
    }

    public void setStreetLongAddress(String streetLongAddress) {
        this.streetLongAddress = streetLongAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public Customer() {
        this.name = "";
        this.lastName = "";
        this.email = "";
        this.phone = "";
        this.password = "";
        this.city = "";
        this.id = -1;
        this.promoCode = "";
    }

    public Customer(String name, String lastName, String email, String phone, String password, String city, int id, String promoCode) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.city = city;
        this.id = id;
        this.promoCode = promoCode;
    }


    public ApiCallParams create() {

        String endpoint = "customers/create";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("email", email);
        namevaluepair.put("password", password);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams updateProfile(Context context) throws Exception {

        String endpoint = "customers/updateProfile";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("firstName", name);
        namevaluepair.put("lastName", lastName);
        namevaluepair.put("phone", phone);
        namevaluepair.put("city", city);
        namevaluepair.put("starchOnShirts_id", starchOnShirtsId);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams updateUSerAddress(Context context) throws Exception {

        String endpoint = "customers/updateProfile";
        String url = null;
        HashMap<String, String> namevaluepair = null;
        try {
            url = ApiUrlGenerator.getApiUrl(endpoint);

            namevaluepair = new HashMap<String, String>();
            namevaluepair.put("token", Constants.TOKEN);
            namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
            if(streetAddress!= null)
                namevaluepair.put("address1", streetAddress);

            if(streetLongAddress != null)
                namevaluepair.put("address2", streetLongAddress);

            if(userCity != null)
            namevaluepair.put("city", userCity);

            if(state != null)
            namevaluepair.put("state", state);

            if(zipcode != null)
            namevaluepair.put("zip", zipcode);


            if(name != null)
            namevaluepair.put("firstName", name);

            if(lastName != null)
            namevaluepair.put("lastName", lastName);

            if(phone != null)
            namevaluepair.put("phone", phone);

            namevaluepair.put("starchOnShirts_id", starchOnShirtsId);
            namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams validate() {

        String endpoint = "customers/validate";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("username", email);
        namevaluepair.put("password", password);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams sendForgotPasswordEmail() {

        String endpoint = "customers/sendForgotPasswordEmail";

        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("email", email);
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams details(Context context) {

        String endpoint = "customers/details";

        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams updateBillling(Context context) {

        String endpoint = "customers/updateBilling";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("kiosk_access", "1");
        namevaluepair.put("cardNumber", cardNumber);
        namevaluepair.put("expMonth", expMonth);
        namevaluepair.put("expYear", expYear);
        namevaluepair.put("csc", csc);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));


        return new ApiCallParams(namevaluepair, url, endpoint);
    }

    public ApiCallParams savePromoCode(Context context) {

        String endpoint = "customers/addCoupon";

        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> namevaluepair = new HashMap<String, String>();
        namevaluepair.put("token", Constants.TOKEN);
        namevaluepair.put("promoCode", promoCode);
        namevaluepair.put("sessionToken", new SessionManager(context).getSessionToken());
        namevaluepair.put("business_id", Constants.BUSINESS_ID);
        namevaluepair.put("signature", Signature.getUrlConversion(namevaluepair));

        return new ApiCallParams(namevaluepair, url, endpoint);
    }


}

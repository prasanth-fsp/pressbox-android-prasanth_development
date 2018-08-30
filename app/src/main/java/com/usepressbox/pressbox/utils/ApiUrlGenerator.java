package com.usepressbox.pressbox.utils;

/**
 * Created by kruno on 12.04.16..
 */
public class ApiUrlGenerator {

    private static boolean isInTestMode =false;
    public static String BASE_URL_PRODUCTION = "http://droplocker.com/api/v2_2/";
    public static String BASE_URL_TEST = "http://mobilesandbox.droplocker.com/api/v2_2/";

    public static String getApiUrl(String endpoint) {

        if (endpoint == "") try {
            throw new Exception("endpoint can not be empty string!");
        } catch (Exception e) {
            e.printStackTrace();
            //TODO notiffy user that something went wrong
        }
        if (isInTestMode) return BASE_URL_TEST + endpoint;
        else return BASE_URL_PRODUCTION + endpoint;
    }
}

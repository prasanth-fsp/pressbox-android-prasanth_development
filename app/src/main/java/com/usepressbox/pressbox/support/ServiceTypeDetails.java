package com.usepressbox.pressbox.support;


import com.usepressbox.pressbox.interfaces.ISelectServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Diliban on 3/19/2018.
 *This class handles retrieving the data of ordertypes from the api
 */

public class ServiceTypeDetails {

    private JSONObject jsonObject;
    private ArrayList<HashMap<String, String>> select_params = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> options = new HashMap<String, String>();
    private ArrayList<String> selectoptions = new ArrayList<String>();
    private ISelectServiceListener iSelectServiceListener;
    Boolean otherType = false;

    public ServiceTypeDetails(JSONObject jsonObject, ISelectServiceListener iSelectServiceListener) {
        this.jsonObject = jsonObject;
        this.iSelectServiceListener = iSelectServiceListener;
        getServiceTypeObject();

    }

    public void getServiceTypeObject() {
        JSONObject servicetypeObject = null;
        try {
            servicetypeObject = jsonObject.getJSONObject("data");
            Iterator<?> keys = servicetypeObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String keyvalue = servicetypeObject.get(key).toString();

                selectoptions.add(keyvalue);
                options.put(key, keyvalue);


            }
            select_params.add(options);

            if (selectoptions.contains("Other")) {
                otherType = true;
                selectoptions.remove("Other");
            } else {
                otherType = false;
            }

            iSelectServiceListener.selectServiceData(selectoptions, select_params, otherType);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}

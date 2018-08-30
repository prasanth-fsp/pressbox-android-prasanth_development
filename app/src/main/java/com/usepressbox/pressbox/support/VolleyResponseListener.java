package com.usepressbox.pressbox.support;

/**
 * Created by kruno on 12.04.16..
 * This class is used as listner for volley response
 */
public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(String response);
}

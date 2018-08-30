package com.usepressbox.pressbox.models;

import java.util.HashMap;

/**
 * Created by kruno on 12.04.16..
 */
public class ApiCallParams {

    private HashMap<String, String> params;
    private String url;
    private String tag;

    public String getTag() {
        return tag;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public ApiCallParams(HashMap<String, String> params, String url, String tag) {

        this.params = params;
        this.url = url;
        this.tag = tag;
    }
}

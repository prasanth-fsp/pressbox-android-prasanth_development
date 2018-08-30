package com.usepressbox.pressbox.support;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by kruno on 12.04.16..
 * This class is used to handle the server response
 */
public class ServerResponse {
    public static final String tag = "ServerResponse";
    private static Activity context;
    private static String url, result;

    private JSONObject jObject = null;

    private static StringRequest jsonObjectRequest;

    private static String requestString = "fail";

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    public ServerResponse(String url) {
        this.url = url;
    }


    public static String getJSONObjectfromURL(RequestType requestType, final Map<String, String> params, final Context context, final VolleyResponseListener listener) {
        final ProgressDialog progressDialog  = new ProgressDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context);

        if (requestType == RequestType.GET) {
            jsonObjectRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            listener.onResponse(response);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null){
                                switch(response.statusCode){
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        listener.onError(error.toString());
                                        break;

                                    case 401:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        listener.onError(error.toString());
                                        break;
                                }
                            }
                        }
                    }) {

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));

                        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }
        if (requestType == RequestType.POST)
        {


            jsonObjectRequest = new StringRequest
                    (Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response)
                        {

                            progressDialog.dismiss();

                            listener.onResponse(response);
                            requestString = "success";

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                            listener.onError("network not available");
                        }
                    }) {

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                @Override
                protected Map<String, String> getParams() {

                    return params;
                }



                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response)
                {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));

                        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }

                }

            };

        }

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 1f));

        queue.add(jsonObjectRequest);

        return requestString;
    }

    public static String trimMessage(String json, String key)
    {
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
    public static enum RequestType {
        GET, POST
    }
}

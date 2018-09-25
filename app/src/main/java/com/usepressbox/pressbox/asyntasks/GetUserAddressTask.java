package com.usepressbox.pressbox.asyntasks;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.interfaces.IAddressStatusListener;
import com.usepressbox.pressbox.models.ApiCallParams;
import com.usepressbox.pressbox.support.CustomProgressDialog;
import com.usepressbox.pressbox.support.CustomerDetails;
import com.usepressbox.pressbox.support.ServerResponse;
import com.usepressbox.pressbox.support.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Prasanth.S on 9/11/2018.
 */
public class GetUserAddressTask {


    private Context context;
    private ApiCallParams apiCallParams;
    private String redirect;
    private TextView lblMessage;
    private HashMap<String, String> params;
    private IAddressStatusListener iAddressStatusListener;
    private CustomProgressDialog progress;

    public GetUserAddressTask(Context context, IAddressStatusListener iAddressStatusListener,ApiCallParams apiCallParams, String tag) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.redirect = tag;
        this.iAddressStatusListener=iAddressStatusListener;
    }



    public void ResponseTask() {
        progress = CustomProgressDialog.show(context,  false);

        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progress.dismiss();

            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    if (status.equalsIgnoreCase("success")) {
                        iAddressStatusListener.addressStatus(jsonObject);
                        progress.dismiss();

                    } else {
                        String message = Html.fromHtml(jsonObject.getString("message")).toString();
//                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                        UtilityClass.showAlertWithOk(context, "null", message, "ConfirmOrderTypeTask");
                        throw new JSONException("Api call returned unrecognised status: " + apiCallParams.getUrl());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();

                }
            }
        });
    }


}

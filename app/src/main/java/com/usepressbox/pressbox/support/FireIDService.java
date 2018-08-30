package com.usepressbox.pressbox.support;


/**
 * Created by Diliban on 21/11/17.
 * This class is used get the token for firebase notification service
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FireIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("tkn","tkn---->"+refreshedToken);

    }

}
package com.usepressbox.pressbox.interfaces;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Diliban on 3/30/2018.
 * This interface is used to pass the selected ordertype values
 */

public interface ISelectServiceListener {
    void onItemClick(View view, int position, String value, ImageView imageView);

    void selectServiceData(ArrayList<String> selectoptions , ArrayList<HashMap<String,String>> selectparams,Boolean other);


}

package com.usepressbox.pressbox.interfaces;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Diliban on 4/6/2018.
 * This interface is used to pass the selected shoecare ordertype values
 */

public interface IShoecareServiceListener {
    void onItemClick(View view, int position, String value, ImageView imageView);
}

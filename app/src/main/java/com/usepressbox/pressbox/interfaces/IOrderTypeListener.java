package com.usepressbox.pressbox.interfaces;

import android.view.View;
import android.widget.ImageView;

import com.usepressbox.pressbox.models.OrderTypeModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Diliban on 3/30/2018.
 * This interface is used to pass the selected ordertype values
 */

public interface IOrderTypeListener {
    void orderTypeData(ArrayList<OrderTypeModel>  orderTypeModels);
}

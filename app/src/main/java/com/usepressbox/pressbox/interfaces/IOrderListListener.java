package com.usepressbox.pressbox.interfaces;

import com.usepressbox.pressbox.models.GetOrdersModel;
import com.usepressbox.pressbox.models.OrderTypeModel;

import java.util.ArrayList;

/**
 * Created by Prasanth on 9/20/2018.
 * This interface is used to pass the orders
 */

public interface IOrderListListener {
    void orderTypeData(ArrayList<GetOrdersModel> orderTypeModels);
}

package com.usepressbox.pressbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.GetOrdersModel;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import java.util.ArrayList;

/**
 * Created by kruno on 22.04.16..
 * This adapter class is used to list the placed orders by the user
 */
public class OrdersAdapter extends BaseAdapter {

    public static int selectedPosition = -1;
    private Context c;
    private ArrayList<GetOrdersModel> dataArray = new ArrayList<>();
    private static LayoutInflater inflater=null;

    public OrdersAdapter(ArrayList<GetOrdersModel>dataArray,Context c) {
        this.dataArray=dataArray;
        this.c = c;

        inflater=(LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<GetOrdersModel> data){
        if(data!= null){
            dataArray = data;
        }
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListCell cell;
        if(convertView==null) {
            convertView=inflater.inflate(R.layout.order_row, null);
            cell=new ListCell();

            cell.tw_order_row_address=(TextView)convertView.findViewById(R.id.tw_order_row_address);
            cell.tw_order_row_info=(TextView)convertView.findViewById(R.id.tw_order_row_info);
            cell.tw_order_row_month=(TextView)convertView.findViewById(R.id.tw_order_row_month);
            cell.tw_order_row_date=(TextView)convertView.findViewById(R.id.tw_order_row_date);
            cell.tw_order_row_lockerID=(TextView)convertView.findViewById(R.id.tw_order_row_locker_id);
            cell.tw_unlock_code=(TextView)convertView.findViewById(R.id.tw_unlock_code);

            convertView.setTag(cell);
        }
        else {
            cell=(ListCell)convertView.getTag();
        }
        GetOrdersModel order = dataArray.get(position);

        //change the data of cell here
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.linear_layout_locker_id);
        if (selectedPosition == position){
            if(order.getStatus().equalsIgnoreCase("Ready for Customer Pickup")
                    || order.getStatus().equalsIgnoreCase("Completed")){
                linearLayout.setVisibility(convertView.VISIBLE);
            }else {
                linearLayout.setVisibility(convertView.GONE);
            }
        }else{
            linearLayout.setVisibility(convertView.GONE);
        }



        try {

            String date = order.getDate();
            cell.tw_order_row_month.setText(UtilityClass.convertDate(date.split("-")[1]));
            cell.tw_order_row_date.setText(date.split("-")[2].substring(0, 2));

            cell.tw_order_row_address.setText(order.getAddress());
            cell.tw_order_row_lockerID.setText(order.getLockerId());


            String unlock_code = SessionManager.CUSTOMER.getPhone().substring(SessionManager.CUSTOMER.getPhone().length() - 4);
            cell.tw_unlock_code.setText(unlock_code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cell.tw_order_row_info.setText(order.getStatus());

        return convertView;
    }

    private Context getApplicationContext() {
        return null;
    }



    private class ListCell {
        private TextView tw_order_row_address;
        private TextView tw_order_row_info;
        private TextView tw_order_row_month;
        private TextView tw_order_row_date;
        private TextView tw_order_row_lockerID;
        private TextView tw_unlock_code;
    }
}

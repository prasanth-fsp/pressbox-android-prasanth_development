package com.usepressbox.pressbox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.interfaces.IConfirmOrderTypeListener;
import com.usepressbox.pressbox.models.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanth.S on 21-Aug-18.
 */


public class ConfirmOrderTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocationModel> locationModelList;
    private Context context;
    private int itemLayout;
    private IConfirmOrderTypeListener iConfirmOrderType;


    public ConfirmOrderTypeAdapter(Context context, int itemLayout, ArrayList<LocationModel> data,
                                   IConfirmOrderTypeListener iConfirmOrderType) {
        this.context = context;
        this.itemLayout = itemLayout;
        locationModelList = data;
        this.iConfirmOrderType = iConfirmOrderType;
    }


    public void setData(ArrayList<LocationModel> data) {
        locationModelList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayout, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder rawHolder, final int position) {
        final ViewHolder holder = (ViewHolder) rawHolder;

//        holder.address.setText(locationModelList.get(position).getCity());
        holder.address.setText(locationModelList.get(position).getAddress() + ", "
                + locationModelList.get(position).getCity() + ", "
                + locationModelList.get(position).getState() + ", "
                + locationModelList.get(position).getZipcode());
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iConfirmOrderType.updateUI(locationModelList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return (null != locationModelList ? locationModelList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView address;

        public ViewHolder(View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);


        }
    }
}

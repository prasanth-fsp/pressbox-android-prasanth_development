package com.usepressbox.pressbox.adapter;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.OrderTypeModel;
import com.usepressbox.pressbox.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

/* Created By Prasanth.S on 03/16/2018*/

public class OrderTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private ArrayList<OrderTypeModel> mCategoryList;


    public OrderTypeAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<OrderTypeModel> data) {
        if (mCategoryList != data) {
            mCategoryList = data;
            notifyDataSetChanged();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView order_title;
        public final ImageView order_image;

        public ItemViewHolder(View view) {
            super(view);
            order_title = (TextView) view.findViewById(R.id.order_title);
            order_image = (ImageView) view.findViewById(R.id.order_img);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_type_list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {

        final ItemViewHolder holder = (ItemViewHolder) viewholder;


            GlideApp.with(context)
                    .load(mCategoryList.get(position).getOrderTypeImg())
                    .placeholder(R.drawable.notification_icon)
                    .error(R.drawable.notification_icon)
                    .into(holder.order_image);

            holder.order_title.setText(mCategoryList.get(position).getOrderTypeName());


        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return (null != mCategoryList ? mCategoryList.size() : 0);
    }


}

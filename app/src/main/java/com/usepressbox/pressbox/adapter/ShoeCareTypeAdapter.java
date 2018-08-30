package com.usepressbox.pressbox.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.OrderTypeModel;

import java.util.ArrayList;

/* Created By Prasanth.S on 03/16/2018*/

public class ShoeCareTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private ArrayList<OrderTypeModel> mCategoryList;


    public ShoeCareTypeAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<OrderTypeModel> data) {
        if (mCategoryList != data) {
            mCategoryList = data;
            notifyDataSetChanged();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView shoe_care_title;

        public ItemViewHolder(View view) {
            super(view);
            shoe_care_title = (TextView) view.findViewById(R.id.shoe_care_title);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.shoe_care_list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {

        final ItemViewHolder holder = (ItemViewHolder) viewholder;

        holder.shoe_care_title.setText(mCategoryList.get(position).getOrderTypeName());

        //Flexlayout properties to setup
        ViewGroup.LayoutParams lp = holder.shoe_care_title.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams)
                    holder.shoe_care_title.getLayoutParams();
            flexboxLp.setFlexGrow(1.0f);
        }
        holder.shoe_care_title.setBackgroundResource(R.drawable.shoe_care_item_bg);
        holder.shoe_care_title.setSelected(true);
        holder.shoe_care_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shoe_care_title.isSelected()) {
                    holder.shoe_care_title.setBackgroundResource(R.drawable.shoe_care_item_selected_bg);
                    holder.shoe_care_title.setTextColor(context.getResources().getColor(R.color.white));
                    v.setSelected(false);

                    Toast.makeText(context, mCategoryList.get(position).getOrderTypeName(), Toast.LENGTH_SHORT).show();
                } else {
                    holder.shoe_care_title.setBackgroundResource(R.drawable.shoe_care_item_bg);
                    holder.shoe_care_title.setTextColor(context.getResources().getColor(R.color.black));
                    v.setSelected(true);
                }
            }
        });
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return (null != mCategoryList ? mCategoryList.size() : 0);
    }


}

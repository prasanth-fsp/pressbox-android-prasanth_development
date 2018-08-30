package com.usepressbox.pressbox.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usepressbox.pressbox.interfaces.ISelectServiceListener;
import com.usepressbox.pressbox.R;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Diliban on 3/29/2018.
 * Modified by Prasanth.S on 27/08/2018
 *
 * This adapter class is used to list the selected ordertypes by the user
 */

public class SelectservicesAdapter extends RecyclerView.Adapter<SelectservicesAdapter.ViewHolder> {
    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;
    private ISelectServiceListener mClickListener;
    private Context context;

    public SelectservicesAdapter(Context context, ISelectServiceListener mClickListener, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener = mClickListener;
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.selectservices_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String selectoption = mData.get(position);

        holder.myTextView.setText(selectoption);

        setIcons(position, selectoption, holder);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(holder.itemView, position, selectoption, holder.myimage);

            }
        });


    }

    private void setIcons(int position, String selectoption, ViewHolder holder) {

        switch (selectoption) {
            case "Wash and Fold":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wash_fold_non_active));
            case "Wash & Fold":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wash_fold_non_active));
                break;
            case "Dry Clean & Press":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dry_clean_non_active));
                break;
            case "Launder & Press":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.launder_non_active));
                break;
            case "Repairs & Alterations":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.repairs_non_active));
                break;
            case "Press Only":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pressonly_non_active));
                break;
            case "Customer Service":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.customer_service_non_active));
                break;
            case "Shoe Care":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shoe_care_non_active));
                break;
            case "Shoe Shine":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shoe_shine_non_active));
                break;
            case "Shoe Repair":
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shoe_repair_non_active));
                break;
            default:
                holder.myimage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unselect));

        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        ImageView myimage;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.dry);
            myimage = (ImageView) itemView.findViewById(R.id.order_image);
        }

    }

    public String getItem(int id) {
        return mData.get(id);
    }


}

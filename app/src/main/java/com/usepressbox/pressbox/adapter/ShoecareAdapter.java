package com.usepressbox.pressbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import java.util.ArrayList;
import com.usepressbox.pressbox.interfaces.IShoecareServiceListener;

/**
 * Created by Node on 3/29/2018.
 * This adapter class is used to list the selected shoecare ordertypes by the user
 */

public class ShoecareAdapter  extends RecyclerView.Adapter<ShoecareAdapter.ViewHolder> {
    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;
    private IShoecareServiceListener mClickListener;
    public ShoecareAdapter(Context context, IShoecareServiceListener mClickListener, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mClickListener=mClickListener;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shoecare_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String shoecareoption = mData.get(position);
        holder.myTextView.setText(shoecareoption);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.onItemClick(holder.rlayout,position,shoecareoption,holder.myimage);


            }
        });



    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        ImageView myimage;
        RelativeLayout rlayout;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.shoecaretext);
            myimage = (ImageView) itemView.findViewById(R.id.shoecarecheck);
            rlayout =(RelativeLayout)itemView.findViewById(R.id.shoecarelayout);
        }

    }

    public  String getItem(int id) {
        return mData.get(id);
    }

}

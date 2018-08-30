package com.usepressbox.pressbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.TermsAndConditionModel;

import java.util.ArrayList;

/**
 * Created by kruno on 11.05.16..
 * This adapter class is used to list the terms and conditons
 */
public class TermsAndConditionAdapter extends BaseAdapter {
    private ArrayList<TermsAndConditionModel> termsAndConditionModelArrayList = new ArrayList<>();
    Context c;

    private static LayoutInflater inflater=null;

    public TermsAndConditionAdapter(ArrayList<TermsAndConditionModel> termsAndConditionModelArrayList, Context c) {
        this.termsAndConditionModelArrayList = termsAndConditionModelArrayList;
        this.c = c;

        inflater=(LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.termsAndConditionModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListCell cell;
        if(convertView==null) {
            convertView=inflater.inflate(R.layout.terms_and_condition_row, null);
            cell=new ListCell();

            cell.tw_header=(TextView)convertView.findViewById(R.id.tw_header);
            cell.tw_description=(TextView)convertView.findViewById(R.id.tw_description);

            convertView.setTag(cell);
        }
        else {
            cell=(ListCell)convertView.getTag();
        }


        TermsAndConditionModel termsAndConditionModel = this.termsAndConditionModelArrayList.get(position);

        cell.tw_header.setText(termsAndConditionModel.getHeader());
        cell.tw_description.setText(termsAndConditionModel.getDescription());

        return convertView;
    }

    private Context getApplicationContext() {
        return null;
    }

    private class ListCell {
        private TextView tw_header;
        private TextView tw_description;
    }
}

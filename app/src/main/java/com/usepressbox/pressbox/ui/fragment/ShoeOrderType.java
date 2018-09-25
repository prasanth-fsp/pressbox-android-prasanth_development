package com.usepressbox.pressbox.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.ShoecareAdapter;
import com.usepressbox.pressbox.interfaces.IShoecareServiceListener;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Diliban
 * This fragment is used to show the listed shoecare ordertypes with respect to the service area when user selcted shoecare on select services
 * This fragment allows user to select the shoecare ordertypes
 */

public class ShoeOrderType extends Fragment implements IShoecareServiceListener {


    private View v;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_left)
    TextView cancel;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;


    @BindView(R.id.rvShoecare)
    RecyclerView recyclerView;
    ShoecareAdapter mAdapter;


    @BindView(R.id.place_order_button)
    Button place_order_button;
    ArrayList<String> shoecarelist ;
    ArrayList<String> shoecareoptions ;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        UtilityClass.hideKeyboard(getActivity());


        v = inflater.inflate(R.layout.fragment_shoe_order_type, container, false);
        ButterKnife.bind(this, v);
        place_order_button.setText("Next");


        shoecarelist = new ArrayList<String>();
        shoecareoptions = new ArrayList<String>();


        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);

        setToolbarTitle();



        String[] suboptions = {"Shoe cleaning", "Cleaning and waterproofing", "UGGs", "Heel tips", "Half sole replacement",
                "Full sole replacement", "Sole guards", "Handbags"
        };
        shoecareoptions = new ArrayList<String>(Arrays.asList(suboptions));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShoecareAdapter(getActivity(), this, shoecareoptions);
        recyclerView.setAdapter(mAdapter);


        return v;
    }
    @OnClick(R.id.toolbar_left)
    void cancel() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

    public void setToolbarTitle() {

//        toolbar.setNavigationIcon(R.drawable.back);
        cancel.setText("Back");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");
        if (Constants.register) {
            cancel.setVisibility(View.INVISIBLE);
        }

    }


    @OnClick(R.id.place_order_button)
    void next() {
        String backStateName = this.getClass().getName();

        if (shoecarelist.size() > 0) {
            if (Constants.register) {

                Fragment fragment = new NewLockerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("shoecarelist", shoecarelist);
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragment, fragment);
                transaction.commit();


            } else {

//                Fragment fragment = new InstructionsFragment();
                Fragment fragment = new NewLockerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("shoecarelist", shoecarelist);
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragment, fragment);
                transaction.commit();
            }
        } else {
            UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.shoecarealert), "shoecare");
        }

    }


    @Override
    public void onItemClick(View view, int position, String value, ImageView imageView) {

        if (!itemStateArray.get(position, false)) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.customselect));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.shoeselect));
            shoecarelist.add(value);
            itemStateArray.put(position, true);
        } else {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.customunselect));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.shoeunselect));
            shoecarelist.remove(value);
            itemStateArray.put(position, false);
        }

    }


}


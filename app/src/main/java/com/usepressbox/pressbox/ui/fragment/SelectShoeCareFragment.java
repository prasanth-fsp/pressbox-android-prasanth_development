package com.usepressbox.pressbox.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.OrderTypeAdapter;
import com.usepressbox.pressbox.adapter.ShoeCareTypeAdapter;
import com.usepressbox.pressbox.models.OrderTypeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth.S on 8/2/2018.
 */

public class SelectShoeCareFragment extends Fragment {
    private View view;
    @BindView(R.id.toolbar_left)
    TextView left;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;

    @BindView(R.id.select_shoe_care_title)
    TextView select_shoe_care_title;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.place_order_button)
    Button place_order_button;
    @BindView(R.id.shoe_care_type_recyclerview)
    RecyclerView shoe_care_type_recyclerview;
    @BindView(R.id.special_instruction_edittext)
    EditText special_instruction_edittext;
    @BindView(R.id.shoe_care_type_parent)
    RelativeLayout shoe_care_type_parent;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = inflater.inflate(R.layout.fragment_select_shoe_care, container, false);
        ButterKnife.bind(this, view);
        context = getContext();

        setToolbarTitle();
        place_order_button.setText("Next");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setShoeCareRecyclerView();
        return view;
    }

    private void setShoeCareRecyclerView() {

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        // Set flex direction.
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);

        shoe_care_type_recyclerview.setLayoutManager(flexboxLayoutManager);

        shoe_care_type_recyclerview.setItemAnimator(new DefaultItemAnimator());
        shoe_care_type_recyclerview.setHasFixedSize(true);
        shoe_care_type_recyclerview.setNestedScrollingEnabled(false);

        ShoeCareTypeAdapter shoeCareTypeAdapter = new ShoeCareTypeAdapter(context);
        shoe_care_type_recyclerview.setAdapter(shoeCareTypeAdapter);
        shoeCareTypeAdapter.setData(shoeCareList());
    }

    private ArrayList<OrderTypeModel> shoeCareList() {
        ArrayList<OrderTypeModel> orderTypeModels = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            OrderTypeModel orderTypeModel = new OrderTypeModel();
            switch (i) {
                case 0:
                    orderTypeModel.setOrderTypeName("Shoe Shine");
                    break;
                case 1:
                    orderTypeModel.setOrderTypeName("Sole Repair");
                    break;
                case 2:
                    orderTypeModel.setOrderTypeName("Lining Replacement");
                    break;
                case 3:
                    orderTypeModel.setOrderTypeName("Add Sole Guards");
                    break;
                case 4:
                    orderTypeModel.setOrderTypeName("Heel Replacement");
                    break;
                case 5:
                    orderTypeModel.setOrderTypeName("Other");
                    break;
            }

            orderTypeModels.add(orderTypeModel);

        }
        return orderTypeModels;
    }

    private void setToolbarTitle() {
        left.setText("Cancel");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");
    }


    @OnClick(R.id.toolbar_left)
    void previousScreen() {
        FragmentTransaction article_fragmentTransaction = getFragmentManager().beginTransaction();
        article_fragmentTransaction.replace(R.id.fragment, new SelectOrderTypeFragment());
        article_fragmentTransaction.commit();
    }

}
package com.usepressbox.pressbox.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.OrderTypeAdapter;
import com.usepressbox.pressbox.interfaces.IOrderTypeListener;
import com.usepressbox.pressbox.models.OrderTypeModel;
import com.usepressbox.pressbox.ui.activity.order.OrderPreferences;
import com.usepressbox.pressbox.utils.UtilityClass;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
/**
 * Created by Prasanth.S on 8/2/2018.
 */
public class SelectOrderTypeFragment extends Fragment implements IOrderTypeListener {

    @BindView(R.id.toolbar_left)
    TextView left;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.select_order_title)
    TextView select_order_title;
    @BindView(R.id.select_order_message)
    TextView select_order_message;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.place_order_button)
    Button place_order_button;
    @BindView(R.id.order_type_recyclerview)
    RecyclerView order_type_recyclerview;
    @BindView(R.id.set_order_preference_layout)
    LinearLayout set_order_preference_layout;
    @BindView(R.id.special_instruction_edittext)
    EditText special_instruction_edittext;
    @BindView(R.id.order_type_parent)
    RelativeLayout order_type_parent;
    private Context context;

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = inflater.inflate(R.layout.fragment_select_order_type, container, false);
        ButterKnife.bind(this, view);
        context = getContext();

        setToolbarTitle();
        place_order_button.setText("Next");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        setOrderType();
        return view;
    }

    private void setOrderType() {

        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        order_type_recyclerview.setLayoutManager(llm);
        order_type_recyclerview.setItemAnimator(new DefaultItemAnimator());
        order_type_recyclerview.setHasFixedSize(true);
        order_type_recyclerview.setNestedScrollingEnabled(false);

        OrderTypeAdapter orderTypeAdapter = new OrderTypeAdapter(context);
        order_type_recyclerview.setAdapter(orderTypeAdapter);
        orderTypeAdapter.setData(orderList());
    }

    private void setToolbarTitle() {
        left.setText("Cancel");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");
    }

    @OnClick(R.id.toolbar_left)
    void cancel() {
        FragmentTransaction article_fragmentTransaction = getFragmentManager().beginTransaction();
        article_fragmentTransaction.replace(R.id.fragment, new SecureFragment());
        article_fragmentTransaction.commit();
    }

    @OnClick(R.id.set_order_preference_layout)
    void orderPreference() {
        Intent orderPreferences = new Intent(getContext(), OrderPreferences.class);
        orderPreferences.putExtra("From", "SelectOrderTypeFragment");
        startActivity(orderPreferences);
    }

    @OnClick(R.id.place_order_button)
    void nextScreen() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new SelectShoeCareFragment());
        fragmentTransaction.commit();
    }

    @OnClick(R.id.special_instruction_edittext)
    void specialInstruction() {
//        order_type_parent.setVisibility(View.GONE);
    }

    @OnClick(R.id.order_type_parent)
    void orderTypreParent() {
        UtilityClass.hideKeyboard(getActivity());
//        order_type_parent.setVisibility(View.VISIBLE);
    }

    @OnTextChanged(value = R.id.special_instruction_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void specialInstruction(Editable editable) {
        validateSpecialInstruction(editable);
    }

    private boolean validateSpecialInstruction(Editable editable) {
        String value = editable.toString();
        if (value.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void orderTypeData(ArrayList<OrderTypeModel> orderTypeModels) {

    }

    private ArrayList<OrderTypeModel> orderList() {
        ArrayList<OrderTypeModel> orderTypeModels = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            OrderTypeModel orderTypeModel = new OrderTypeModel();
//            orderTypeModel.setOrderTypeImg(String.valueOf(R.drawable.notification_icon));
            switch (i) {
                case 0:
                    orderTypeModel.setOrderTypeName("Dry Clean & Press");
                    break;
                case 1:
                    orderTypeModel.setOrderTypeName("Wash & Fold");
                    break;
                case 2:
                    orderTypeModel.setOrderTypeName("Shoe Care");
                    break;
                case 3:
                    orderTypeModel.setOrderTypeName("Launder & Press");
                    break;
                case 4:
                    orderTypeModel.setOrderTypeName("Press Only");
                    break;
                case 5:
                    orderTypeModel.setOrderTypeName("Repairs and Alterations");
                    break;
            }

            orderTypeModels.add(orderTypeModel);

        }
        return orderTypeModels;
    }
}
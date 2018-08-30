package com.usepressbox.pressbox.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kruno on 26.04.16..
 * This fragment is used to place new order for the new user after registration
 */
public class IntroWelcomeFragment extends Fragment {

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_left) TextView cancel;
    @BindView(R.id.toolbar_right) TextView skip;
    @BindView(R.id.toolbar_title) TextView title;
    @BindView(R.id.tw_welcome) TextView welcome;
    @BindView(R.id.tw_locker_message) TextView message;
//    @BindView(R.id.et_locker_number) EditText lockerNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        UtilityClass.hideKeyboard(getActivity());

        v = inflater.inflate(R.layout.fragment_new_order_locker, container, false);
        ButterKnife.bind(this, v);

        welcome.setText("Welcome, " + SessionManager.CUSTOMER.getName());
        message.setText(R.string.lockertip);


        setToolbarTitle();
        return v;
    }

    @OnClick(R.id.toolbar_right) void skip() {
        Constants.register= false;
        Intent toOrders = new Intent(getActivity(), Orders.class);
        startActivity(toOrders);
        getActivity().finish();
    }

//    @OnClick(R.id.btn_new_order_next) void makeOrder() {
//
//        if (lockerNumber.getText().toString().length()>0){
//            if(SessionManager.ORDER ==null) SessionManager.ORDER= new Order();
//            SessionManager.ORDER.setLockerName(lockerNumber.getText().toString());
//
//            Constants.register= true;
//
//            Fragment fragment = new SelectServices();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.add(R.id.fragment, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//        }else{
//            Toast.makeText(getActivity(), getResources().getString(R.string.toast_locker_number), Toast.LENGTH_SHORT).show();
//        }
//    }



    public void setToolbarTitle(){
        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        cancel.setText("");
        skip.setText("skip");
        title.setText("");
    }
}

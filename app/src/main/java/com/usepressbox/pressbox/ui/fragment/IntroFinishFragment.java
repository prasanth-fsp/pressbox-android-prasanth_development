package com.usepressbox.pressbox.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.ui.activity.order.OrderPreferences;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.ui.activity.order.Video;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kruno on 26.04.16..
 * This fragment is used to update the card details and order preferences on registration
 */
public class IntroFinishFragment extends Fragment {

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_left) TextView skip;
    @BindView(R.id.toolbar_right) TextView next;
    @BindView(R.id.toolbar_title) TextView title;
    @BindView(R.id.tw_go_home) TextView goHome;
    @BindView(R.id.credit_card_number) EditText credit_card_number;
    @BindView(R.id.credit_card_date) EditText credit_card_date;
    @BindView(R.id.credit_card_CVV) EditText credit_card_CVV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilityClass.hideKeyboard(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        v = inflater.inflate(R.layout.fragment_intro_finish, container, false);
        ButterKnife.bind(this, v);

        setToolbarTitle();
        setDateFormat();
        return v;
    }

    @OnClick(R.id.tw_go_home) void toOrders() {

        updateBilling();
    }
    @OnClick(R.id.linear_layout_order_pref) void toOrderPreferences() {
        Intent orderPreferences = new Intent(getActivity(), OrderPreferences.class);
        startActivity(orderPreferences);
        Constants.firstTime = true;
    }

    public void setToolbarTitle(){
        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        skip.setText("");
        next.setText("");
        title.setText("");
    }

    public boolean checkCreditCard(){

        boolean creditCardOk = false;
        if (credit_card_number.getText().toString().length()>0){
            if (credit_card_date.getText().toString().length()>0){
                if (credit_card_CVV.getText().toString().length()>0){
                    creditCardOk = true;
                }
            }
        }
        return creditCardOk;
    }

    public void updateBilling(){

        if (checkCreditCard()){
            SessionManager.CUSTOMER.setCardNumber(credit_card_number.getText().toString());
            SessionManager.CUSTOMER.setExpMonth(credit_card_date.getText().toString().split("/")[0]);
            SessionManager.CUSTOMER.setExpYear("20" + credit_card_date.getText().toString().split("/")[1]);
            SessionManager.CUSTOMER.setCsc(credit_card_CVV.getText().toString());

            new BackgroundTask(getActivity(), SessionManager.CUSTOMER.updateBillling(getActivity()));
        }
        Constants.register = false;

        Intent intent = new Intent(getActivity(), Orders.class);
        startActivity(intent);

    }

    public void setDateFormat(){

        TextWatcher mDateEntryWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                if (working.length() == 2 && before == 0){
                    working += "/";
                    credit_card_date.setText(working);
                    credit_card_date.setSelection(working.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        credit_card_date.addTextChangedListener(mDateEntryWatcher);
    }


}

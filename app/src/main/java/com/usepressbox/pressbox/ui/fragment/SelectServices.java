package com.usepressbox.pressbox.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.SelectservicesAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.interfaces.ISelectServiceListener;
import com.usepressbox.pressbox.ui.activity.order.OrderPreferences;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Diliban
 * This fragment is used to/s/ow the listed ordertypes with respect to the service area and allows user to select the ordertypes
 * Modified by Prasanth.S on 08/13/2018
 */

public class SelectServices extends Fragment implements ISelectServiceListener {


    private View v;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_left)
    TextView cancel;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;


    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;
    @BindView(R.id.otherlayout)
    LinearLayout otherlayout;
    @BindView(R.id.parent_scroll)
    LinearLayout parent_scroll;


    @BindView(R.id.place_order_button)
    Button place_order_button;
    @BindView(R.id.set_order_preference_layout)
    LinearLayout set_order_preference_layout;
    @BindView(R.id.special_instruction_input_layout)
    TextInputLayout special_instruction_input_layout;
    @BindView(R.id.select_service_parent)
    RelativeLayout select_service_parent;
    @BindView(R.id.special_instruction_edittext)
    EditText special_instruction_edittext;
    @BindView(R.id.preference_img)
    ImageView preference_img;
    @BindView(R.id.special_instruction_write_img)
    ImageView special_instruction_write_img;
    @BindView(R.id.select_serviec_scrollview)
    NestedScrollView select_serviec_scrollview;


    Boolean state = false;
    ArrayList<String> selectlist;
    ArrayList<String> selectids;


    SelectservicesAdapter adapter;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    ArrayList<HashMap<String, String>> selectvalues;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }


        v = inflater.inflate(R.layout.fragment_select_services, container, false);
        ButterKnife.bind(this, v);

        UtilityClass.hideKeyboard(getActivity());
        place_order_button.setText("Next");
        special_instruction_edittext.clearFocus();
        special_instruction_edittext.setActivated(false);

        selectlist = new ArrayList<String>();
        selectids = new ArrayList<String>();

        setToolbarTitle();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (UtilityClass.isConnectingToInternet(getActivity())) {
            new BackgroundTask(getActivity(), this, SessionManager.ORDER.getServiceType(getActivity()));
        } else {
            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


        return v;
    }

    @Override
    public void onItemClick(View view, int position, String value, ImageView imageView) {


        if (!itemStateArray.get(position, false)) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.customselect));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.select));
            selectlist.add(value);
            itemStateArray.put(position, true);

            //Created By Prasanth.S on 13.08.2018
            setIcons(imageView, value, "true");

        } else {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.customunselect));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.unselect));
            selectlist.remove(value);
            itemStateArray.put(position, false);
            setIcons(imageView, value, "false");

        }

    }

    /*
        Created By Prasanth.S on 13.08.2018
    */
    private void setIcons(ImageView imageView, String orderType, String select) {
        switch (orderType) {
            case "Wash and Fold":
                if (select.equalsIgnoreCase("true")) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.wash_fold_active));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.wash_fold_non_active));
                }
            case "Wash & Fold":
                if (select.equalsIgnoreCase("true")) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.wash_fold_active));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.wash_fold_non_active));
                }
                break;
            case "Dry Clean & Press":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dry_clean_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dry_clean_non_active));
                break;
            case "Launder & Press":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.launder_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.launder_non_active));
                break;
            case "Repairs & Alterations":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.repairs_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.repairs_non_active));
                break;
            case "Press Only":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.pressonly_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.pressonly_non_active));
                break;
            case "Customer Service":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.customer_service_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.customer_service_non_active));
                break;
            case "Shoe Care":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_care_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_care_non_active));
                break;
            case "Shoe Shine":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_shine_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_shine_non_active));
                break;
            case "Shoe Repair":
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_repair_active));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shoe_repair_non_active));
                break;
            default:
                if (select.equalsIgnoreCase("true"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.select));
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.unselect));

        }
    }


    @Override
    public void selectServiceData(ArrayList<String> selectoptions, ArrayList<HashMap<String, String>> selectparams, Boolean
            other) {

        if (other)
            otherlayout.setVisibility(View.GONE);
        else
            otherlayout.setVisibility(View.GONE);
        selectvalues = selectparams;
        int numberOfColumns = 2;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));


        ArrayList<String> actualList = new ArrayList<String>();
        actualList = selectoptions;
        ArrayList<String> sortedList = new ArrayList<>();

        if (selectoptions != null || selectoptions.size() > 0) {
//          for (int i = 0; i < selectoptions.size(); i++) {

            if (actualList.contains("Dry Clean & Press")) {
                sortedList.add("Dry Clean & Press");
                actualList.remove("Dry Clean & Press");
            }

            if (actualList.contains("Wash & Fold")) {
                sortedList.add("Wash & Fold");
                actualList.remove("Wash & Fold");
            }
            if (actualList.contains("Wash and Fold")) {
                sortedList.add("Wash and Fold");
                actualList.remove("Wash and Fold");
            }

            if (actualList.contains("Repairs & Alterations")) {
                sortedList.add("Repairs & Alterations");
                actualList.remove("Repairs & Alterations");
            }

            if (actualList.contains("Shoe Care")) {
                sortedList.add("Shoe Care");
                actualList.remove("Shoe Care");
            }

            for (int i = 0; i < actualList.size(); i++) {
                sortedList.add(actualList.get(i));
            }

//          }
        }

        adapter = new SelectservicesAdapter(getActivity(), this, sortedList);
        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.place_order_button)
    void next() {

        selectids = getSelectIds(selectlist, selectvalues);
        String ordertype = TextUtils.join(", ", selectids);
        SessionManager.ORDER.setOrderType(ordertype);

        if (selectlist.size() > 0) {
//            if (validateSpecialInstruction()) {
            if (special_instruction_edittext.getText().toString().length() > 0)
                SessionManager.ORDER.setOrderNotes(special_instruction_edittext.getText().toString());
            else
                SessionManager.ORDER.setOrderNotes("");

            if (selectlist.contains(Constants.shoecare)) {

                Fragment fragment = new ShoeOrderType();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
                transaction.replace(R.id.fragment, fragment);
                transaction.commit();

            } else {
                Fragment fragment = new NewLockerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
                transaction.replace(R.id.fragment, fragment);
                transaction.commit();

            }
//            } else {
//                UtilityClass.showAlert(getActivity(),"Please write an order notes");
//                }
        } else {
            UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.selectalert), "selectservice");
        }

    }


    @OnClick(R.id.otherlayout)
    void other() {
        if (this.state.equals(false)) {
            otherlayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.customselect));
            selectlist.add("Other");
            this.state = true;
        } else {
            state = true;
            otherlayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.customunselect));
            selectlist.remove("Other");
            this.state = false;
        }


    }

    public void setToolbarTitle() {

        cancel.setText("cancel");
        next.setText("Skip");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");

        if (new SessionManager(getContext()).getUserFlow() != null) {
            if (new SessionManager(getContext()).getUserFlow().equalsIgnoreCase("Register")) {
                next.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                new SessionManager(getContext()).saveUserFlow("done");
            }
        }

    }

    private ArrayList<String> getSelectIds(ArrayList<String> selectlist, ArrayList<HashMap<String, String>> selectvalues) {
        ArrayList<String> selected = new ArrayList<String>();

        for (String s : selectlist) {
            for (HashMap<String, String> map : selectvalues) {

                for (HashMap.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(s)) {
                        String id = entry.getKey();
                        selected.add(id);

                    }
                }
            }

        }

        return selected;
    }


    @OnClick(R.id.toolbar_left)
    void cancel() {
        Intent toOrder = new Intent(getActivity(), Orders.class);
        startActivity(toOrder);
        getActivity().finish();
    }
    @OnClick(R.id.toolbar_right)
    void landingScreen() {
        Intent toOrder = new Intent(getActivity(), Orders.class);
        startActivity(toOrder);
        getActivity().finish();
    }

    @OnClick(R.id.set_order_preference_layout)
    void orderPreference() {
        Intent orderPreferences = new Intent(getContext(), OrderPreferences.class);
        orderPreferences.putExtra("From", "SelectService");
        startActivity(orderPreferences);
        getActivity().finish();
    }


    @OnTouch({R.id.select_service_parent, R.id.tool_bar, R.id.rvNumbers, R.id.parent_scroll, R.id.special_instruction_heading_layout})
    boolean touch(View v, MotionEvent motionEvent) {
        UtilityClass.hideKeyboard(getActivity());
        special_instruction_edittext.clearFocus();
        special_instruction_edittext.setCursorVisible(false);

        switch (v.getId()) {
            case R.id.special_instruction_heading_layout:
                special_instruction_edittext.setCursorVisible(true);
                special_instruction_edittext.setFocusableInTouchMode(true);
                special_instruction_edittext.setFocusable(true);
                special_instruction_edittext.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(special_instruction_edittext, InputMethodManager.SHOW_IMPLICIT);
                break;

        }
        return true;
    }

    @OnTextChanged(value = R.id.special_instruction_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void specialInstruction(Editable editable) {
        validateSpecialInstruction();
    }

    private boolean validateSpecialInstruction() {
        String value = special_instruction_edittext.getText().toString();
        if (value.isEmpty()) {
            special_instruction_write_img.setVisibility(View.VISIBLE);
            return false;
        }
        special_instruction_write_img.setVisibility(View.GONE);
        return true;
    }


}

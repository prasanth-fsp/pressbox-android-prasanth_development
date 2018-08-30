package com.usepressbox.pressbox.ui.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.ConfirmOrderTypeAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.asyntasks.ConfirmOrderTypeTask;
import com.usepressbox.pressbox.interfaces.IConfirmOrderType;
import com.usepressbox.pressbox.models.LocationModel;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.Signature;
import com.usepressbox.pressbox.utils.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth.S on 09.08.2018..
 */
public class NewLockerFragment extends Fragment implements IConfirmOrderType {

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_left)
    TextView skip;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.promo_code_edittext)
    EditText promo_code_edittext;
    @BindView(R.id.locker_number_edittext)
    EditText locker_number_edittext;
    @BindView(R.id.apply_button)
    Button apply_button;
    @BindView(R.id.find_location__button)
    Button find_location__button;
    @BindView(R.id.pickup_from_location__button)
    Button pickup_from_location__button;
    @BindView(R.id.place_order_button)
    Button place_order_button;
    @BindView(R.id.concierge_layout)
    LinearLayout concierge_layout;
    @BindView(R.id.private_locker)
    LinearLayout private_locker;
    @BindView(R.id.promocode_layout)
    LinearLayout promocode_layout;
    @BindView(R.id.concierge_address)
    TextView concierge_address;
    @BindView(R.id.concierge_invalid_address)
    TextView concierge_invalid_address;
    @BindView(R.id.concierge_place_order_content)
    TextView concierge_place_order_content;
    @BindView(R.id.promo_code_heading)
    TextView promo_code_heading;
    ArrayList<String> shoecarelist;
    public Context context;
    private String lockerMatchCase ="private";
    private Dialog dialog;
    private String nearByAddress,lockerNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        UtilityClass.hideKeyboard(getActivity());

        v = inflater.inflate(R.layout.fragment_new_order_locker, container, false);
        ButterKnife.bind(this, v);

        context = getContext();
        place_order_button.setText("SUBMIT");
        place_order_button.setBackground(getResources().getDrawable(R.drawable.submit_order_button_bg));

        setToolbarTitle();


        /*To check API with current address */
        getOrderType();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("shoecarelist"))
                shoecarelist = args.getStringArrayList("shoecarelist");
        }

//        new SessionManager(context).saveLockerNumber(null);
//        new SessionManager(context).saveLockerNumber(locker_number_edittext.getText().toString());
        lockerNumber = locker_number_edittext.getText().toString();

        return v;
    }

    private void getOrderType() {

        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
        if (new SessionManager(context).getUserAddres() != null) {
            UtilityClass.getLocationFromAddress(new SessionManager(context).getUserAddres(), getContext());

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("token", Constants.TOKEN);
            params.put("address", new SessionManager(context).getUserAddres());
            params.put("geolocation", new SessionManager(context).getUserGeoLocation());
//        params.put("address","30 Park Avenue");
//        params.put("geolocation", "41.8829607, -87.63414829999999");
            params.put("sessionToken", new SessionManager(context).getSessionToken());
            params.put("signature", Signature.getUrlConversion(params));
            ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.confirmOrderType(), this, params,"getOrderType");
            confirmOrderTypeTask.ResponseTask();
        } else {
            UtilityClass.showAlertWithRedirect(context, "null", "Please enter your address", "myaccount");
        }
    }


    @OnClick(R.id.toolbar_left)
    void cancel() {

//        if (shoecarelist != null) {
//            Fragment fragment = new ShoeOrderType();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment);
//
//            if (!currentFragment.getClass().equals(fragment.getClass())) {
//                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
//            }            transaction.replace(R.id.fragment, fragment);
//            transaction.commit();
//        } else {
//            Fragment fragment = new SelectServices();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment);
//
//            if (!currentFragment.getClass().equals(fragment.getClass())) {
//                transaction.addToBackStack(Constants.BACK_STACK_ROOT_TAG);
//            }            transaction.replace(R.id.fragment, fragment);
//            transaction.commit();

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
//        }
    }


    @OnClick(R.id.apply_button)
    void promoCodeApply() {
        if (promo_code_edittext.getText().toString().length() > 0) {
            SessionManager.CUSTOMER.setPromoCode(promo_code_edittext.getText().toString());
            new BackgroundTask(getActivity(), SessionManager.CUSTOMER.savePromoCode(context), this, "NewLockerFragment");
        } else {
            UtilityClass.showAlertWithOk(context, "Alert!", "Please enter a promo code", "promocode");
        }

    }


    @OnClick(R.id.find_location__button)
    void find_location() {
        getNearByLocation();
    }

    private void getNearByLocation() {

        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();

        String latitude = null;
        String longitude = null;
        try {
            String[] splited = new SessionManager(context).getUserAddres().split(",");
            latitude = splited[0];
            longitude = splited[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", Constants.TOKEN);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("sessionToken", new SessionManager(context).getSessionToken());
        params.put("business_id", Constants.BUSINESS_ID);
        params.put("signature", Signature.getUrlConversion(params));
        ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.getNearByLocation(), this, params,"getNearByLocation");
        confirmOrderTypeTask.ResponseTask();
    }




    public void setToolbarTitle() {
        skip.setText("Cancel");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");

        if (Constants.register) {
            skip.setVisibility(View.INVISIBLE);
        }
    }

    private String getOrderNoteText(ArrayList<String> shoecarelist, String ordernotes) {
        String shoecaretext = TextUtils.join(", ", shoecarelist);
        String shoecareformat = shoecaretext.toUpperCase();
        String order_Notes = shoecareformat + ", " + ordernotes;
        return order_Notes;
    }


    @Override
    public void addressMatchCase(String value, LocationModel locationModel) {

        switch (value) {
            case "true":
                if (!locationModel.getLocationType().equalsIgnoreCase("null")) {
                    if (locationModel.getLocationType().equalsIgnoreCase("Lockers")) {
                        showLockerView();

                        lockerMatchCase = "Approved Private Locker";
                        find_location__button.setVisibility(View.GONE);

                    } else if (locationModel.getLocationType().equalsIgnoreCase("Concierge")
                            || locationModel.getLocationType().equalsIgnoreCase("Offices")) {
                        lockerMatchCase = "Approved doorman";
                        pickup_from_location__button.setVisibility(View.VISIBLE);

                        String locationId = locationModel.getLocation_id();
                        String address = locationModel.getAddress();

                        concierge_layout.setVisibility(View.VISIBLE);
                        /*UpdateTextViewStyle*/
                        updateTextViewStyle();

                        if (!address.equalsIgnoreCase("null"))
                            concierge_address.setText(address);

                        new SessionManager(context).saveLocationId(locationId);
                        /*GetLockerNumber From API*/
                        getLockerNumber();

                        showPromoCodeView();

                    } else if (locationModel.getLocationType().equalsIgnoreCase("Kiosk")) {
                        lockerMatchCase = "Public locker location";
                        find_location__button.setVisibility(View.VISIBLE);
                        showLockerView();
                    } else {
                        lockerMatchCase = "Public locker location";
                        find_location__button.setVisibility(View.VISIBLE);
                        showLockerView();
                    }
                } else {
                    lockerMatchCase = "Public locker location";
                    showLockerView();
                    find_location__button.setVisibility(View.VISIBLE);
                }
                break;
            case "false":
                showLockerView();
                find_location__button.setVisibility(View.VISIBLE);
                lockerMatchCase = "Public locker location";
                break;
        }
    }

    private void updateTextViewStyle() {
        concierge_place_order_content.setTypeface(concierge_place_order_content.getTypeface(), Typeface.ITALIC);

        SpannableString ss = new SpannableString(getResources().getString(R.string.concierge_order_address_incorret));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(context, MyAcccount.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.pressbox)); // specific color for this link
            }
        };
        ss.setSpan(clickableSpan, 25, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        concierge_invalid_address.setText(ss);
        concierge_invalid_address.setMovementMethod(LinkMovementMethod.getInstance());
        concierge_invalid_address.setHighlightColor(Color.TRANSPARENT);

    }

    private void showLockerView() {
        private_locker.setVisibility(View.VISIBLE);
        pickup_from_location__button.setVisibility(View.GONE);
        showPromoCodeView();
    }

    private void showPromoCodeView() {
        promo_code_heading.setVisibility(View.VISIBLE);
        promocode_layout.setVisibility(View.VISIBLE);
    }

    private void getLockerNumber() {
        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", Constants.TOKEN);
        params.put("location_id", new SessionManager(context).getUserLocationId());
        params.put("sessionToken", new SessionManager(context).getSessionToken());
//        params.put("business_id", Constants.BUSINESS_ID);
        params.put("signature", Signature.getUrlConversion(params));
        ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.getLockerType(), this,params, "getLockerNumber");
        confirmOrderTypeTask.ResponseTask();
    }

    @Override
    public void promoCodeStatus(String status, String message) {

        if (status.equalsIgnoreCase("success")) {
            promo_code_edittext.setCompoundDrawables(getResources().getDrawable(R.drawable.select), null, null, null);
            UtilityClass.showAlertWithOk(context, "VALID PROMO CODE", message, "promocode-success");
        } else {
            if (message.toLowerCase().contains("Promotional code is invalid:".toLowerCase())) {
                String replacedString = message.replace("Promotional code is invalid:", "");
                UtilityClass.showAlertWithOk(context, "INVALID PROMO CODE", replacedString, "promocode-error");
            } else {
                UtilityClass.showAlertWithOk(context, "INVALID PROMO CODE", message, "promocode-error");
            }
        }
    }

    @Override
    public void nearByLocations(ArrayList<LocationModel> locationModels) {
        showPopup(locationModels);
    }

    @Override
    public void updateUI(LocationModel locationModel) {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
        }

        if (locationModel.getAddress() != null) {

            if (lockerMatchCase.equalsIgnoreCase("Public locker location")) {
                nearByAddress = locationModel.getAddress();
                UtilityClass.getLocationFromAddress(nearByAddress, getContext());

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("token", Constants.TOKEN);
                params.put("address", nearByAddress);
                params.put("geolocation", new SessionManager(context).getUserGeoLocation());
//        params.put("address","30 Park Avenue");
//        params.put("geolocation", "41.8829607, -87.63414829999999");
                params.put("sessionToken", new SessionManager(context).getSessionToken());
                params.put("signature", Signature.getUrlConversion(params));
                ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.confirmOrderType(), this, params, nearByAddress,"getOrderType");
                confirmOrderTypeTask.ResponseTask();
            } else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", "Please try again", "place-order");
            }
        }




    }

    @Override
    public void updateLockerNumber(String value) {
        if (value != null) {
            new SessionManager(context).saveLockerNumber(value);
            lockerNumber = value;

            if (private_locker.getVisibility() == View.VISIBLE)
                locker_number_edittext.setText(value);

        }

    }

    private void showPopup(ArrayList<LocationModel> locationModels) {
        dialog = new Dialog(context, R.style.custom_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_order_type_popup);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.popup_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        ConfirmOrderTypeAdapter confirmOrderTypeAdapter = new ConfirmOrderTypeAdapter(context, R.layout.popup_layout, locationModels, this);
        recyclerView.setAdapter(confirmOrderTypeAdapter);

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (UtilityClass.getScreenHeight(getActivity()) * .6));

    }
    @OnClick(R.id.place_order_button)
    void submit() {

        if(lockerMatchCase.equalsIgnoreCase("Approved Private Locker")){
                    if (locker_number_edittext.getText().toString().length() > 0) {
                        new SessionManager(context).saveLockerNumber(locker_number_edittext.getText().toString());
                        claimsCreateTask();

                    }
                    else {
                        UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.toast_locker_number), "place-order");
                    }

        }else if(lockerMatchCase.equalsIgnoreCase("Approved doorman")){

            if(lockerNumber != null || !lockerNumber.equalsIgnoreCase("")) {
                new SessionManager(context).saveLockerNumber(lockerNumber);
                claimsCreateTask();
            }
            else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", "Please try again", "place-order");
            }

        }else{
            if (locker_number_edittext.getText().toString().length() > 0) {
                new SessionManager(context).saveLockerNumber(locker_number_edittext.getText().toString());
                claimsCreateTask();
            }
            else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.toast_locker_number), "place-order");
            }
        }
        
    /*    if (new SessionManager(context).getLockerNumber() != null
                || !new SessionManager(context).getLockerNumber().isEmpty()
                || !new SessionManager(context).getLockerNumber().equalsIgnoreCase("null")
                || !new SessionManager(context).getLockerNumber().equalsIgnoreCase("")) {
                    

   
        } else {
            if(lockerMatchCase.equalsIgnoreCase("Approved doorman")){
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", "Please try again", "place-order");

            }else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.toast_locker_number), "place-order");
            }
        }*/
    }

    private void claimsCreateTask() {
        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
//        SessionManager.ORDER.setLockerName(locker_number_edittext.getText().toString());

        if (shoecarelist != null) {
            String order_notes = getOrderNoteText(shoecarelist, SessionManager.ORDER.getOrderNotes());
            SessionManager.ORDER.setOrderNotes(order_notes);
        }

        new BackgroundTask(getActivity(), SessionManager.ORDER.claimsCreate(getActivity()), getFragmentManager(), "null");

    }

}

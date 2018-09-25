package com.usepressbox.pressbox.ui.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.ConfirmOrderTypeAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.asyntasks.ConfirmOrderTypeTask;
import com.usepressbox.pressbox.interfaces.IConfirmOrderTypeListener;
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
import butterknife.OnEditorAction;

/**
 * Created by Prasanth.S on 09.08.2018..
 */
public class NewLockerFragment extends Fragment implements IConfirmOrderTypeListener {

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
    private String lockerMatchCase = "private";
    private Dialog dialog;
    private String nearByAddress;
    private String lockerNumber = "1";

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("shoecarelist"))
                shoecarelist = args.getStringArrayList("shoecarelist");
        }

        lockerNumber = locker_number_edittext.getText().toString();

        /*Update style for find locker location button*/
        updateFindLocationButtonStyle();


        return v;
    }

    @OnEditorAction(R.id.locker_number_edittext)
    protected boolean lockerNumberClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            UtilityClass.hideKeyboard(getActivity());
            return true;
        }

        return false;
    }

    @OnEditorAction(R.id.promo_code_edittext)
    protected boolean promocodeClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (promo_code_edittext.getText().toString().length() > 0) {
                savePromoCodeTask();
            }
            return true;
        }

        return false;
    }

    private void getOrderType() {

        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
        if (new SessionManager(context).getUserAddress() != null) {
            if (new SessionManager(context).getUserShortAddress() != null) {
                UtilityClass.getLocationFromAddress(new SessionManager(context).getUserShortAddress(), getContext());
            } else {
                UtilityClass.getLocationFromAddress(new SessionManager(context).getUserAddress(), getContext());
            }

            if (new SessionManager(context).getUserGeoLocation() == null) {
                UtilityClass.showAlertWithOk(context, "Incorrect Address!", "Please enter your complete address", "locker");
            } else {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("token", Constants.TOKEN);
                if (new SessionManager(context).getUserShortAddress() != null) {
                    params.put("address", new SessionManager(context).getUserShortAddress());
                } else {
                    params.put("address", new SessionManager(context).getUserAddress());
                }
                params.put("geolocation", new SessionManager(context).getUserGeoLocation());
                params.put("sessionToken", new SessionManager(context).getSessionToken());
                params.put("signature", Signature.getUrlConversion(params));
                ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.confirmOrderType(), this, params, "getOrderType");
                confirmOrderTypeTask.ResponseTask();
            }

        } else {
            this.addressMatchCase("false", null);
//            UtilityClass.showAlertWithRedirect(context, "null", "Please enter your address", "myaccount");
        }
    }


    @OnClick(R.id.toolbar_left)
    void cancel() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }


    @OnClick(R.id.apply_button)
    void promoCodeApply() {
        if (promo_code_edittext.getText().toString().length() > 0) {
            savePromoCodeTask();
        } else {
            UtilityClass.showAlertWithOk(context, "Alert!", "Please enter a promo code", "promocode");
        }

    }

    private void savePromoCodeTask() {
        SessionManager.CUSTOMER.setPromoCode(promo_code_edittext.getText().toString());
        new BackgroundTask(getActivity(), SessionManager.CUSTOMER.savePromoCode(context), this, "NewLockerFragment");
    }


    /* @OnClick(R.id.find_location__button)
     void find_location() {
     }
 */
    private void getNearByLocation() {

        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();

        if (new SessionManager(context).getUserShortAddress() != null
                || new SessionManager(context).getUserAddress() != null) {
            String latitude = null;
            String longitude = null;
            String[] splited;
            try {
                if (new SessionManager(context).getUserShortAddress() != null) {
                    splited = new SessionManager(context).getUserShortAddress().split(",");
                } else {
                    splited = new SessionManager(context).getUserAddress().split(",");
                }
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
            ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.getNearByLocation(), this, params, "getNearByLocation");
            confirmOrderTypeTask.ResponseTask();
        } else {
            UtilityClass.showAlertWithOk(context, "null", "Please enter your address in account screen", "myaccount");
        }
    }


    public void setToolbarTitle() {
        skip.setText("Back");
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
                        hideConciergeView();
                        lockerMatchCase = "Approved Private Locker";
                        find_location__button.setVisibility(View.GONE);
//                        lockerNumber = null;
                    } else if (locationModel.getLocationType().equalsIgnoreCase("Concierge")
                            || locationModel.getLocationType().equalsIgnoreCase("Offices")) {
                        lockerMatchCase = "Approved doorman";
//                        lockerNumber = null;

                        hideLockerView();

                        concierge_layout.setVisibility(View.VISIBLE);

                        String locationId = locationModel.getLocation_id();
                        String address = locationModel.getAddress();

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
                        hideConciergeView();
                        showLockerView();
//                        lockerNumber = null;

                    } else {
                        lockerMatchCase = "Public locker location";
                        find_location__button.setVisibility(View.VISIBLE);
                        hideConciergeView();
                        showLockerView();
//                        lockerNumber = null;

                    }
                } else {
                    hideConciergeView();
                    lockerMatchCase = "Public locker location";
                    showLockerView();
                    find_location__button.setVisibility(View.VISIBLE);
                }
                break;
            case "false":
                hideConciergeView();
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

    private void updateFindLocationButtonStyle() {
        find_location__button.setTypeface(concierge_place_order_content.getTypeface(), Typeface.ITALIC);

        SpannableString ss = new SpannableString(getResources().getString(R.string.find_locker_location));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                /*API call*/
                getNearByLocation();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.pressbox)); // specific color for this link
            }
        };
        ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        find_location__button.setText(ss);
        find_location__button.setMovementMethod(LinkMovementMethod.getInstance());
        find_location__button.setHighlightColor(Color.TRANSPARENT);

    }

    private void showLockerView() {
        private_locker.setVisibility(View.VISIBLE);
        showPromoCodeView();
    }

    private void hideLockerView() {
        private_locker.setVisibility(View.GONE);
    }

    private void hideConciergeView() {
        concierge_layout.setVisibility(View.GONE);
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
        ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.getLockerType(), this, params, "getLockerNumber");
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
                UtilityClass.showAlertWithEmailRedirect(context, "INVALID PROMO CODE", replacedString, "myaccount");
            } else {
                UtilityClass.showAlertWithEmailRedirect(context, "INVALID PROMO CODE", message, "myaccount");
            }
        }
    }

    @Override
    public void nearByLocations(ArrayList<LocationModel> locationModels) {
        if (locationModels != null && locationModels.size() > 0) {
            showPopup(locationModels);
        } else {
            UtilityClass.showAlertWithOk(context, "Alert!", "There are currently no public locations in your city", "newLocker");

        }
    }

    @Override
    public void updateUI(LocationModel locationModel) {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
        }

        if (locationModel.getAddress() != null) {
            nearByAddress = locationModel.getAddress() + "," + locationModel.getCity() + "," + locationModel.getState() + "," + locationModel.getZipcode();
            UtilityClass.getLocationFromAddress(nearByAddress, getContext());

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("token", Constants.TOKEN);
            params.put("address", nearByAddress);
            params.put("geolocation", new SessionManager(context).getUserGeoLocation());
            params.put("sessionToken", new SessionManager(context).getSessionToken());
            params.put("signature", Signature.getUrlConversion(params));
            ConfirmOrderTypeTask confirmOrderTypeTask = new ConfirmOrderTypeTask(context, SessionManager.ORDER.confirmOrderType(), this, params, nearByAddress, "getOrderType");
            confirmOrderTypeTask.ResponseTask();
        } else {
            UtilityClass.showAlertWithOk(getActivity(), "Alert!", "Please try again", "place-order");
        }


    }

    @Override
    public void updateLockerNumber(String value) {
        if (value != null) {
            if (lockerMatchCase.equalsIgnoreCase("Approved doorman")) {

                new SessionManager(context).saveLockerNumber(value);
                if (lockerNumber.equalsIgnoreCase("0")) {
                    lockerNumber = "1";
                } else {
                    lockerNumber = value.replaceAll("[^\\d-]", "");
//                    lockerNumber = value;
                }

                if (private_locker.getVisibility() == View.VISIBLE)
                    locker_number_edittext.setText(value);

            }
        }

    }

    @Override
    public void LockerStatus(String value) {
        if (value != null) {
            if (value.toLowerCase().contains("contact support".toLowerCase())) {
                String replacedString = value.replace("contact support", "");
                UtilityClass.showAlertWithEmailRedirect(context, "Alert!", replacedString, "null");
            } else {
                UtilityClass.showAlertWithEmailRedirect(context, "Alert!", value, "null");
            }
        }
    }

    private void showPopup(ArrayList<LocationModel> locationModels) {
        dialog = new Dialog(context, R.style.custom_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_order_type_popup);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);


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

        if (lockerMatchCase.equalsIgnoreCase("Approved Private Locker")) {
            if (locker_number_edittext.getText().toString().length() > 0) {
                new SessionManager(context).saveLockerNumber(locker_number_edittext.getText().toString());
                claimsCreateTask();

            } else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.toast_locker_number), "place-order");
            }

        } else if (lockerMatchCase.equalsIgnoreCase("Approved doorman")) {

            if (lockerNumber.length() > 0) {
                new SessionManager(context).saveLockerNumber(lockerNumber);
                claimsCreateTask();
            } else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", "Please try again", "place-order");
            }

        } else {
            if (locker_number_edittext.getText().toString().length() > 0) {
                new SessionManager(context).saveLockerNumber(locker_number_edittext.getText().toString());
                claimsCreateTask();
            } else {
                UtilityClass.showAlertWithOk(getActivity(), "Alert!", getResources().getString(R.string.toast_locker_number), "place-order");
            }
        }

    }

    private void claimsCreateTask() {
        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
//        SessionManager.ORDER.setLockerName(locker_number_edittext.getText().toString());

        if (shoecarelist != null) {
            String order_notes = getOrderNoteText(shoecarelist, SessionManager.ORDER.getOrderNotes());
            SessionManager.ORDER.setOrderNotes(order_notes);
        }

        new BackgroundTask(getActivity(), SessionManager.ORDER.claimsCreate(getActivity()), this, "claims");

    }

}

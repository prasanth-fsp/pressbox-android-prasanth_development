package com.usepressbox.pressbox.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.PlacesArrayAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.asyntasks.GetUserAddressTask;
import com.usepressbox.pressbox.asyntasks.SaveUserAddressTask;
import com.usepressbox.pressbox.interfaces.IAddressStatusListener;
import com.usepressbox.pressbox.interfaces.IPromoCodeStatusListener;
import com.usepressbox.pressbox.support.GPSTracker;
import com.usepressbox.pressbox.ui.activity.order.OrderPreferences;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.ui.activity.register.ChangePassword;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by kruno on 14.04.16..
 * Modified by Prasanth.S on 27/08/2018
 * This activity is show the account details
 */
public class MyAcccount extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, IPromoCodeStatusListener, IAddressStatusListener {

    private static final String LOG_TAG = "MyAcccount";


    private String[] citys;
    private Toolbar toolbar;
    private SessionManager objSession;
    @BindView(R.id.spinner_city)
    Spinner city;
    @BindView(R.id.et_account_name)
    EditText name;
    @BindView(R.id.et_account_last_name)
    EditText lastName;
    @BindView(R.id.et_account_phone)
    EditText phone;

    @BindView(R.id.et_account_email)
    TextView email;
    @BindView(R.id.tw_my_account_payment)
    TextView addCreditCard;
    @BindView(R.id.credit_card_detail)
    LinearLayout credit_card_detail;
    @BindView(R.id.credit_card_date)
    EditText credit_card_date;
    @BindView(R.id.credit_card_CVV)
    EditText credit_card_CVV;
    @BindView(R.id.credit_card_number)
    EditText credit_card_number;
    @BindView(R.id.et_my_account_promo_code)
    EditText promo_code;
    @BindView(R.id.userAddress)
    AutoCompleteTextView mAutocompleteTextView;
    @BindView(R.id.tw_my_account_last_four_card_numbers)
    TextView tw_my_account_last_four_card_numbers;
    @BindView(R.id.terms_and_conditions)
    RelativeLayout terms_and_conditions;
    @BindView(R.id.faq)
    RelativeLayout faq;
    @BindView(R.id.pricetable)
    RelativeLayout pricetable;
    @BindView(R.id.locations)
    RelativeLayout locations;
    @BindView(R.id.privacy_policy_layout)
    RelativeLayout privacy_policy_layout;

    @BindView(R.id.relative_layout_current_card_number)
    LinearLayout relative_layout_current_card_number;
    @BindView(R.id.my_account_parent_layout)
    LinearLayout my_account_parent_layout;
    @BindView(R.id.my_account_child_layout)
    LinearLayout my_account_child_layout;
    @BindView(R.id.my_account_scrollview)
    NestedScrollView my_account_scrollview;
    @BindView(R.id.apply_button)
    Button promocode_apply_button;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private GoogleApiClient mGoogleApiClient;
    private PlacesArrayAdapter mPlaceArrayAdapter;
    private static LatLngBounds USER_BOUNDS = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private String provider;
    public Location userLocation;
    private LocationManager locationManager;


    private boolean mLocationPermissionGranted;

    FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;


    String streetAddress, shortAddress, userCity, state, country, postalCode, finalLongAddress;
    LatLng userAddressLatLong;
    SessionManager sessionManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_screen);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setToolbarTitle();
        setDateFormat();
        sessionManager = new SessionManager(this);
        /* Search address related created on 03/08/18 */

        mGoogleApiClient = new GoogleApiClient.Builder(MyAcccount.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        CallLocationData();

        getUserAddressTask();


        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlacesArrayAdapter(this, android.R.layout.simple_list_item_1,
                USER_BOUNDS, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        citys = getResources().getStringArray(R.array.citys);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, citys);
        city.setAdapter(cityAdapter);

        name.setText(SessionManager.CUSTOMER.getName());
        lastName.setText(SessionManager.CUSTOMER.getLastName());
        email.setText(SessionManager.CUSTOMER.getEmail());
        phone.setText(SessionManager.CUSTOMER.getPhone());


//        if (new SessionManager(this).getUserAddress() != null) {


        mAutocompleteTextView.setActivated(false);
        mAutocompleteTextView.setSelected(false);
        mAutocompleteTextView.setCursorVisible(false);


        objSession = new SessionManager(this);
        //set credit card last four numbers
        if (SessionManager.CUSTOMER.getCardNumber() != "0000" && SessionManager.CUSTOMER.getCardNumber() != null) {
            relative_layout_current_card_number.setVisibility(View.VISIBLE);
            String creditCard = null;
            try {
                creditCard = SessionManager.CUSTOMER.getCardNumber().substring(SessionManager.CUSTOMER.getCardNumber().length() - 4);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
//            if(creditCard != null) {
            tw_my_account_last_four_card_numbers.setText("***********" + creditCard);
//            }else {
//                tw_my_account_last_four_card_numbers.setText("");
//                relative_layout_current_card_number.setVisibility(View.GONE);
//            }
        } else {
            tw_my_account_last_four_card_numbers.setText("");
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                mAutocompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                break;
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
                updateBilling();

                if (changes()) {
                    Intent intent = new Intent(MyAcccount.this, Orders.class);
                    startActivity(intent);
                    finish();
                } else {
                    updateCustomer();

                }
            }
        });


    }

    private void getUserAddressTask() {
        if (UtilityClass.isConnectingToInternet(this)) {
            GetUserAddressTask getUserAddressTask = new GetUserAddressTask(this, this, SessionManager.CUSTOMER.details(this), "Myaccount");
            getUserAddressTask.ResponseTask();
        } else {
            UtilityClass.showAlertWithOk(this, "Incorrect Address!", "Please check your internet connection", "locker");

        }
    }

    private void loadPermissions(String perm, int requestCode) {
        //Checks if permission is granted or not
        // Log.e("PErmission", "permission"+Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    private void getUserBounds(double lat, double lng) {

        double radiusDegrees = 1.0;
        LatLng center = new LatLng(lat, lng);
        Log.e("", "" + center.latitude);
        LatLng northEast = new LatLng(center.latitude + radiusDegrees, center.longitude + radiusDegrees);
        LatLng southWest = new LatLng(center.latitude - radiusDegrees, center.longitude - radiusDegrees);
        USER_BOUNDS = LatLngBounds.builder()
                .include(northEast)
                .include(southWest)
                .build();


    }

    private void CallLocationData() {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will not proceed

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // create class object
        gps = new GPSTracker(MyAcccount.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            getUserBounds(latitude, longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }


    @OnTouch({R.id.userAddress})
    boolean touchAddress(View v, MotionEvent motionEvent) {
        mAutocompleteTextView.setActivated(true);
        mAutocompleteTextView.setSelected(true);
        mAutocompleteTextView.setCursorVisible(true);
        mAutocompleteTextView.requestFocus();
        mAutocompleteTextView.setText("");
        UtilityClass.showKeyboard(mAutocompleteTextView, this);
        return true;
    }

    @OnClick(R.id.apply_button)
    void apply_button_click() {
        savePromoCode();
    }


    @OnEditorAction(R.id.et_my_account_promo_code)
    protected boolean promocodeClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if(promo_code.getText().toString().length()>0){
                savePromoCode();
            }
            return true;
        }

        return false;
    }
    public void saveAddress() {
        if (mAutocompleteTextView.getText().toString().length() > 0) {
//            SessionManager.CUSTOMER.setAddress(mAutocompleteTextView.getText().toString());

            sessionManager.saveUserAddress(mAutocompleteTextView.getText().toString());

            /*Convert Address into LatLong*/
            /*Long address passing*/
            UtilityClass.getLocationFromAddress(mAutocompleteTextView.getText().toString(), this);
            if (sessionManager.getUserGeoLocation() != null) {
                String[] location = sessionManager.getUserGeoLocation().split(",");
                double lat = Double.parseDouble(location[0]);
                double longitude = Double.parseDouble(location[1]);

                retrieveUserSplitedAddress(lat, longitude, "longAddress");
            }

            /*Short address passing*/
            if (userAddressLatLong != null) {
                retrieveUserSplitedAddress(userAddressLatLong.latitude, userAddressLatLong.longitude, "shortAddress");
            }

            /*Update user address to API*/
            if (UtilityClass.isConnectingToInternet(MyAcccount.this)) {
                try {
                    if (userCity != null && state != null && country != null && postalCode != null) {
                        if (finalLongAddress != null) {
                            SessionManager.CUSTOMER.setStreetLongAddress(finalLongAddress);
                        }
                        if (streetAddress != null) {
                            SessionManager.CUSTOMER.setStreetAddress(streetAddress);
                        }
                        SessionManager.CUSTOMER.setUserCity(userCity);
                        SessionManager.CUSTOMER.setState(state);
                        SessionManager.CUSTOMER.setCountry(country);
                        SessionManager.CUSTOMER.setZipcode(postalCode);
                        SaveUserAddressTask addressTask = new SaveUserAddressTask(this, SessionManager.CUSTOMER.updateUSerAddress(this), "Register");
                        addressTask.ResponseTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void retrieveUserSplitedAddress(Double latitude, Double longitude, String addressType) {
        String address = null;

        /*Retrieving user splited address from latlong*/
        try {
            String[] locationData = getCityNameByCoordinates(latitude, longitude);


            userCity = locationData[0];
            state = locationData[1];
            country = locationData[2];
            postalCode = locationData[3];
            address = mAutocompleteTextView.getText().toString();

            if (addressType.equalsIgnoreCase("longAddress")) {
                if (address != null) {
                    String removedUserCity = address.replace(userCity, "");
//                    String removedUserState = removedUserCity.replaceAll(state, "");
//                    String removedUsercountry = removedUserState.replaceAll(country, "");
                    String removedUserZipcode = removedUserCity.replace(postalCode, "");
                    finalLongAddress = removedUserZipcode.replaceAll(",", "");
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @OnTouch({R.id.my_account_parent_layout, R.id.my_account_child_layout})
    boolean touch(View v, MotionEvent motionEvent) {

        UtilityClass.hideKeyboard(MyAcccount.this);
        return true;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            UtilityClass.hideKeyboard(MyAcccount.this);
            final PlacesArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }


            // Selecting the first object buffer.
            final Place place = places.get(0);
//            final Place place = places.get(1);
            CharSequence attributions = places.getAttributions();

        /*    Log.i("places ", "onResult: " +Html.fromHtml(place.getName() + " "
            + Html.fromHtml(place.getAddress() + " "+ Html.fromHtml(place.getId() + " "
                   + place.getId() + " " + place.getPhoneNumber() + "  "+
                    place.getWebsiteUri() + " " ))));*/

            try {

                Spanned address = Html.fromHtml(String.valueOf(place.getAddress()));
                shortAddress = address.toString();
                SessionManager sessionManager = new SessionManager(MyAcccount.this);
                sessionManager.saveUserShortAddress(shortAddress);


                Spanned street = Html.fromHtml(String.valueOf(place.getName()));
                streetAddress = street.toString();
                userAddressLatLong = place.getLatLng();

                /*Api call to save address*/
                saveAddress();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }


        }
    };


// ...

    private String[] getCityNameByCoordinates(double lat, double lon) throws IOException {
        String[] strng = null;

        try {
            Geocoder mGeocoder = new Geocoder(MyAcccount.this, Locale.getDefault());

            List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && addresses.size() > 0) {
                strng = new String[]{addresses.get(0).getLocality(), addresses.get(0).getAdminArea(),
                        addresses.get(0).getCountryName(), addresses.get(0).getPostalCode(), addresses.get(0).getAddressLine(0)};

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strng;
    }

    @OnClick(R.id.btn_sign_out)
    void sign_out() {

        new SessionManager(this).clearSession();
        Intent toLandingScreen = new Intent(MyAcccount.this, LandingScreen.class);
        startActivity(toLandingScreen);
        Constants.firstTime = false;
        finish();
        Orders.getInstance().finish();

    }

    @OnClick(R.id.privacy_policy_layout)
    void PrivacyPolicy() {
        UtilityClass.goToUrl(this, Constants.PRIVACY_POLICY);
    }


    @OnClick(R.id.btn_reset_password)
    void resetPassword() {

        Constants.FROM = "Myaccount";
        new BackgroundTask(MyAcccount.this, SessionManager.CUSTOMER.sendForgotPasswordEmail());

    }

    @OnClick(R.id.tw_order_preferences)
    void orderPreferences() {

        Intent orderPreferences = new Intent(MyAcccount.this, OrderPreferences.class);
        orderPreferences.putExtra("From", "MyAccount");
        startActivity(orderPreferences);
        Constants.firstTime = false;
    }

    @OnClick(R.id.tw_account_change_password)
    void changePassword() {

        Intent changePassword = new Intent(MyAcccount.this, ChangePassword.class);
        startActivity(changePassword);
    }

    @OnClick(R.id.tw_my_account_payment)
    void addCreditCard() {

        addCreditCard.setVisibility(View.GONE);
        credit_card_detail.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.terms_and_conditions)
    void termsAndConditions() {
        UtilityClass.goToUrl(this, Constants.TANDC);
//
//        Intent termsAndConditions = new Intent(MyAcccount.this, TermsAndConditions.class);
//        startActivity(termsAndConditions);
    }

    @OnClick(R.id.pricetable)
    void price() {
        if (SessionManager.CUSTOMER.getCity().equals("Chicago")) {
            Uri uri = Uri.parse("http://www.usepressbox.com/prices/chicago/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (SessionManager.CUSTOMER.getCity().equals("DC Metro")) {
            Uri uri = Uri.parse("http://www.usepressbox.com/washington-dc/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (SessionManager.CUSTOMER.getCity().equals("Philadelphia")) {
            Uri uri = Uri.parse("http://www.usepressbox.com/philadelphia/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (SessionManager.CUSTOMER.getCity().equals("Nashville")) {
            Uri uri = Uri.parse("http://www.usepressbox.com/nashville/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Uri uri = Uri.parse("https://www.usepressbox.com/dallas/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @OnClick(R.id.faq)
    void faq() {
        Uri uri = Uri.parse("https://usepressbox.zendesk.com/hc/en-us");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.locations)
    void locations() {

        Uri uri = Uri.parse("http://www.usepressbox.com/locations/chicago/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void setToolbarTitle() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    public boolean checkCreditCard() {

        boolean creditCardOk = false;
        if (credit_card_number.getText().toString().length() > 0) {
            if (credit_card_date.getText().toString().length() == 5) {
                if (credit_card_CVV.getText().toString().length() > 0) {
                    creditCardOk = true;
                }
            }
        }
        return creditCardOk;
    }

    public void updateBilling() {

        if (checkCreditCard()) {
//            if(credit_card_number.getText().toString().length()>4) {
            SessionManager.CUSTOMER.setCardNumber(credit_card_number.getText().toString());
//            }
            SessionManager.CUSTOMER.setExpMonth(credit_card_date.getText().toString().split("/")[0]);
            SessionManager.CUSTOMER.setExpYear("20" + credit_card_date.getText().toString().split("/")[1]);
            SessionManager.CUSTOMER.setCsc(credit_card_CVV.getText().toString());

            new BackgroundTask(this, SessionManager.CUSTOMER.updateBillling(this));
        }
    }

    public void setDateFormat() {

        TextWatcher mDateEntryWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*    String working = s.toString();
                if (working.length() == 2 && before == 0) {
                    working += "/";
                    credit_card_date.setText(working);
                    credit_card_date.setSelection(working.length());
                }*/
                onTextValidate(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        credit_card_date.addTextChangedListener(mDateEntryWatcher);
    }


    public void onTextValidate(CharSequence s, int start, int before, int count) {
        String working = s.toString();
        boolean isValid = true;
        if (working.length() == 2 && before == 0) {
            if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                isValid = false;
            } else {
                working += "/";
                credit_card_date.setText(working);
                credit_card_date.setSelection(working.length());
            }
        } else if (working.length() == 5 && before == 0) {
            String enteredYear = working.substring(3);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            String currentYearformat = String.valueOf(currentYear);
            currentYearformat = currentYearformat.substring(2, 4);
            currentYear = Integer.parseInt(currentYearformat);

            if (Integer.parseInt(enteredYear) < currentYear) {
                isValid = false;
            }
        } else if (working.length() != 5) {
            isValid = false;
        }

        if (!isValid) {
            credit_card_date.setError("Enter a valid date: MM/YY");
        } else {
            credit_card_date.setError(null);
        }

    }

    public boolean changes() {

        boolean changes = false;
        if (name.getText().toString().equals(SessionManager.CUSTOMER.getName())) {
            if (lastName.getText().toString().equals(SessionManager.CUSTOMER.getLastName())) {
                if (phone.getText().toString().equals(SessionManager.CUSTOMER.getPhone())) {

                    changes = true;
                }
            }
        }
        return changes;
    }

    public void updateCustomer() {

        if (name.getText().toString().length() > 0) {
            if (lastName.getText().toString().length() > 0) {
                if (phone.getText().toString().length() > 4 && phone.getText().toString().length() < 16) {
                    SessionManager.CUSTOMER.setName(name.getText().toString());
                    SessionManager.CUSTOMER.setLastName(lastName.getText().toString());
                    SessionManager.CUSTOMER.setPhone(phone.getText().toString());

                    try {
                        new BackgroundTask(this, SessionManager.CUSTOMER.updateProfile(this), "myAccount");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MyAcccount.this, getResources().getString(R.string.toast_phone_number), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MyAcccount.this, getResources().getString(R.string.toast_last_name), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MyAcccount.this, getResources().getString(R.string.toast_name), Toast.LENGTH_SHORT).show();
        }


    }

    public void savePromoCode() {
        if (promo_code.getText().toString().length() > 0) {
            SessionManager.CUSTOMER.setPromoCode(promo_code.getText().toString());
            new BackgroundTask(this, SessionManager.CUSTOMER.savePromoCode(this), this, "MyaccountClass");
        } else {
            UtilityClass.showAlertWithOk(this, "Alert!", "Please enter a promo code", "promocode");
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        saveAddress();
        updateBilling();

        if (changes()) {
            Intent intent = new Intent(MyAcccount.this, Orders.class);
            startActivity(intent);
            finish();
        } else {
            updateCustomer();
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void scrollToTop(final NestedScrollView scrollView, final View view) {

        // View needs a focus
        view.requestFocus();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int vTop = view.getTop();
                int vBottom = view.getBottom();
                int sHeight = scrollView.getHeight();
                int sTop = scrollView.getTop();
                int sBottom = scrollView.getBottom();
//                scrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
                scrollView.smoothScrollTo(0, ((sTop + sBottom) / 2) / 2);
            }
        });


    }


    /**
     * Used to scroll to the given view.
     *
     * @param scrollViewParent Parent ScrollView
     * @param view             View to which we need to scroll.
     */
    private void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    public void promoCodeStatus(String status, String message) {
        if (status.equalsIgnoreCase("success")) {
            UtilityClass.showAlertWithOk(this, "VALID PROMO CODE", message, "promocode-success");
//            promo_code.setText(promo_code.getText().toString().trim());
        } else {
            if (message.toLowerCase().contains("Promotional code is invalid:".toLowerCase())) {
                String replacedString = message.replace("Promotional code is invalid:", "");
                UtilityClass.showAlertWithEmailRedirect(this, "INVALID PROMO CODE", replacedString, "myaccount");
                } else {
                UtilityClass.showAlertWithEmailRedirect(this, "INVALID PROMO CODE", message, "myaccount");
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        // Log.e("", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //  Log.e(LOG_TAG, "Google Places API connection failed with error code: "+ connectionResult.getErrorCode());

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }


    @Override
    public void addressStatus(JSONObject jsonObject) {
        if (jsonObject != null)
            getCustomerObject(jsonObject);

    }

    public void getCustomerObject(JSONObject jsonObject) {
        SessionManager sessionManager = new SessionManager(MyAcccount.this);
        JSONObject customerObject = null;
        try {
            customerObject = jsonObject.getJSONObject("data").getJSONObject("customerDetails");
            SessionManager.CUSTOMER.setName(customerObject.getString("firstName"));
            SessionManager.CUSTOMER.setLastName(customerObject.getString("lastName"));
            SessionManager.CUSTOMER.setEmail(customerObject.getString("email"));
            SessionManager.CUSTOMER.setPhone(customerObject.getString("phone"));
            SessionManager.CUSTOMER.setCity(customerObject.getString("city"));
            SessionManager.CUSTOMER.setState(customerObject.getString("state"));
            SessionManager.CUSTOMER.setZipcode(customerObject.getString("zip"));
            if (!customerObject.getString("address2").trim().isEmpty() || customerObject.getString("address2") != null) {
                SessionManager.CUSTOMER.setStreetLongAddress(customerObject.getString("address2"));
                sessionManager.saveUserAddress("");
                sessionManager.saveUserAddress(customerObject.getString("address2") + "," +
                        customerObject.getString("city") + "," + customerObject.getString("state") + "," + customerObject.getString("zip"));
            }

            if (!customerObject.getString("address1").trim().isEmpty() || customerObject.getString("address1") != null) {
                SessionManager.CUSTOMER.setStreetAddress(customerObject.getString("address1"));
                sessionManager.saveUserShortAddress("");
                sessionManager.saveUserShortAddress(customerObject.getString("address1") + "," +
                        customerObject.getString("city") + "," + customerObject.getString("state") + "," + customerObject.getString("zip"));
            }

            if (sessionManager.getUserAddress() != null) {
                mAutocompleteTextView.setText(sessionManager.getUserAddress() + ".");
            } else {
                if (sessionManager.getUserShortAddress() != null)
                    mAutocompleteTextView.setText(sessionManager.getUserShortAddress() + ".");
            }
            SessionManager.CUSTOMER.setStarchOnShirtsId(customerObject.getString("starchOnShirts_id"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

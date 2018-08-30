package com.usepressbox.pressbox.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.PlacesArrayAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.ui.activity.order.OrderPreferences;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.ui.activity.register.ChangePassword;
import com.usepressbox.pressbox.ui.activity.register.Register;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 14.04.16..
 * Modified by Prasanth.S on 27/08/2018
 * This activity is show the account details
 */
public class MyAcccount extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

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


    private static final int GOOGLE_API_CLIENT_ID = 0;

    private GoogleApiClient mGoogleApiClient;
    private PlacesArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_screen);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setToolbarTitle();
        setDateFormat();

        /* Search address related created on 03/08/18 */
        mGoogleApiClient = new GoogleApiClient.Builder(MyAcccount.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();


        //  mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlacesArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changes()) {
                    Intent intent = new Intent(MyAcccount.this, Orders.class);
                    startActivity(intent);
                    finish();
                } else {
                    updateCustomer();

                }

                savePromoCode();
                saveAddress();
                updateBilling();
            }
        });

        name.setText(SessionManager.CUSTOMER.getName());
        lastName.setText(SessionManager.CUSTOMER.getLastName());
        email.setText(SessionManager.CUSTOMER.getEmail());
        phone.setText(SessionManager.CUSTOMER.getPhone());


        if (new SessionManager(this).getUserAddres() != null)
            mAutocompleteTextView.setText((new SessionManager(this).getUserAddres()));

        mAutocompleteTextView.setActivated(false);
        mAutocompleteTextView.setSelected(false);
        mAutocompleteTextView.setCursorVisible(false);


        objSession = new SessionManager(this);
        //set credit card last four numbers
        if (SessionManager.CUSTOMER.getCardNumber() != "0000" && SessionManager.CUSTOMER.getCardNumber() != null) {
            relative_layout_current_card_number.setVisibility(View.VISIBLE);
            String creditCard = SessionManager.CUSTOMER.getCardNumber().substring(SessionManager.CUSTOMER.getCardNumber().length() - 4);
            tw_my_account_last_four_card_numbers.setText("***********" + creditCard);
        } else {
            tw_my_account_last_four_card_numbers.setText("");
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                mAutocompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                break;
        }


    }

    @OnClick(R.id.userAddress)
    void mAutocompleteTextViewClick() {
        mAutocompleteTextView.setActivated(true);
        mAutocompleteTextView.setSelected(true);
        mAutocompleteTextView.setCursorVisible(true);
        mAutocompleteTextView.requestFocus();

    }

    public void saveAddress(){
        if (mAutocompleteTextView.getText().toString().length()>0){
            SessionManager.CUSTOMER.setAddress(mAutocompleteTextView.getText().toString());
            SessionManager sessionManager=new SessionManager(this);
            sessionManager.saveUserAddres(mAutocompleteTextView.getText().toString());
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
            CharSequence attributions = places.getAttributions();


        }
    };


    @OnClick(R.id.btn_sign_out)
    void sign_out() {

        new SessionManager(this).clearSession();
        Intent toLandingScreen = new Intent(MyAcccount.this, LandingScreen.class);
        startActivity(toLandingScreen);
        Constants.firstTime = false;
        finish();
        Orders.getInstance().finish();

    }
    @OnClick(R.id.privacy_policy_layout) void PrivacyPolicy() {
        UtilityClass.goToUrl(this,Constants.PRIVACY_POLICY);
    }


    @OnClick(R.id.btn_reset_password)
    void resetPassword() {

        Constants.FROM = "Myaccount";
        new BackgroundTask(MyAcccount.this, SessionManager.CUSTOMER.sendForgotPasswordEmail());

    }

    @OnClick(R.id.tw_order_preferences)
    void orderPreferences() {

        Intent orderPreferences = new Intent(MyAcccount.this, OrderPreferences.class);
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

        Intent termsAndConditions = new Intent(MyAcccount.this, TermsAndConditions.class);
        startActivity(termsAndConditions);
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


        Uri uri = Uri.parse("http://www.usepressbox.com/faq/");
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
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Account");
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
            SessionManager.CUSTOMER.setCardNumber(credit_card_number.getText().toString());
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
                String working = s.toString();
                if (working.length() == 2 && before == 0) {
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
            new BackgroundTask(this, SessionManager.CUSTOMER.savePromoCode(this));
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (changes()) {
            Intent intent = new Intent(MyAcccount.this, Orders.class);
            startActivity(intent);
            finish();
        } else {
            updateCustomer();
        }

        savePromoCode();
        saveAddress();
        updateBilling();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }




    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e("", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }
}

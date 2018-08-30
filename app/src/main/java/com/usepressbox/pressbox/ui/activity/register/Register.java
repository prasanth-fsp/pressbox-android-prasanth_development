package com.usepressbox.pressbox.ui.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.ui.TermsAndConditions;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;
import com.usepressbox.pressbox.utils.ValidateCheckingClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 12.04.16..
 * This activity is used for new user registration
 */
public class Register extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private Toolbar toolbar;
    private String[] citys;
    private static final String LOG_TAG = "MainActivity";
  //  private AutoCompleteTextView mAutocompleteTextView;

    @BindView(R.id.et_first_name) EditText name;
    @BindView(R.id.et_last_name) EditText lastName;
    @BindView(R.id.et_email) EditText email;
    @BindView(R.id.et_phone_number) EditText phone;
    @BindView(R.id.et_new_password) EditText password;
    @BindView(R.id.et_promo_code) EditText promoCode;
    @BindView(R.id.spinner_city) Spinner city;
    @BindView(R.id.tw_to_condition) TextView text_condition;
    @BindView(R.id.autoCompleteTextView) AutoCompleteTextView mAutocompleteTextView;

    @BindView(R.id.tc_button) TextView condition_button;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private GoogleApiClient mGoogleApiClient;
    private PlacesArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);

        setToolbarTitle();
        /* Search address related created on 03/08/18 */
        mGoogleApiClient = new GoogleApiClient.Builder(Register.this)
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
        citys = getResources().getStringArray(R.array.citys);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, citys);
        city.setAdapter(cityAdapter);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        /* Search address related end */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent landingScreen = new Intent(Register.this, LandingScreen.class);
                startActivity(landingScreen);
                finish();
            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                UtilityClass.setBusinessId(Register.this, city.getSelectedItem().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                text_condition.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                mAutocompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                break;
        }
    }

    @OnClick(R.id.btn_register) void register() {
        registerUser();
    }

    @OnClick(R.id.tw_to_condition) void Tandc() {
        UtilityClass.goToUrl(this, Constants.TANDC);
    }


    @OnClick(R.id.tw_to_login) void toLogin() {
        Intent toLogn = new Intent(Register.this, Login.class);
        startActivity(toLogn);
        finish();
    }
    @OnClick(R.id.tc_button) void toTerms() {

       startActivity(new Intent(Register.this, TermsAndConditions.class));


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

    public void registerUser(){

        if(name.getText().toString().length()>0){
            if (lastName.getText().toString().length()>0){
                if (email.getText().toString().length()>0){
                    if (new ValidateCheckingClass().emailValidate(email.getText().toString())){
                        if (phone.getText().toString().length()> 4 && phone.getText().toString().length() < 16){
                            if (password.getText().toString().length()>0){
                                if (UtilityClass.isConnectingToInternet(getApplicationContext())){

                                    SessionManager.CUSTOMER = new Customer(name.getText().toString(), lastName.getText().toString(),
                                            email.getText().toString(), phone.getText().toString(), password.getText().toString(),
                                            city.getSelectedItem().toString(), -1, "");

                                    savePromoCode();
                                    saveAddress();
                                    new BackgroundTask(this, SessionManager.CUSTOMER.create());
                                }else{
                                    Toast.makeText(Register.this, "Please check you network connection", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(Register.this, getResources().getString(R.string.toast_password), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Register.this, getResources().getString(R.string.toast_phone_number), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, getResources().getString(R.string.toast_email), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //  email length validation
                    Toast.makeText(Register.this, getResources().getString(R.string.toast_email), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Register.this, getResources().getString(R.string.toast_last_name), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Register.this, getResources().getString(R.string.toast_name), Toast.LENGTH_SHORT).show();
        }
    }

    public void savePromoCode(){
        if (promoCode.getText().toString().length()>0){
            SessionManager.CUSTOMER.setPromoCode(promoCode.getText().toString());
        }
    }
    public void saveAddress(){
        if (mAutocompleteTextView.getText().toString().length()>0){
            SessionManager.CUSTOMER.setAddress(mAutocompleteTextView.getText().toString());
            SessionManager sessionManager=new SessionManager(this);
            sessionManager.saveUserAddres(mAutocompleteTextView.getText().toString());
        }
    }
    public void setToolbarTitle(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.title_activity_registeration);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent landingScreen = new Intent(Register.this, LandingScreen.class);
        startActivity(landingScreen);
        finish();
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

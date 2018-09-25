package com.usepressbox.pressbox.ui.activity.register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.text.Html;
import android.text.Spanned;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.PlacesArrayAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.asyntasks.SaveUserAddressTask;
import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.support.GPSTracker;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.ui.TermsAndConditions;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;
import com.usepressbox.pressbox.utils.ValidateCheckingClass;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 12.04.16..
 * This activity is used for new user registration
 */
public class Register extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private Toolbar toolbar;
    private String[] citys;
    private static final String LOG_TAG = "MainActivity";
    //  private AutoCompleteTextView mAutocompleteTextView;

    @BindView(R.id.et_first_name)
    EditText name;
    @BindView(R.id.et_last_name)
    EditText lastName;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.et_phone_number)
    EditText phone;
    @BindView(R.id.et_new_password)
    EditText password;
    @BindView(R.id.et_promo_code)
    EditText promoCode;
    @BindView(R.id.spinner_city)
    Spinner city;
    @BindView(R.id.tw_to_condition)
    TextView text_condition;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView mAutocompleteTextView;

    @BindView(R.id.tc_button)
    TextView condition_button;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public Location userLocation;
    public LatLng userCurrentLocation;
    private static LatLngBounds USER_BOUNDS = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private GoogleApiClient mGoogleApiClient;
    private PlacesArrayAdapter mPlaceArrayAdapter;



    /*    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));*/
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;


    private boolean mLocationPermissionGranted;

    FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    LatLng userAddressLatLong;
    SessionManager sessionManager;

    String streetAddress, shortAddress, userCity, state, country, postalCode, finalLongAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);


        mGoogleApiClient = new GoogleApiClient.Builder(Register.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        CallLocationData();


        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlacesArrayAdapter(this, android.R.layout.simple_list_item_1,
                USER_BOUNDS, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        citys = getResources().getStringArray(R.array.citys);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, citys);
        city.setAdapter(cityAdapter);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setToolbarTitle();

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
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                text_condition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                mAutocompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                break;
        }
    }

    @OnClick(R.id.btn_register)
    void register() {
        registerUser();
    }

    @OnClick(R.id.tw_to_condition)
    void Tandc() {
        UtilityClass.goToUrl(this, Constants.TANDC);
    }


    @OnClick(R.id.tw_to_login)
    void toLogin() {
        Intent toLogn = new Intent(Register.this, Login.class);
        startActivity(toLogn);
        finish();
    }

    @OnClick(R.id.tc_button)
    void toTerms() {
        UtilityClass.goToUrl(this, Constants.TANDC);
//        startActivity(new Intent(Register.this, TermsAndConditions.class));

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
            Log.e("",""+center.latitude);
            LatLng northEast = new LatLng(center.latitude + radiusDegrees, center.longitude + radiusDegrees);
            LatLng southWest = new LatLng(center.latitude - radiusDegrees, center.longitude - radiusDegrees);
            USER_BOUNDS = LatLngBounds.builder()
                    .include(northEast)
                    .include(southWest)
                    .build();


    }
    private void CallLocationData(){
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
        gps = new GPSTracker(Register.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            getUserBounds(latitude, longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
        /*    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);*/
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
            try {
                Spanned address = Html.fromHtml(String.valueOf(place.getAddress()));
                shortAddress = address.toString();
                SessionManager sessionManager = new SessionManager(Register.this);
                sessionManager.saveUserShortAddress(shortAddress);


                Spanned street = Html.fromHtml(String.valueOf(place.getName()));
                streetAddress = street.toString();
                userAddressLatLong = place.getLatLng();
                saveAddress();

            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        }
    };

    public void registerUser() {

        if (name.getText().toString().length() > 0) {
            if (lastName.getText().toString().length() > 0) {
                if (email.getText().toString().length() > 0) {
                    if (new ValidateCheckingClass().emailValidate(email.getText().toString())) {
                        if (phone.getText().toString().length() > 4 && phone.getText().toString().length() < 16) {
                            if (password.getText().toString().length() > 0) {
                                if (UtilityClass.isConnectingToInternet(getApplicationContext())) {

                                    SessionManager.CUSTOMER = new Customer(name.getText().toString(), lastName.getText().toString(),
                                            email.getText().toString(), phone.getText().toString(), password.getText().toString(),
                                            city.getSelectedItem().toString(), -1, "");
                                    saveAddress();

                                    savePromoCode();
                                    new BackgroundTask(this, SessionManager.CUSTOMER.create(), "register");
                                } else {
                                    Toast.makeText(Register.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, getResources().getString(R.string.toast_password), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, getResources().getString(R.string.toast_phone_number), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, getResources().getString(R.string.toast_email), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //  email length validation
                    Toast.makeText(Register.this, getResources().getString(R.string.toast_email), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Register.this, getResources().getString(R.string.toast_last_name), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Register.this, getResources().getString(R.string.toast_name), Toast.LENGTH_SHORT).show();
        }
    }

    public void savePromoCode() {
        if (promoCode.getText().toString().length() > 0) {
            SessionManager.CUSTOMER.setPromoCode(promoCode.getText().toString());
        }
    }

    public void saveAddress() {
        if (mAutocompleteTextView.getText().toString().length() > 0) {
            SessionManager.CUSTOMER.setAddress(mAutocompleteTextView.getText().toString());

            sessionManager.saveUserAddress(mAutocompleteTextView.getText().toString());

            /*Convert Address into LatLong*/
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
//                UtilityClass.getLocationFromAddress(shortAddress, this);
                if (userAddressLatLong != null) {
                    retrieveUserSplitedAddress(userAddressLatLong.latitude, userAddressLatLong.longitude, "shortAddress");
                }

            /*Update user address to Model*/
                try {
                    if (userCity != null && state != null && country != null && postalCode != null) {
                        if (finalLongAddress != null) {
                            SessionManager.CUSTOMER.setStreetLongAddress(finalLongAddress);
                        } if (streetAddress != null) {
                            SessionManager.CUSTOMER.setStreetAddress(streetAddress);
                        }
                        SessionManager.CUSTOMER.setUserCity(userCity);
                        SessionManager.CUSTOMER.setState(state);
                        SessionManager.CUSTOMER.setCountry(country);
                        SessionManager.CUSTOMER.setZipcode(postalCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getCityNameByCoordinates(double lat, double lon) throws IOException {
        String[] strng = null;

        try {
            Geocoder mGeocoder = new Geocoder(Register.this, Locale.getDefault());

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

    public void setToolbarTitle() {
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


}

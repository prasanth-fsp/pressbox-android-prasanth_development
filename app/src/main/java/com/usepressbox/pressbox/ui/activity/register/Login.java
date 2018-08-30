package com.usepressbox.pressbox.ui.activity.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;
import com.usepressbox.pressbox.utils.ValidateCheckingClass;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by kruno on 12.04.16..
 * This activity handles Login process for existing user
 */
public class Login extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] citys;
    private Bundle extras;
    private SessionManager objsession;

    @BindView(R.id.et_username)
    EditText username;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.spinner_city)
    Spinner city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);
        objsession = new SessionManager(this);
        extras = getIntent().getExtras();
        if (extras != null && !extras.isEmpty()) {
            showalert();
        }

        setToolbarTitle();

        citys = getResources().getStringArray(R.array.citys);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, citys);
        city.setAdapter(cityAdapter);


        if (new SessionManager(Login.this).getBussinesId() != null) {
            city.setSelection(Arrays.asList(citys).indexOf(new SessionManager(Login.this).getBussinesId()));
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                UtilityClass.setBusinessId(Login.this, city.getSelectedItem().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent landingScreen = new Intent(Login.this, LandingScreen.class);
                startActivity(landingScreen);
                finish();
            }
        });
    }


    @OnClick(R.id.btn_sign_in)
    void login() {

        if (username.getText().toString().length() > 0) {
            if (new ValidateCheckingClass().emailValidate(username.getText().toString())) {

                if (password.getText().toString().length() > 0) {
                    if (UtilityClass.isConnectingToInternet(getApplicationContext())) {

                        if (SessionManager.CUSTOMER == null)
                            SessionManager.CUSTOMER = new Customer();
                        SessionManager.CUSTOMER.setEmail(username.getText().toString());
                        SessionManager.CUSTOMER.setPassword(password.getText().toString());
                        new BackgroundTask(this, SessionManager.CUSTOMER.validate());
                    } else {
                        Toast.makeText(Login.this, "Please check you network connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, getResources().getString(R.string.toast_password), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Please enter valid e-mail", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(Login.this, getResources().getString(R.string.toast_email), Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.tw_forgot_password)
    void forgotPassword() {

        Intent toForgetPassword = new Intent(Login.this, ForgotPassword.class);
        startActivity(toForgetPassword);
    }


    public void setToolbarTitle() {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.title_activity_login);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void attachBaseContext(Context newBase) {

super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent landingScreen = new Intent(Login.this, LandingScreen.class);
        startActivity(landingScreen);
        finish();
    }


    public void showalert() {

        if (extras.getString("from").equals("ForgotPassword")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage(getResources().getString(R.string.resetpwd_sucess)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

}

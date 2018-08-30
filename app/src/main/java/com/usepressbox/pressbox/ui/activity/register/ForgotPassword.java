package com.usepressbox.pressbox.ui.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;
import com.usepressbox.pressbox.utils.ValidateCheckingClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by kruno on 15.04.16..
 * This activity is used for ForgotPassword of the user
 */
public class ForgotPassword extends AppCompatActivity {

    private Toolbar toolbar;
    @BindView(R.id.et_reset_username)
    EditText username;
    private Bundle extras;
    private SessionManager objsession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        objsession = new SessionManager(this);
        setToolbarTitle();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    @OnClick(R.id.btn_reset_password)
    void resetUsername() {

        if (username.getText().toString().length() > 0) {

            if (SessionManager.CUSTOMER == null)
                SessionManager.CUSTOMER = new Customer();
                SessionManager.CUSTOMER.setEmail(username.getText().toString());

            if (new ValidateCheckingClass().emailValidate(username.getText().toString())) {

                if (UtilityClass.isConnectingToInternet(getApplicationContext())) {

                    new BackgroundTask(ForgotPassword.this, SessionManager.CUSTOMER.sendForgotPasswordEmail());
                } else {
                    Toast.makeText(ForgotPassword.this, "Please check you network connection", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(ForgotPassword.this, "Please enter valid e-mail", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(ForgotPassword.this, "Please enter e-mail", Toast.LENGTH_SHORT).show();
        }
    }


    public void setToolbarTitle() {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.title_activity_forgotpassword);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void resetPassword() {

        objsession.clearSession();
        Intent intent_settings = new Intent(ForgotPassword.this, Login.class);
        intent_settings.putExtra("from", "ForgotPassword");
        startActivity(intent_settings);
        ForgotPassword.this.finish();
    }
}

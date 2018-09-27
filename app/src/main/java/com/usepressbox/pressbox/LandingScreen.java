package com.usepressbox.pressbox;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.models.Customer;
import com.usepressbox.pressbox.ui.activity.order.Video;
import com.usepressbox.pressbox.ui.activity.register.Login;
import com.usepressbox.pressbox.ui.activity.register.Register;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kruno on 12.04.16..
 *  This activity will be shown when the existing user has logged in
 *  This activity will list the orders placed by the user
 */
public class LandingScreen extends AppCompatActivity {

    private SessionManager sessionManager;
    private Bundle extras;
    String code,percentage;

    @BindView(R.id.linear_layout_login) LinearLayout linear_layout_login;
    @BindView(R.id.btn_intro_video)LinearLayout introvideo;
    @BindView(R.id.btn_intro_arrow)ImageView introarrow;
    private static final int REQUEST_CODE_PERMISSION = 0;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        if (ActivityCompat.checkSelfPermission(this, mPermission)
                != MockPackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{mPermission},
                    REQUEST_CODE_PERMISSION);
        }
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);

                if (key.equals("percentage")) {
                    percentage = getIntent().getExtras().getString(key);
                }
                else if (key.equals("code")){
                    code = getIntent().getExtras().getString(key);
                }

                }
            if ( percentage !=null && code !=null) {

                sessionManager.savePercentage(percentage);
                sessionManager.saveCode(code);
            }

            }


        if (sessionManager.getBussinesId()!=null){
            UtilityClass.setBusinessId(LandingScreen.this, sessionManager.getBussinesId());
        }

        if (sessionManager.getUserName().length()>0 && sessionManager.getPassword().length()>0){

            if(SessionManager.CUSTOMER ==null) SessionManager.CUSTOMER= new Customer("", "", sessionManager.getUserName(), "", sessionManager.getPassword(), "", -1, "");
            new BackgroundTask(this, SessionManager.CUSTOMER.validate(), "login");


        }else{


            introvideo.setVisibility(View.VISIBLE);
             introarrow.setVisibility(View.VISIBLE);
            linear_layout_login.setVisibility(View.VISIBLE);
        }
    }
      @OnClick(R.id.btn_intro_video) void playvideo() {

           Intent intent = new Intent(LandingScreen.this, Video.class);
           startActivity(intent);
       }
    @OnClick(R.id.btn_login) void login() {
        Intent login = new Intent(LandingScreen.this, Login.class);
        startActivity(login);
        finish();
    }

    @OnClick(R.id.btn_register) void register() {
        Intent register = new Intent(LandingScreen.this, Register.class);
        startActivity(register);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}

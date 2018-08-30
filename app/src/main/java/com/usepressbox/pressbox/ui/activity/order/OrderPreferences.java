package com.usepressbox.pressbox.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.models.OrderPreference;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 20.04.16..
 * Modified by Prasanth.S on 08/13/2018
 * This class is used to set the order preferences to place the order
 */
public class OrderPreferences extends AppCompatActivity {

    private Toolbar toolbar;
    Boolean flag = true, flag1 = true;
    @BindView(R.id.sb_starch_level)
    SeekBar starchLevel;
    @BindView(R.id.tw_order_preferences_light)
    TextView preferences_light;
    @BindView(R.id.tw_order_preferences_medium)
    TextView preferences_medium;
    @BindView(R.id.tw_order_preferences_heavy)
    TextView preferences_heavy;
    @BindView(R.id.switch_dryer)
    android.support.v7.widget.SwitchCompat switch_dryer;
    @BindView(R.id.switch_fabric_softner)
    android.support.v7.widget.SwitchCompat switch_fabric_softner;
    @BindView(R.id.tw_order_preferences_gain)
    TextView gain;
    @BindView(R.id.tw_order_preferences_tide)
    TextView tide;
    @BindView(R.id.tw_order_preferences_charlies)
    TextView charlies;
    @BindView(R.id.btn_save_order)
    TextView save;
    private String From;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preferences);
        ButterKnife.bind(this);

        setToolbarTitle();
        setSeekerGravity();
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("From")) {
            From = getIntent().getExtras().getString("From");

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.firstTime) {
                    chechSwitchActive();
                } else {
                    if (From != null) {
                        if (From.equalsIgnoreCase("SelectService")) {
                            Intent orderPreferences = new Intent(OrderPreferences.this, NewOrder.class);
                            orderPreferences.putExtra("From", "SelectService");
                            startActivity(orderPreferences);
                            finish();
                        } else {
                            Intent toLocker = new Intent(OrderPreferences.this, MyAcccount.class);
                            startActivity(toLocker);
                            finish();
                        }
                    } else {
                        Intent toLocker = new Intent(OrderPreferences.this, MyAcccount.class);
                        startActivity(toLocker);
                        finish();
                    }
                }

            }
        });
        setPreferences();
    }

    @OnClick(R.id.tw_order_preferences_gain)
    void setGain() {

        setDetergent(getResources().getString(R.string.gain));
    }

    @OnClick(R.id.tw_order_preferences_tide)
    void setTide() {

        setDetergent(getResources().getString(R.string.tide));
    }

    @OnClick(R.id.tw_order_preferences_charlies)
    void setCharlies() {

        setDetergent(getResources().getString(R.string.charlies));
    }

    public void setToolbarTitle() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Preferences");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setSeekerGravity() {
        starchLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int p = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                p = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (p >= 0 && p < 25) {
                    p = 0;
                    starchLevel.setProgress(p);
                    setStarchTextColor(starchLevel.getProgress());

                } else if (p >= 25 && p < 75) {
                    p = 50;
                    starchLevel.setProgress(p);
                    setStarchTextColor(starchLevel.getProgress());

                } else if (p >= 75 && p <= 100) {
                    p = 100;
                    starchLevel.setProgress(p);
                    setStarchTextColor(starchLevel.getProgress());
                }
            }
        });
    }

    public void setStarchTextColor(int i) {
        if (i == 00) {
            setStarchOn(preferences_light);
            setStarchOff(preferences_medium);
            setStarchOff(preferences_heavy);
            SessionManager.CUSTOMER.setStarchOnShirtsId("2");
        }

        if (i == 50) {
            setStarchOn(preferences_medium);
            setStarchOff(preferences_light);
            setStarchOff(preferences_heavy);
            SessionManager.CUSTOMER.setStarchOnShirtsId("3");
        }

        if (i == 100) {
            setStarchOn(preferences_heavy);
            setStarchOff(preferences_medium);
            setStarchOff(preferences_light);
            SessionManager.CUSTOMER.setStarchOnShirtsId("4");
        }
    }

    public void setDetergent(String s) {

        if (s.equals(getResources().getString(R.string.gain))) {
            setDetergentOn(gain);
            setDetergentOff(tide);
            setDetergentOff(charlies);
            SessionManager.ORDER_PREFERENCE.setDetergentID("3616");
        } else if (s.equals(getResources().getString(R.string.tide))) {
            setDetergentOn(tide);
            setDetergentOff(gain);
            setDetergentOff(charlies);
            SessionManager.ORDER_PREFERENCE.setDetergentID("3117");
        } else if (s.equals(getResources().getString(R.string.charlies))) {
            setDetergentOn(charlies);
            setDetergentOff(tide);
            setDetergentOff(gain);
            SessionManager.ORDER_PREFERENCE.setDetergentID("3110");
        }
    }

    public void setDetergentOn(TextView tw) {
        tw.setBackgroundResource(R.drawable.shape_orange);
//        tw.setTextColor(ContextCompat.getColor(this, R.color.black));
        tw.setTextColor(Color.parseColor("#182085"));
        tw.setTextSize(16);
    }

    public void setDetergentOff(TextView tw) {
        tw.setBackgroundResource(R.drawable.shape);
        tw.setTextColor(ContextCompat.getColor(this, R.color.Gray));
        tw.setTextSize(13);
    }

    public void setStarchOn(TextView tw) {
        tw.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    public void setStarchOff(TextView tw) {
        tw.setTextColor(ContextCompat.getColor(this, R.color.Gray));
    }

    public void chechSwitchActive() {

        if (switch_dryer.isChecked()) {
            SessionManager.ORDER_PREFERENCE.setDryerSheetID(getResources().getString(R.string.dryer_sheet_on));
        } else {
            SessionManager.ORDER_PREFERENCE.setDryerSheetID(getResources().getString(R.string.dryer_sheet_off));
        }

        if (switch_fabric_softner.isChecked()) {
            SessionManager.ORDER_PREFERENCE.setFabricSoftnerID(getResources().getString(R.string.fabric_on));
        } else {
            SessionManager.ORDER_PREFERENCE.setFabricSoftnerID(getResources().getString(R.string.fabric_off));
        }

        updateProfile();
    }


    public void updateProfile() {

        try {
            new BackgroundTask(OrderPreferences.this, SessionManager.CUSTOMER.updateProfile(OrderPreferences.this), "orderPreferences");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDryerSheet(String s) {
        if (s.equals(getResources().getString(R.string.dryer_sheet_on))) {
            switch_dryer.setChecked(true);
        }
    }

    public void setFabricSoftner(String s) {
        if (s.equals(getResources().getString(R.string.fabric_on))) {
            switch_fabric_softner.setChecked(true);
        }
    }

    public void setPreferences() {

        if (SessionManager.ORDER_PREFERENCE == null)
            SessionManager.ORDER_PREFERENCE = new OrderPreference();

        if (SessionManager.CUSTOMER.getStarchOnShirtsId().equals("1") || SessionManager.CUSTOMER.getStarchOnShirtsId().equals("2")) {
            starchLevel.setProgress(0);
            setStarchTextColor(0);
        } else if (SessionManager.CUSTOMER.getStarchOnShirtsId().equals("3")) {
            starchLevel.setProgress(50);
            setStarchTextColor(50);
        } else if (SessionManager.CUSTOMER.getStarchOnShirtsId().equals("4")) {
            starchLevel.setProgress(100);
            setStarchTextColor(100);
        }


        setDryerSheet(SessionManager.ORDER_PREFERENCE.getDryerSheetID());
        setFabricSoftner(SessionManager.ORDER_PREFERENCE.getFabricSoftnerID());

        setDetergent(SessionManager.ORDER_PREFERENCE.getDetergentID());
    }

    @OnClick(R.id.btn_save_order)
    void saveOrder() {

        chechSwitchActive();


    }

    @Override
    public void onBackPressed() {


        if (Constants.firstTime) {
            chechSwitchActive();

        } else {
            if (From != null) {
                if (From.equalsIgnoreCase("SelectService")) {
                    Intent orderPreferences = new Intent(OrderPreferences.this, NewOrder.class);
                    orderPreferences.putExtra("From", "SelectService");
                    startActivity(orderPreferences);
                    finish();
                } else {
                    Intent toLocker = new Intent(OrderPreferences.this, MyAcccount.class);
                    startActivity(toLocker);
                    finish();
                }
            } else {
                Intent toLocker = new Intent(OrderPreferences.this, MyAcccount.class);
                startActivity(toLocker);
                finish();
            }


        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

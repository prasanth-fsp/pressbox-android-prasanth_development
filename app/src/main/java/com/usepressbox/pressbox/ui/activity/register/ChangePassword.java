package com.usepressbox.pressbox.ui.activity.register;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.usepressbox.pressbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 20.04.16..
 * This activity is used to change the password of the existing user
 */
public class ChangePassword extends AppCompatActivity {


    private Toolbar toolbar;
    @BindView(R.id.toolbar_left) TextView skip;
    @BindView(R.id.toolbar_right) TextView next;
    @BindView(R.id.toolbar_title) TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);
        ButterKnife.bind(this);

        setToolbarTitle();

    }

    @OnClick(R.id.toolbar_left) void cancel() {
        finish();
    }

    @OnClick(R.id.toolbar_right) void save() {
        finish();
    }

    public void setToolbarTitle(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Change Password");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

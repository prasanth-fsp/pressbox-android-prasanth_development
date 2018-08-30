package com.usepressbox.pressbox.ui.activity.register;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.fragment.IntroFinishFragment;
import com.usepressbox.pressbox.ui.fragment.IntroTipFragment;
import com.usepressbox.pressbox.ui.fragment.IntroWelcomeFragment;
import com.usepressbox.pressbox.utils.Constants;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 18.04.16..
 * This activity is used as Intro screen when user is logged from registration
 */
public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        ButterKnife.bind(this);

//        if (Constants.firstTime){

        Fragment fragment = new IntroTipFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
//        }else {
//            Fragment fragment = new IntroWelcomeFragment();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.fragment, fragment);
//            transaction.commit();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

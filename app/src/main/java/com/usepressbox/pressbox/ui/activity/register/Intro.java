package com.usepressbox.pressbox.ui.activity.register;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.fragment.IntroFinishFragment;
import com.usepressbox.pressbox.ui.fragment.IntroTipFragment;
import com.usepressbox.pressbox.ui.fragment.IntroWelcomeFragment;
import com.usepressbox.pressbox.ui.fragment.SelectServices;
import com.usepressbox.pressbox.utils.Constants;

import java.util.Objects;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 18.04.16..
 * N
 * This activity is used as Intro screen when user is logged from registration
 */
public class Intro extends AppCompatActivity {
    private String From ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        ButterKnife.bind(this);


        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("From")) {
            From = getIntent().getExtras().getString("From");
        } else {
            replaceFragment(new IntroFinishFragment());
        }

        if (From == null) {
            replaceFragment(new IntroFinishFragment());
        }else{
            if(From.equalsIgnoreCase("IntroFinishFragment")){
                replaceFragment(new IntroFinishFragment());
            }
        }

    }

    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
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

package com.usepressbox.pressbox.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.utils.Constants;


/*Created BY Prasanth.s on 27/09/2018*/
public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to show splash in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
         imageView=(ImageView) findViewById(R.id.splash_logo);

        //to show the splash page for a specified time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LandingScreen.class);
                startActivity(intent);
                finish();
//                }
            }
        }, Constants.SPLASH_TIME_OUT);


        AlphaAnimation animation1 = new AlphaAnimation(0.1f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(50);
        animation1.setFillAfter(true);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation1);


    }
}

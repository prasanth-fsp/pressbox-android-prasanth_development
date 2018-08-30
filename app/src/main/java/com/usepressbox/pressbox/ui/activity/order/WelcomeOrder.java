package com.usepressbox.pressbox.ui.activity.order;


/**
 * Created by root on 4/8/17.
 *  This class is used for placing new order for new user from welcome screen

 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.fragment.IntroWelcomeFragment;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.support.v4.app.FragmentTransaction;

public class WelcomeOrder extends AppCompatActivity {
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_screen);
        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            from = extras.getString("Data");
            setFragment(from);
        }

        Fragment fragment = new IntroWelcomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();

    }

    public void setFragment(String sourceString ){

        Fragment fragment = new IntroWelcomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();

        final TextView tx1 = new TextView(this);
        // tx1.setTextSize(18);
        tx1.setGravity(Gravity.CENTER_HORIZONTAL);
        tx1.setPadding(0,25,0,0);
        tx1.setAutoLinkMask(RESULT_OK);
        tx1.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                            }
                        })
                .setView(tx1);

        final AlertDialog dialog = builder.create();
        sourceString = sourceString + "\n Contact Support";
        String keyWord = "Contact Support";

        SpannableString spannableString = new SpannableString(sourceString);


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@usepressbox.com" });
                startActivity(Intent.createChooser(intent, ""));
                dialog.dismiss();
            }
        };

        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),0,tx1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan, sourceString.indexOf(keyWord), sourceString.indexOf(keyWord) + keyWord.length(), 0);

        spannableString.setSpan(new NonUnderlinedClickableSpan(keyWord), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tx1.setText(spannableString);

        dialog.show();



    }
    public class NonUnderlinedClickableSpan extends ClickableSpan {

        String clicked;
        public NonUnderlinedClickableSpan(String string) {
            super();
            clicked = string;
        }

        public void onClick(View tv) {

        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLACK);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}



package com.usepressbox.pressbox.ui.activity.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.ui.fragment.LockerFragment;
import com.usepressbox.pressbox.ui.fragment.SecureFragment;
import com.usepressbox.pressbox.ui.fragment.SelectOrderTypeFragment;
import com.usepressbox.pressbox.ui.fragment.SelectServices;
import com.usepressbox.pressbox.utils.Constants;

import java.util.Objects;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 22.04.16..
 * This class is used for placing new order for existing user in landing screen
 */
public class NewOrder extends AppCompatActivity {

    private String From ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_screen);
        ButterKnife.bind(this);

        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("From")) {
            From = getIntent().getExtras().getString("From");
        } else {
            replaceFragment(new SelectServices());
        }


        switch (Objects.requireNonNull(From)) {
            case "SelectService":
                replaceFragment(new SelectServices());
                break;
            default:
                replaceFragment(new SelectServices());


        }
    }

    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack(Constants.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    public void setFragment(String sourceString) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, new SelectServices());
        transaction.commit();

        final TextView tx1 = new TextView(this);
        tx1.setGravity(Gravity.CENTER_HORIZONTAL);
        tx1.setPadding(0, 25, 0, 0);
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
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@usepressbox.com"});
                startActivity(Intent.createChooser(intent, ""));
                dialog.dismiss();
            }
        };

        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, tx1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        /*created by Prasanth.s on 30/08/2018*/

        FragmentManager fragments = getSupportFragmentManager();

        if (fragments.getBackStackEntryCount() != 0) {
            // We have fragments on the backstack that are poppable
            if (fragments.getBackStackEntryCount() > 0) {
                fragments.popBackStackImmediate();
            } else {
                super.onBackPressed();
            }
        } else {

            if (From != null || !From.equalsIgnoreCase("null")) {
                if (From.equalsIgnoreCase("Orders")) {
                    Intent newOrder = new Intent(NewOrder.this, Orders.class);
                    newOrder.putExtra("From", "NewOrder");
                    startActivity(newOrder);
                    finish();
                }
                else{
                    Intent newOrder = new Intent(NewOrder.this, Orders.class);
                    startActivity(newOrder);
                    finish();
                }
            }else{
                Intent newOrder = new Intent(NewOrder.this, Orders.class);
                startActivity(newOrder);
                finish();
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}

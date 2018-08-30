package com.usepressbox.pressbox.support;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.usepressbox.pressbox.R;


/**
 * Created by Prasanth.S on 08/30/2018.
 */

public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }


    public static CustomProgressDialog show(Context context, boolean cancelable) {
        CustomProgressDialog dialog = new CustomProgressDialog(context, R.style.ProgressDialogTheme);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_dialog_layout);

        dialog.setCancelable(cancelable);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setWindowAnimations(R.style.dialog_zoom);
        dialog.show();
        return dialog;
    }


}


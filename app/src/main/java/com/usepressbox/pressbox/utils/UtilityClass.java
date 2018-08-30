package com.usepressbox.pressbox.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.ui.activity.order.Orders;

import java.io.IOException;
import java.util.List;

/**
 * Created by kruno on 12.04.16..
 * This class handles alert messages and date conversion
 */
public class UtilityClass {

    public static void showAlert(Activity activity, String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                        activity.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
            TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void showOkAlert(final Activity context,
                                   final String msgTitle, final String msgBody) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(msgTitle);
        alertDialogBuilder.setMessage(msgBody).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setBusinessId(Context c, String s) {
        if (s.equals("Chicago")) {
            Constants.BUSINESS_ID = "37";
        } else if (s.equals("DC Metro")) {
            Constants.BUSINESS_ID = "317";
        } else if (s.equals("Nashville")) {
            Constants.BUSINESS_ID = "361";
        } else if (s.equals("Philadelphia")) {
            Constants.BUSINESS_ID = "399";
        } else if (s.equals("Dallas")) {
            Constants.BUSINESS_ID = "426";
        }else if (s.equals("Denver")) {
            Constants.BUSINESS_ID = "150";
        }
        new SessionManager(c).saveBussinesId(s);
    }

    public static String convertDate(String s) {
        String month = "";
        switch (s) {
            case "01":
                month = "JAN";
                break;
            case "02":
                month = "FEB";
                break;
            case "03":
                month = "MAR";
                break;
            case "04":
                month = "APR";
                break;
            case "05":
                month = "MAY";
                break;
            case "06":
                month = "JUN";
                break;
            case "07":
                month = "JUL";
                break;
            case "08":
                month = "AUG";
                break;
            case "09":
                month = "SEP";
                break;
            case "10":
                month = "OCT";
                break;
            case "11":
                month = "NOV";
                break;
            case "12":
                month = "DEC";
                break;
        }
        return month;
    }

    public static void showNotificationAlert(final Activity context,
                                             final String percentage, final String code) {


        final Dialog dialog = new Dialog(context, R.style.Empty_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notification_popup);
//        dialog.getWindow().setWindowAnimations(R.style.Empty_dialog_animation);
        TextView dialog_text = (TextView) dialog.findViewById(R.id.textmsg1);
        TextView dialog_message = (TextView) dialog.findViewById(R.id.textmsg2);

        String start = context.getResources().getString(R.string.start);
//        String code = getResources().getString(R.string.code);
        String end = context.getResources().getString(R.string.end);

        String message = start + " " + code + " " + end;

        int startlen = start.length() + 1;
        int codelen = startlen + code.length();

        SpannableString span = new SpannableString(message);
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.notification)), startlen, codelen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dialog_text.setText(percentage);
        dialog_message.setText(span);


        Button ok_btn = (Button) dialog.findViewById(R.id.button);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();
                Intent i = new Intent(context, MyAcccount.class);
                context.startActivity(i);
                context.finish();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }


    public static void showAlertWithOk(final Context context, String title, String message,String from) {
        final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_alert_layout);

        TextView dialog_text = (TextView) dialog.findViewById(R.id.alert_heading);
        TextView dialog_message = (TextView) dialog.findViewById(R.id.alert_message);
        ImageView image = (ImageView) dialog.findViewById(R.id.order_image);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_button);

        if(from != null || !from.equalsIgnoreCase("null")) {
            if (from.equalsIgnoreCase("promocode-success") || from.equalsIgnoreCase("confirm-order")) {
                image.setVisibility(View.VISIBLE);
            }
        }
        /*Setting width dynamically*/
        Rect displayRectangle = new Rect();
        Window window = ((Activity)context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.85f),ViewGroup.LayoutParams.WRAP_CONTENT);

        if (title == null || title.equalsIgnoreCase("null")) {
            dialog_text.setVisibility(View.GONE);
        } else {
            dialog_text.setText(title);
        }

        if (message == null || message.equalsIgnoreCase("null")) {
            dialog_message.setVisibility(View.GONE);
        } else {
            Spanned msg = Html.fromHtml(message);
            dialog_message.setText(msg);
        }


        ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }


    public static void showAlertWithRedirect(final Context context, String title, String message, final String redirect) {
        final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_alert_layout);

        TextView dialog_text = (TextView) dialog.findViewById(R.id.alert_heading);
        TextView dialog_message = (TextView) dialog.findViewById(R.id.alert_message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_button);
        ImageView image = (ImageView) dialog.findViewById(R.id.order_image);


        /*Setting width dynamically*/
        Rect displayRectangle = new Rect();
        Window window = ((Activity)context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.85f),ViewGroup.LayoutParams.WRAP_CONTENT);

        if (title == null || title.equalsIgnoreCase("null")) {
            dialog_text.setVisibility(View.GONE);
        } else {
            dialog_text.setText(title);
        }

        if (message == null || message.equalsIgnoreCase("null")) {
            dialog_message.setVisibility(View.GONE);
        } else {
            Spanned msg = Html.fromHtml(message);
            dialog_message.setText(msg);
        }


        ok_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                if(redirect != null || !redirect.equalsIgnoreCase("null")){
                    if(redirect.equalsIgnoreCase("myaccount")){
                        Intent intent = new Intent(context, MyAcccount.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }



    // convert address into location
    public static void getLocationFromAddress(String strAddress, Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        try {
            //Get latLng from String
            if (strAddress != null) {
                address = coder.getFromLocationName(strAddress, 5);
            }

            //check for null
            if (address == null) {
                return;
            }
            Address location = address.get(0);

            SessionManager sessionManager = new SessionManager(context);
            sessionManager.saveUserGeoLocation(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* goto external url*/
    public static void goToUrl(Context context, String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }

    public static int getScreenHeight(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }


    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

}


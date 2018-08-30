package com.usepressbox.pressbox.support;

/**
 * Created by Diliban on 21/11/17.
 * This class is used get the message for firebase notification service
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.usepressbox.pressbox.LandingScreen;
import com.usepressbox.pressbox.R;



public class FireMsgService extends FirebaseMessagingService {

    String message,title;

    private static final String TAG = FireMsgService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        message = remoteMessage.getNotification().getBody();

        if(remoteMessage.getNotification().getTitle() !=null) {
            title = remoteMessage.getNotification().getTitle();
        }else{
            title = getResources().getString(R.string.app_name);
        }

        if (remoteMessage.getData().size() > 0) {
            String percentage = remoteMessage.getData().get("percentage");
            String code = remoteMessage.getData().get("code");
            handleDataMessage(percentage,code);

        }

    }



    private void handleDataMessage(String percentage,String code) {

        Intent intent = new Intent(getApplicationContext(), LandingScreen.class);
        intent.putExtra("percentage",percentage);
        intent.putExtra("code",code);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        Bitmap big_icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("By Pressbox");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setLargeIcon(big_icon)
                .setStyle(bigText);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.transparent_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.pressbox));

        } else {
            notificationBuilder.setSmallIcon(R.drawable.notification_icon);

        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1410, notificationBuilder.build());


    }

}
package com.example.shanna.orbital2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String CHANNEL_ID = "1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Notification bar on the phone -> Will only be displayed if user is not using the app
        //and app is in the background.
        //For details of notification title and body, check out index.js
        String notification_title = remoteMessage.getNotification().getTitle(); //Title contains the project's title
        String notification_body = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //unique id each time
        int notificationId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(FirebaseMessagingService.this,
                notificationId, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.rocket_earth)
                .setContentTitle(notification_title)
                .setContentText(notification_body)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build();


        //unique id each time

         NotificationManager notificationManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // notificationId is a unique int for each notification that you must define
        notificationManager1.notify(0, mBuilder);


    }
}
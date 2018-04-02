package org.edx.mobile.module.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.edx.mobile.R;
import org.edx.mobile.logger.Logger;
import org.edx.mobile.view.LaunchActivity;
import org.edx.mobile.view.SplashActivity;

/**
 * Created by marcashman on 2014-12-01.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getCanonicalName();
    private static final Logger logger = new Logger(MyFirebaseMessagingService.class);

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            Toast.makeText(this, "뒤로 갈수 없습니다.111", Toast.LENGTH_SHORT).show();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            final String messageBody = remoteMessage.getNotification().getBody();
            final String messageTitle = remoteMessage.getNotification().getTitle();
            final String messageSound = remoteMessage.getNotification().getSound();

            Log.d(TAG, "FCM Notification Message Body : " + messageBody);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getApplicationContext(), messageSound, Toast.LENGTH_SHORT).show();
                    sendNotification(messageBody, messageTitle, messageSound);
                }
            });

//            Toast.makeText(this, "뒤로 갈수 없습니다.222", Toast.LENGTH_SHORT).show();
            }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        sendNotification(remoteMessage.getNotification().getBody());
    }
    // [END receive_message]

    private void sendNotification(String messageBody, String messageTitle, String messageSound) {
        Intent intent = new Intent(this, LaunchActivity.class);
/* Intent intent = new Intent(this, SplashActivity.class); */
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_new)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if(messageSound != null){
            notificationBuilder.setSound(defaultSoundUri);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
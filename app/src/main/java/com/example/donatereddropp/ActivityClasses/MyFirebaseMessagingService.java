package com.example.donatereddropp.ActivityClasses;

import static com.example.donatereddropp.ActivityClasses.Chat.msgtext;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.donatereddropp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String msgtext = Chat.msgtext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            // Check if the notification click action is "OPEN_CHAT"
            if ("OPEN_CHAT".equals(data.get("click_action"))) {
                // Retrieve sender ID from the notification payload
                String senderId = data.get("senderId");

                // Open the sender's chat (you may need to fetch additional chat details)
                Intent chatIntent = new Intent(this, Chat.class);
                chatIntent.putExtra("senderId", senderId);
                chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this, 0, chatIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                );

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // Create and display the notification
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.baseline_notifications_24)
                        .setContentTitle("Red Drop")
                        .setContentText(msgtext)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }
        }


    }
}


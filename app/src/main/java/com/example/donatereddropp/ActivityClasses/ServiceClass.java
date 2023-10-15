package com.example.donatereddropp.ActivityClasses;

import static com.example.donatereddropp.ActivityClasses.Chat.msgtext;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.donatereddropp.R;

public class ServiceClass extends Service {
    MediaPlayer player;
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (player == null) {
                    player = MediaPlayer.create(ServiceClass.this, Settings.System.DEFAULT_RINGTONE_URI);
                    player.setLooping(true);
                    player.start();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        } else {
            player.start();
        }
    }

    private Notification createNotification() {


        Intent notificationIntent = new Intent(this, MyFirebaseMessagingService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(this, "ChannelId")
                .setContentTitle("Red Drop")
                .setContentText(msgtext)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .build();

    }
}

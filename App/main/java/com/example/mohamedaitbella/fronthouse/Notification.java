package com.example.mohamedaitbella.fronthouse;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification extends FirebaseMessagingService {


    public Notification() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remotemessage) {
        RemoteMessage rm = remotemessage;

        String message = "";
        if(rm.getNotification() != null) {
            try {
                JSONObject json = new JSONObject(rm.getData());
                String message2 = json.getString("body");
                Log.d("JSON2", "Body: " + message);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSON2", "Catching notification failed");
            }
            message = rm.getNotification().getBody();
            Log.d("JSON", "Notification: " + message);
        }

        if(rm.getData().size() > 0) {
            Log.d("JSON", "Starts");
            try {
                JSONObject json = new JSONObject(rm.getData());
                String message2 = json.getString("extra_information");
                Log.d("JSON", "Body: " + message2);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSON", "Catching notification failed");
            }
        }
        Intent intent = null;

        // If sent from JSON and in foreground
        if(rm.getNotification() != null) {
            if(rm.getNotification().getClickAction() != null) {
                if (rm.getNotification().getClickAction().equals("Main2Activity"))
                    intent = new Intent(this, Main2Activity.class);
                //else if (rm.getNotification().getClickAction().equals("Main3Activity"))
                    //intent = new Intent(this, Main3Activity.class);
                else
                    intent = new Intent(this, MainActivity.class);
            }
            else
                Log.d("NOT_INENT", "Switching didn't work. Check for typos");
        }

        sendNotification(message, rm.getNotification().getTitle(), intent);

        // Look at title to determine which page to go to
        // Types: Manager receives request, Worker receives responce,
    }

    private void sendNotification(String messageBody, String title, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        // Channel/type of notification
        String channelId = getString(R.string.default_notification_channel_id);
        CharSequence channel = "Default";
        int importance = NotificationManager.IMPORTANCE_LOW;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channel, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        /////////////////////////////////////////////////////////////////////////////////////

        // Appearance and behavior
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setColor(0);
        ///////////////////////////////////////////////////////////////////////////////////

        // Where to go next
        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
        }
        ///////////////////////////////////////////////////////////////////////////////////


        // Run
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}

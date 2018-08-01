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

import com.google.firebase.messaging.FirebaseMessaging;
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
                // This should be else statement (excluding the 'RequestID' portion)
                if (rm.getNotification().getClickAction().equals("Home")) {
                    if(rm.getData()!=null &&rm.getData().get("RequestID")!=null)
                        FirebaseMessaging.getInstance().subscribeToTopic(rm.getData().get("RequestID") );
                    intent = new Intent(this, Home.class);
                    if(rm.getData().get("action") == null)
                        intent.putExtra("action", "Schedule");
                    else
                        intent.putExtra("action", rm.getData().get("action"));
                }
                else if (rm.getNotification().getClickAction().equals("MyAvailability"))
                    intent = new Intent(this, MyAvailability.class);
                else if(rm.getNotification().getClickAction().equals("Manager")){
                    Log.d("NOTIFY", "HERE" );
                    intent = new Intent(this, Manager.class);
                    intent.putExtra("RequestType", rm.getData().get("RequestType"));
                    intent.putExtra("Name1", rm.getData().get("Employee1"));
                    intent.putExtra("Shift1", rm.getData().get("Shift"));
                    intent.putExtra("RequestID", Integer.parseInt(rm.getData().get("RequestID")));
                    if(rm.getData().get("Employee2") != null) {
                        intent.putExtra("Name2", rm.getData().get("Employee2"));
                        intent.putExtra("Shift2", rm.getData().get("Shift2"));
                    }
                }
                else
                    intent = new Intent(this, Home.class);
                Log.d("NOTIFY", "CLICK-ACTION = " + rm.getNotification().getClickAction());
            }
            else
                Log.d("NOT_INENT", "Switching didn't work. Check for typos");
        }

        sendNotification(message, rm.getNotification().getTitle(), intent);

        // Look at Extras to determine which page to go to
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
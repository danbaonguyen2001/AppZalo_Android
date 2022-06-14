package hcmute.danbaonguyen19110036.appzalo.firebase;


import android.content.Intent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import hcmute.danbaonguyen19110036.appzalo.Activities.MainActivity;
import hcmute.danbaonguyen19110036.appzalo.R;

import hcmute.danbaonguyen19110036.appzalo.Activities.VideoCallInComingActivity;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;

public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = MessagingService.class.getName();
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e(TAG,token);
        Log.d("FCM","Token:"+token);

    }

    @Override

    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        sendNotification(notification.getTitle(),notification.getBody());

        String type = remoteMessage.getData().get("type");
        System.out.println(type);
        if(type!=null){
            if(type.equals("invitation")){
                Intent intent = new Intent(getApplicationContext(),VideoCallInComingActivity.class);
                intent.putExtra("meetingType",remoteMessage.getData().get("meetinType"));
                intent.putExtra("receiverName",remoteMessage.getData().get("receiverName"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

package hcmute.danbaonguyen19110036.appzalo.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hcmute.danbaonguyen19110036.appzalo.Activities.VideoCallInComingActivity;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM","Token:"+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent =new Intent(getApplicationContext(), VideoCallInComingActivity.class);
        startActivity(intent);
        if(message.getNotification()!=null){
            Log.d("FCM","Remote message received:"+message.getNotification().getBody());
        }
    }
}

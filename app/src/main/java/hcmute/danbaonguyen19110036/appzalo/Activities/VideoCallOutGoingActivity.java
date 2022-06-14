package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;
import hcmute.danbaonguyen19110036.appzalo.Utils.PreferenceManager;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;
import hcmute.danbaonguyen19110036.appzalo.network.ApiClient;
import hcmute.danbaonguyen19110036.appzalo.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCallOutGoingActivity extends AppCompatActivity {
    private ImageView receiver_avt;
    private TextView receiver_name;
    private String receiver_url,receiver_token,receiver_uid,type,inviter_token;
    private FloatingActionButton btnEndCall;
    private DatabaseReference reference;
    private User user;

    private String meetingType="video";


    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_out_going);

        preferenceManager =new PreferenceManager(getApplicationContext());


        receiver_avt=findViewById(R.id.img_avatar_receiver);
        receiver_name= findViewById(R.id.txt_username_receiver);
        btnEndCall= (FloatingActionButton) findViewById(R.id.btn_end_call);
        btnEndCall.setColorFilter(Color.WHITE);

        receiver_token=getIntent().getStringExtra("receiver_token");

        //initiateMeeting("dYwjeaCwRxOUPqUXwCbE3-:APA91bEXZpiHM5QQYCvv-d-p5uayRXM9kaGZeHZmtXHUuVAU6pfZwMZxQiyebkxhybcUc7sYdm2LBdebShsqoWynWm5CiHEAM06-mwljbUFTHTlk2RaY8TlDvG_M-llfoIsHHoOHsDN6");

        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(meetingType!=null && user!=null){
            initiateMeeting(meetingType,user.getToken());
        }
    }

    private void initiateMeeting(String meetingType,String receiverToken){
        try {
            JSONArray tokens= new JSONArray();

            tokens.put(receiverToken);

            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();

            data.put("type",AllConstants.REMOTE_MSG_INVITATION);
            data.put("meetingType",meetingType);
            data.put("receiverName","userName");
            data.put("inviterToken",inviter_token);

            body.put("data",data);
            body.put(AllConstants.REMOTE_MSG_REGISTRATION_IDS,tokens);

            sendRemoteMessage(body.toString(),AllConstants.REMOTE_MSG_INVITATION);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody,String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                AllConstants.getRemoteMessageHeaders(),remoteMessageBody)
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
               if(type.equals(AllConstants.REMOTE_MSG_INVITATION)){
                   Toast.makeText(VideoCallOutGoingActivity.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(VideoCallOutGoingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                   finish();
               }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(VideoCallOutGoingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
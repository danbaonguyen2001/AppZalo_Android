package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private String meetingRoom=null;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_out_going);

        preferenceManager =new PreferenceManager(getApplicationContext());
        inviter_token = Util.currentUser.getToken();

        receiver_avt=findViewById(R.id.img_avatar_receiver);
        receiver_name= findViewById(R.id.txt_username_receiver);
        btnEndCall= (FloatingActionButton) findViewById(R.id.btn_end_call);
        btnEndCall.setColorFilter(Color.WHITE);
        receiver_token=getIntent().getStringExtra("receiver_token");
        //initiateMeeting("dYwjeaCwRxOUPqUXwCbE3-:APA91bEXZpiHM5QQYCvv-d-p5uayRXM9kaGZeHZmtXHUuVAU6pfZwMZxQiyebkxhybcUc7sYdm2LBdebShsqoWynWm5CiHEAM06-mwljbUFTHTlk2RaY8TlDvG_M-llfoIsHHoOHsDN6");

        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelInvitation(receiver_token);
                onBackPressed();
            }
        });
        if(meetingType!=null){
            initiateMeeting(meetingType,receiver_token);
        }
    }

    private void initiateMeeting(String meetingType,String receiverToken){
        try {
            meetingRoom=Util.currentUser.getUserName()+"_"+Util.currentUser.getBirthDay();
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            JSONArray tokens= new JSONArray();
            tokens.put(receiverToken);
            data.put("type", "invitation");
            data.put("meetingType",meetingType);
            data.put("receiverName","TEST");
            data.put("inviterToken",Util.currentUser.getToken());
            data.put(AllConstants.REMOTE_MSG_MEETING_ROOM,meetingRoom);
            JSONObject notificationData = new JSONObject();
            notificationData.put("data", data);
            notificationData.put("registration_ids",tokens);
            JsonObjectRequest request = new JsonObjectRequest(AllConstants.NOTIFICATION_URL, notificationData,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("OK");
                            System.out.println(notificationData);
                            Toast.makeText(VideoCallOutGoingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VideoCallOutGoingActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "key=" + AllConstants.SERVER_KEY);
                    map.put("Content-Type", "application/json");
                    return map;
                }
            };
            queue.add(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody,String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                AllConstants.getRemoteMessageHeaders(),remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if(response.isSuccessful()){
                            if(type.equals(AllConstants.REMOTE_MSG_INVITATION)){
                                Toast.makeText(VideoCallOutGoingActivity.this,
                                        "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                            }else if(type.equals(AllConstants.REMOTE_MSG_INVITATION_RESPONSE)){
                                Toast.makeText(VideoCallOutGoingActivity.this, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else{
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

    private void cancelInvitation(String receiverToken){
        try {
            JSONArray tokens=new JSONArray();
            tokens.put(receiverToken);
            JSONObject data= new JSONObject();
            JSONObject body= new JSONObject();
            data.put(AllConstants.REMOTE_MSG_TYPE,AllConstants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(AllConstants.REMOTE_MSG_INVITATION_RESPONSE,AllConstants.REMOTE_MSG_INVITATION_CANCELLED);
            body.put(AllConstants.REMOTE_MSG_DATA,data);
            body.put(AllConstants.REMOTE_MSG_REGISTRATION_IDS,tokens);

            sendRemoteMessage(body.toString(),AllConstants.REMOTE_MSG_INVITATION_RESPONSE);

        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String  type= intent.getStringExtra(AllConstants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null){
                if(type.equals(AllConstants.REMOTE_MSG_INVITATION_ACCEPTED)){
                    try{
                        URL serverURL=new URL("http://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions=
                                new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setRoom("GIANG456")
                                        .build();
                        JitsiMeetActivity.launch(VideoCallOutGoingActivity.this,conferenceOptions);
                        finish();
                    }
                    catch (Exception e){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else if(type.equals(AllConstants.REMOTE_MSG_INVITATION_REJECTED)){
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(AllConstants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}
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

import android.widget.Toast;

import android.view.Window;
import android.view.WindowManager;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;
import hcmute.danbaonguyen19110036.appzalo.network.ApiClient;
import hcmute.danbaonguyen19110036.appzalo.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCallInComingActivity extends AppCompatActivity {

    FloatingActionButton btnDecline,btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_call_in_coming);
        btnDecline=findViewById(R.id.btn_decline_call);
        btnAccept=findViewById(R.id.btn_accept_call);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(
                        AllConstants.REMOTE_MSG_INVITATION_REJECTED,
                        getIntent().getStringExtra(AllConstants.REMOTE_MSG_INVITER_TOKEN)
                );
                onBackPressed();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(
                        AllConstants.REMOTE_MSG_INVITATION_ACCEPTED,
                        getIntent().getStringExtra(AllConstants.REMOTE_MSG_INVITER_TOKEN)
                );
            }
        });

    }

    private void sendInvitationResponse(String type, String receiverToken){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            JSONArray tokens= new JSONArray();
            tokens.put(receiverToken);
            data.put(AllConstants.REMOTE_MSG_TYPE,AllConstants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(AllConstants.REMOTE_MSG_INVITATION_RESPONSE,type);
            JSONObject notificationData = new JSONObject();
            notificationData.put(AllConstants.REMOTE_MSG_DATA, data);
            notificationData.put(AllConstants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            JsonObjectRequest request = new JsonObjectRequest(AllConstants.NOTIFICATION_URL, notificationData,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(type.equals(AllConstants.REMOTE_MSG_INVITATION_ACCEPTED)){
                                try{
                                    URL serverURL=new URL("http://meet.jit.si");
                                    JitsiMeetConferenceOptions conferenceOptions=
                                            new JitsiMeetConferenceOptions.Builder()
                                                    .setServerURL(serverURL)
                                                    .setRoom("GIANG456")
                                                    .build();
                                    JitsiMeetActivity.launch(VideoCallInComingActivity.this,conferenceOptions);
                                    finish();
                                }
                                catch (Exception e){
                                    Toast.makeText(VideoCallInComingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(VideoCallInComingActivity.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VideoCallInComingActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String  type= intent.getStringExtra(AllConstants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null){
                if(type.equals(AllConstants.REMOTE_MSG_INVITATION_CANCELLED)){
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
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
package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONObject;

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

    private String meetingType="video";


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
                onBackPressed();
            }
        });
        if(meetingType!=null){
            initiateMeeting(meetingType,receiver_token);
        }
    }

    private void initiateMeeting(String meetingType,String receiverToken){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            JSONArray tokens= new JSONArray();
            tokens.put(receiverToken);
            data.put("type", "invitation");
            data.put("meetingType",meetingType);
            data.put("receiverName","TEST");
            data.put("inviterToken",Util.currentUser.getToken());
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
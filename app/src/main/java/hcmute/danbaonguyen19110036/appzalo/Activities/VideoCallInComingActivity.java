package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class VideoCallInComingActivity extends AppCompatActivity {
    public String groupId;
    private ImageView avatar;
    private TextView userName;
    private FirebaseDatabase firebaseDatabase;
    FloatingActionButton btnDecline,btnAccept;  //lưu button chấp nhận và từ chối cuộc gọi video từ bạn bè để xử lý sự kiện click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_call_in_coming);
        initData();
        groupId = getIntent().getStringExtra(AllConstants.REMOTE_ROOM_ID);
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    if(user.getToken().equals(getIntent().getStringExtra(AllConstants.REMOTE_MSG_INVITER_TOKEN))){
                        Picasso.get().load(user.getImg()).into(avatar);
                        userName.setText(user.getUserName());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //xử lý sự kiện click vào button từ chối cuộc gọi
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
        //xử lý sự kiện click vào button chấp nhận cuộc gọi
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
    public void initData(){
        avatar = findViewById(R.id.img_avatar_receiver);
        userName = findViewById(R.id.txt_username_receiver);
        btnDecline=findViewById(R.id.btn_decline_call);
        btnAccept=findViewById(R.id.btn_accept_call);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    //Gửi phản hồi cuộc gọi video lại cho người mời
    private void sendInvitationResponse(String type, String receiverToken){
        try {
            // Call API
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            JSONArray tokens= new JSONArray();
            tokens.put(receiverToken);
            data.put(AllConstants.REMOTE_ROOM_ID,groupId);
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
                                    Intent it = new Intent(VideoCallInComingActivity.this,JitsiMeetViewActivity.class);
                                    Util.groupId=groupId;
                                    startActivity(it);
                                    finish();
                                }
                                catch (Exception e){
                                    Toast.makeText(VideoCallInComingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(VideoCallInComingActivity.this,JitsiMeetViewActivity.class));
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

    //Gửi phản hồi đến người gửi khi người dùng từ chối yêu cầu gọi video
    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type= intent.getStringExtra(AllConstants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null){
                if(type.equals(AllConstants.REMOTE_MSG_INVITATION_CANCELLED)){
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };
    //Đăng ký sự kiện gửi phản hồi yêu cầu gọi video khi activity đang được onStart
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(AllConstants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }
    //Huỷ đăng ký sự kiện gửi phản hồi yêu cầu gọi video khi activity rơi vào onStop
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }

}
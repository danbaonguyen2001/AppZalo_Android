package hcmute.danbaonguyen19110036.appzalo.Activities;

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

import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;
import hcmute.danbaonguyen19110036.appzalo.Utils.PreferenceManager;
import hcmute.danbaonguyen19110036.appzalo.network.ApiClient;
import hcmute.danbaonguyen19110036.appzalo.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCallOutGoingActivity extends AppCompatActivity {
    private ImageView receiver_avt;
    private TextView receiver_name;
    private String receiver_url,receiver_token,receiver_uid,type;
    private FloatingActionButton btnEndCall;
    private DatabaseReference reference;


    private PreferenceManager preferenceManager;
    private String inviterToken="dNx9ak7SRNuXa_Jqip6BaG:APA91bG9SUHikYKEiMwb8guSCa3af95D-FmuXy29SXartRKVugTijWnPdoTCOSUuTwna7LvQTj4PoE-CwOOOXHGGuAP0iZZNXnIKCD0pSV-P3Hkoit6wZof82tx5u3bx_jp7EJaGyN_M";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_out_going);

        //Lấy token của user hiện tại

        receiver_avt=findViewById(R.id.img_avatar_receiver);
        receiver_name= findViewById(R.id.txt_username_receiver);
        btnEndCall= (FloatingActionButton) findViewById(R.id.btn_end_call);
        btnEndCall.setColorFilter(Color.WHITE);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            receiver_uid=bundle.getString("uid");
        }else{
            Toast.makeText(this, "Data missing", Toast.LENGTH_SHORT).show();
        }

        initiateMeeting("dNx9ak7SRNuXa_Jqip6BaG:APA91bG9SUHikYKEiMwb8guSCa3af95D-FmuXy29SXartRKVugTijWnPdoTCOSUuTwna7LvQTj4PoE-CwOOOXHGGuAP0iZZNXnIKCD0pSV-P3Hkoit6wZof82tx5u3bx_jp7EJaGyN_M");

        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initiateMeeting(String receiver_token){
        try {
            JSONArray tokens= new JSONArray();
            tokens.put(receiver_token);
            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();
            body.put("data",data);
            body.put("registation",tokens);

        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage("123","123")
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
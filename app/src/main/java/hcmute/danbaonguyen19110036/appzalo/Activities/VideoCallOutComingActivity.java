package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hcmute.danbaonguyen19110036.appzalo.R;

public class VideoCallOutComingActivity extends AppCompatActivity {
    ImageView receiver_avt;
    TextView receiver_name;
    String receiver_url,receiver_token,receiver_uid,type;
    FloatingActionButton btnEndCall;
    DatabaseReference reference;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_out_coming);



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

    }
}
package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hcmute.danbaonguyen19110036.appzalo.R;

public class VideoCallInComingActivity extends AppCompatActivity {

    FloatingActionButton btnDecline,btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_in_coming);
        btnDecline=findViewById(R.id.btn_decline_call);
        btnAccept=findViewById(R.id.btn_accept_call);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
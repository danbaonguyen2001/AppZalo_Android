package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hcmute.danbaonguyen19110036.appzalo.R;

public class ChatboxActivity extends AppCompatActivity {
    private TextView username;
    private ImageView btnBack;
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chatbox);
        initData();
        // lấy giá trị trong intent lưu vào TextView
        username.setText(getIntent().getStringExtra("username"));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set sự kiện quay lại TabChat
                startActivity(new Intent(ChatboxActivity.this,MainActivity.class));
            }
        });
    }
    // Khởi tạo các giá trị ban đầu
    private void initData(){
        username = findViewById(R.id.chatbox_username);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        btnBack = findViewById(R.id.chatbox_arrowback);
    }
}
package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Adapter.ChatAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ChatboxActivity extends AppCompatActivity {
    // Khai báo cáo View để xử lý sự kiện
    private TextView username;
    private ImageView btnBack;
    private RecyclerView recyclerView;
    private EditText enterMessage;
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatboxActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(messageList,ChatboxActivity.this);
        recyclerView.setAdapter(chatAdapter);
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    Message message = dsp.getValue(Message.class);
                    messageList.add(message);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        recyclerView = findViewById(R.id.chatbox_recycler_chat);
        enterMessage = findViewById(R.id.enterMessage);
        messageList = new ArrayList<>();
    }
    public void SendMessage(View view){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages");
        String key = databaseReference.push().getKey();
        String senderId = firebaseAuth.getCurrentUser().getUid();
        String groupId= getIntent().getStringExtra("roomId");
        String text = enterMessage.getText().toString();
        if(text.isEmpty()){
            Toast.makeText(ChatboxActivity.this,"Vui lòng nhập tin nhắn",Toast.LENGTH_SHORT).show();
            return;
        }
        Message message = new Message(key,groupId,senderId,text);
        databaseReference.child(key).setValue(message);
        messageList.add(message);
        enterMessage.setText("");
        chatAdapter.notifyDataSetChanged();
    }
}
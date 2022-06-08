package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Adapter.ChatAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ChatboxActivity extends AppCompatActivity {
    // Khai báo cáo View để xử lý sự kiện
    private TextView username;
    private ImageView btnBack,selectImg;
    private RecyclerView recyclerView;
    private EditText enterMessage;
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    private String groupId;
    private static int PICK_IMAGE=123;
    private Uri imagepath;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private String imageToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chatbox);
        initData();
        // lấy giá trị trong intent lưu vào TextView
        groupId= getIntent().getStringExtra("roomId");
        username.setText(getIntent().getStringExtra("username"));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatboxActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(messageList,ChatboxActivity.this);
        recyclerView.setAdapter(chatAdapter);
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
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
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }
    // Khởi tạo các giá trị ban đầu
    private void initData(){
        username = findViewById(R.id.chatbox_username);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        btnBack = findViewById(R.id.chatbox_arrowback);
        recyclerView = findViewById(R.id.chatbox_recycler_chat);
        enterMessage = findViewById(R.id.enterMessage);
        selectImg = findViewById(R.id.select_img);
        messageList = new ArrayList<>();
    }
    public void SendMessage(View view){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
        String key = databaseReference.push().getKey();
        String senderId = firebaseAuth.getCurrentUser().getUid();
        String text = enterMessage.getText().toString();
        if(text.isEmpty()){
            Toast.makeText(ChatboxActivity.this,"Vui lòng nhập tin nhắn",Toast.LENGTH_SHORT).show();
            return;
        }
        Message message = new Message(key,groupId,senderId,text,"text","");
        databaseReference.child(key).setValue(message);
        messageList.add(message);
        enterMessage.setText("");
        chatAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            sendImageMessage(imagepath);
        }
    }
    public void sendImageMessage(Uri imagepath){
        String timeStamp = ""+System.currentTimeMillis();
        String fillNamepath = "ChatImages/"+"post_"+timeStamp;
        StorageReference imgref = storageReference.child("Images").child(fillNamepath);
        Bitmap bitmap=null;
        if(imagepath==null){
            Toast.makeText(this,"Vui lòng chọn ảnh của bạn",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();
        UploadTask uploadTask=imgref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageToken=uri.toString();
                        Toast.makeText(getApplicationContext(),"URI get sucess",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"URI get Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"Image is uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image Not UPdloaded",Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
        String key = databaseReference.push().getKey();
        String senderId = firebaseAuth.getCurrentUser().getUid();
        Message message = new Message(key,groupId,senderId,"","image",imageToken);
        databaseReference.child(key).setValue(message);
        messageList.add(message);
        chatAdapter.notifyDataSetChanged();
    }
}
package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ChatAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.Group;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class ChatboxActivity extends AppCompatActivity {
    // Khai báo cáo View để xử lý sự kiện
    private TextView username;
    private ImageView btnBack,selectImg,sendBtn,avatar,camera;
    private RecyclerView recyclerView;
    private EditText enterMessage;

    private ConstraintLayout containerChatbox;
    //Các biến thực thi chức năng call video
    private ImageView imageViewVideoCall;
    private String receiver_name,receiver_uid,sender_uid,url,usertoken;
    Uri uri;

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
    private String imageToken,receiverId;
    private RecordButton micro;
    private RecordView recordView;
    private MediaRecorder mediaRecorder;
    private String audioPath;
    private Group group;
    private User receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chatbox);
        initData();
        // lấy giá trị trong intent lưu vào TextView
        setInformation();
        receiverId =getIntent().getStringExtra("receiverId");
        System.out.println(receiverId);
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
                recyclerView.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //Video call giữa 2 user

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            url=bundle.getString("u");
            receiver_name=bundle.getString("n");
            receiver_uid=bundle.getString("uid");
        }else{
            Toast.makeText(this, "user missing,", Toast.LENGTH_SHORT).show();
        }

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        sender_uid=user.getUid();

        imageViewVideoCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent= new Intent(ChatboxActivity.this, test.class);
                intent.putExtra("uid",receiver_uid);

                startActivity(intent);

//                try {
//                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                            .setServerURL(new URL("https://meet.jit.si"))
//                            .setRoom("test")
//                            .setAudioMuted(false)
//                            .setVideoMuted(false)
//                            .setAudioOnly(false)
//                            .build();
//
//                    JitsiMeetActivity.launch(ChatboxActivity.this,options);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }

            }
        });


        //Chụp ảnh và gửi đi
            //Yêu cầu cho phép camera
        if(ContextCompat.checkSelfPermission(ChatboxActivity.this,Manifest.permission.CAMERA)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChatboxActivity.this,
                    new String[]{
                Manifest.permission.CAMERA
            },100);
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mở camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set sự kiện quay lại TabChat
                finish();
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        micro.setOnClickListener(view-> {
            if (Util.recordingOk(ChatboxActivity.this))
            {
                if (Util.isStorageOk(ChatboxActivity.this))
                {
                    micro.setListenForRecord(true);
                }
                Util.requestStorage(ChatboxActivity.this);
            }
            else
            {
                Util.requestRecording(ChatboxActivity.this);
            }
            if(!Util.isPermissionGranted(ChatboxActivity.this)){
                new AlertDialog.Builder(this)
                        .setTitle("Allfilepermission")
                        .setMessage("Android 11")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                takePermission();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                Toast.makeText(this,"Permission already granted",Toast.LENGTH_SHORT).show();
            }
        });
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                setUpRecording();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                enterMessage.setVisibility(View.GONE);
                recordView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancel() {
                //On Swipe To Cancel
                mediaRecorder.reset();
                mediaRecorder.release();
                File file = new File(audioPath);
                if (file.exists())
                    file.delete();
                recordView.setVisibility(View.GONE);
                enterMessage.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFinish(long recordTime) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                } catch (RuntimeException stopException) {
                    System.out.println("123");
                    stopException.printStackTrace();
                }
                recordView.setVisibility(View.GONE);
                enterMessage.setVisibility(View.VISIBLE);
                sendRecordingMessage(audioPath);
            }
            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                mediaRecorder.reset();
                mediaRecorder.release();
                recordView.setVisibility(View.GONE);
            }
        });
        enterMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                micro.setVisibility(View.GONE);
                sendBtn.setVisibility(View.VISIBLE);
                if(enterMessage.getText().toString().trim().equals("")){
                    sendBtn.setVisibility(View.GONE);
                    micro.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    // Khởi tạo các giá trị ban đầu
    private void initData(){
        imageViewVideoCall=findViewById(R.id.img_video_call);
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
        camera=findViewById(R.id.camera);
        micro = findViewById(R.id.micro);
        recordView = findViewById(R.id.recordView);
        sendBtn = findViewById(R.id.sendBtn);
        avatar = findViewById(R.id.profile);
        micro.setRecordView(recordView);
        micro.setListenForRecord(false);
        messageList = new ArrayList<>();
    }

    //Chụp ảnh
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if(requestCode==100){
            Toast.makeText(this, "Take photo complete", Toast.LENGTH_SHORT).show();
        }
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
        recyclerView.scrollToPosition(messageList.size()-1);
        enterMessage.setText("");
        chatAdapter.notifyDataSetChanged();
        databaseReference = firebaseDatabase.getReference("Group").child(groupId).child("message");
        databaseReference.setValue(message);
        arrangeGroupList(Util.currentUser);
//        databaseReference = firebaseDatabase.getReference("Users").child(receiverId);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                receiver = snapshot.getValue(User.class);
//                arrangeGroupList(receiver);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    public void arrangeGroupList(User user){
        user.getGroupUserList().add(new GroupUser());
        GroupUser groupUser = new GroupUser();
        for(int i=0;i<user.getGroupUserList().size();i++){
            if(user.getGroupUserList().get(i).getGroupId().equals(groupId)){
                groupUser = user.getGroupUserList().get(i);
                break;
            }
        }
        user.getGroupUserList().remove(groupUser);
        for (int i =user.getGroupUserList().size()-1;i>0;i--) {
            user.getGroupUserList().set(i, user.getGroupUserList().get(i-1));
        };
        user.getGroupUserList().set(0,groupUser);
        user.setGroupUserList(user.getGroupUserList());
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
        databaseReference.child("groupUserList").setValue(user.getGroupUserList());
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
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
                        String key = databaseReference.push().getKey();
                        String senderId = firebaseAuth.getCurrentUser().getUid();
                        Message message = new Message(key,groupId,senderId,"","image",imageToken);
                        databaseReference.child(key).setValue(message);
                        databaseReference = firebaseDatabase.getReference("Group").child(groupId).child("message");
                        databaseReference.setValue(message);
                        messageList.add(message);
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size()-1);
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
    }
    public void takePermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivityForResult(intent,101);
            }
            catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,101);
            }
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
            },101);
        }
    }
    public void setUpRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ChatsApp/Media/Recording");
        if (!file.exists())
            file.mkdirs();
        audioPath = file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".3gp";
        mediaRecorder.setOutputFile(audioPath);
    }
    public void sendRecordingMessage(String audioPath){
        StorageReference storageReference = firebaseStorage.getReference(  "Media/Recording/" + Util.currentUser.getId() + "/" + System.currentTimeMillis());
        Uri audioFile = Uri.fromFile(new File(audioPath));
        storageReference.putFile(audioFile).addOnSuccessListener(success->{
            Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
            audioUrl.addOnCompleteListener(path->{
                if(path.isSuccessful()){
                    String url = path.getResult().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
                    String key = databaseReference.push().getKey();
                    Message message = new Message(key,groupId,Util.currentUser.getId(), url,"audio","");
                    databaseReference.child(key).setValue(message);
                    databaseReference = firebaseDatabase.getReference("Group").child(groupId).child("message");
                    databaseReference.setValue(message);
                    messageList.add(message);
                    chatAdapter.notifyDataSetChanged();
                }
            });
        });
    }
    public void setInformation(){
        groupId= getIntent().getStringExtra("roomId");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Group").child(groupId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                group = snapshot.getValue(Group.class);
                if(group.getTypeGroup().equals("group")){
                    username.setText(group.getGroupName());
                    Picasso.get().load(group.getImgUrl()).into(avatar);
                }
                else {
                    username.setText(getIntent().getStringExtra("username"));
                    Picasso.get().load(getIntent().getStringExtra("imageUrl")).into(avatar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode==101){
                boolean readExt = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(!readExt){
                    takePermission();
                }
            }
        }
    }

}
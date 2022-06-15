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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hcmute.danbaonguyen19110036.appzalo.Adapter.ChatAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.Group;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class ChatboxActivity extends AppCompatActivity {
    // Khai báo cáo View để xử lý sự kiện
    private TextView username;
    private ImageView btnBack,selectImg,sendBtn,avatar,camera;
    private RecyclerView recyclerView;
    private EditText enterMessage;
    //Các biến thực thi chức năng call video
    private ImageView imageViewVideoCall;
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    //
    private StorageReference storageReference;
    // FirebaseStorage dùng để lưu hình ảnh lên firebase
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    // khai báo list message để lưu trữ đoạn chat của User
    private List<Message> messageList;
    // Khai báo Adapter để set vào Recycle View
    private ChatAdapter chatAdapter;
    // groupId dùng để lưu trữ groupId trong Intent đã lưu ở Activity trước
    // audioPath để khi record , file record sẽ được vào file trong máy điện thoại
    private String groupId,audioPath;
    // request_code để lúc chọn hình ảnh mình có thể check xem là chọn hình ảnh hay chụp hình ảnh
    // PICK_IMAGE chọn hình ảnh , CAPTURE_IMAGE chụp hình
    private static int PICK_IMAGE=123,CAPTURE_IMAGE=100;
    // Dùng để lưu path khi user chọn ảnh từ folder
    private Uri imagepath;
    // Sau khi xử lý hình ảnh và upload lên server dùng biến imageToken để lưu lại URL
    // receiverToken dùng để lưu lại Token của người nhận được lưu trong intent
    private String imageToken,receiverId,receiverToken;
    private RecordButton micro;
    private RecordView recordView;
    private MediaRecorder mediaRecorder;
    private Group group;
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
        //Chụp ảnh và gửi đi
        //Yêu cầu cấp quền camera camera
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
                startActivityForResult(intent,CAPTURE_IMAGE);
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
                        .setTitle("Cho phép Zalo truy cập vào quản lý của bạn")
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
            // gọi đến hàm setup record trước khi chúng ta record
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
            // Chúng ta cần xóa file audio sau khi user cancel recording
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
            // Nếu user hoàn thành record ,upload file lên firebase storage
            @Override
            public void onFinish(long recordTime) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                } catch (RuntimeException stopException) {
                    stopException.printStackTrace();
                }
                recordView.setVisibility(View.GONE);
                enterMessage.setVisibility(View.VISIBLE);
                sendRecordingMessage(audioPath);
            }
            // hàm này sẽ được gọi khi thời gian recording ít hơn 1 giây
            // và chúng ta cần xóa file đó
            @Override
            public void onLessThanSecond() {
                File file = new File(audioPath);
                if (file.exists())
                    file.delete();
                mediaRecorder.reset();
                mediaRecorder.release();

                recordView.setVisibility(View.GONE);
            }
        });
        enterMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
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
        //Xử lý sự kiện click vào hình gọi video trong khung chat
        imageViewVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChatboxActivity.this, VideoCallOutGoingActivity.class);
                // lưu lại thông tin token của user được chọn
                intent.putExtra("receiver_token",receiverToken);
                // lưu lại groupId để cho 2 user call nhau join vào room
                intent.putExtra("groupId",groupId);
                intent.putExtra("receiverId",receiverId);
                startActivity(intent);
            }
        });
    }
    // Khởi tạo các giá trị View và Firebase ban đầu
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
        receiverToken = getIntent().getStringExtra("token");
    }
    // Dùng để gửi tin nhắn dạng text
    public void SendMessage(View view){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
        String key = databaseReference.push().getKey();
        String senderId = firebaseAuth.getCurrentUser().getUid();
        String text = enterMessage.getText().toString();
        // Kiểm tra xem người dùng đã nhập tin nhắn hay chưa
        if(text.isEmpty()){
            Toast.makeText(ChatboxActivity.this,"Vui lòng nhập tin nhắn",Toast.LENGTH_SHORT).show();
            return;
        }
        // Nếu tin nhắn được gửi vào room có dạng là group thì thêm tên người gửi vào đằng
        // trước để phân biệt
        if(group.getTypeGroup().equals("group")){
            text = Util.currentUser.getUserName()+": "+text;
        }
        // Lưu tin nhắn này lên FirebaseDatabase
        Message message = new Message(key,groupId,senderId,text,"text","");
        databaseReference.child(key).setValue(message);
        // Thêm tinh nhắn vào listMessage
        messageList.add(message);
        // Cuộn xuống tin nhắn mới nhất
        recyclerView.scrollToPosition(messageList.size()-1);
        enterMessage.setText("");
        // Cập nhật lại adapter
        chatAdapter.notifyDataSetChanged();
        // Lưu lại last message trong Room
        databaseReference = firebaseDatabase.getReference("Group").child(groupId).child("message");
        databaseReference.setValue(message);

        arrangeGroupList(Util.currentUser);
        // Vì nhóm em chưa làm kịp nên chỉ làm thông báo đến các Group private
        if(group.getTypeGroup().equals("private")){
            sendNotification(text,receiverToken);
        }
    }
    // Sắp xếp lại thứ tự tin nhắn ví dụ người nhắn tin gần nhất sẽ
    // đươc đưa lên đầu
    public void arrangeGroupList(User user){
        user.getGroupUserList().add(new GroupUser());
        GroupUser groupUser = new GroupUser();
        // Sắp xếp lại mảng
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
        // Cập nhật giá trị này lên database
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
        databaseReference.child("groupUserList").setValue(user.getGroupUserList());
    }
    // Sử dụng dịch vụ Message của Firebase và notification để thông báo
    // đến Receiver khi có tin nhắn mới. Để sử dụng dịch vụ Message của Firease
    // ta call đến API với URL
    void sendNotification(String message, String token) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject data = new JSONObject();
            data.put("title", Util.currentUser.getUserName());
            data.put("body", message);
            JSONObject notificationData = new JSONObject();
            notificationData.put("data", data);
            notificationData.put("to", token);
            JsonObjectRequest request = new JsonObjectRequest(AllConstants.NOTIFICATION_URL, notificationData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ChatboxActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ChatboxActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    // Đây là hàm sẽ được chạy khi có có lệnh gọi đến startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Nếu là PICK_IMAGE người dùng chọn ảnh từ trong điện thoại
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            sendImageMessage(imagepath);
        }
        // Nếu là PICK_IMAGE người dùng chụp ảnh
        if(requestCode==CAPTURE_IMAGE){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            imagepath = tempUri;
            sendImageMessage(imagepath);
            Toast.makeText(this, "Take photo complete", Toast.LENGTH_SHORT).show();
        }
    }
    // Xử lý và lấy ra Uri của tấm ảnh vừa chụp được
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    // Gửi tin nhắn dạng Image
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
                    // Nếu thành công upload thì lấy ra uri của tấm ảnh
                    // và lưu Message lên firebase Database
                    public void onSuccess(Uri uri) {
                        imageToken=uri.toString();
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
                    // Nếu thất bại thì in ra câu thông báo
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
    // Đối với các Android phiên bản 10+ thì yêu cầu truy cập đến các file của hệ thống
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
    // set up trước khi bắt đầu recording
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
    // Sau khi đã record xong tin nhắn dạng voice lưu tin nhắn này lên Database
    public void sendRecordingMessage(String audioPath){
        StorageReference storageReference = firebaseStorage.getReference(  "Media/Recording/" + Util.currentUser.getId() + "/" + System.currentTimeMillis());
        Uri audioFile = Uri.fromFile(new File(audioPath));
        storageReference.putFile(audioFile).addOnSuccessListener(success->{
            Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
            audioUrl.addOnCompleteListener(path->{
                if(path.isSuccessful()){
                    // Lưu giữ liệu lên Database
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
    // Lấy ra thông tin của Group để xem group đó là private hay group
    // tuy vào type group mà sẽ set thông tin khác nhau
    public void setInformation(){
        groupId= getIntent().getStringExtra("roomId");
        // Lấy ra group ứng với groupId đã được lưu trong Intent trong Activity trước
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

    // Kiểm tra việc yêu cầu cấp quyền có thành công hay không
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
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
    // Khai b??o c??o View ????? x??? l?? s??? ki???n
    private TextView username;
    private ImageView btnBack,selectImg,sendBtn,avatar,camera;
    private RecyclerView recyclerView;
    private EditText enterMessage;
    //C??c bi???n th???c thi ch???c n??ng call video
    private ImageView imageViewVideoCall;
    // firebaseAuth d??ng ????? l???y ra nh???ng th??ng tin c???a user hi???n t???i
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase d??ng ????? l???y ra data trong database
    private FirebaseDatabase firebaseDatabase;
    //
    private StorageReference storageReference;
    // FirebaseStorage d??ng ????? l??u h??nh ???nh l??n firebase
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    // khai b??o list message ????? l??u tr??? ??o???n chat c???a User
    private List<Message> messageList;
    // Khai b??o Adapter ????? set v??o Recycle View
    private ChatAdapter chatAdapter;
    // groupId d??ng ????? l??u tr??? groupId trong Intent ???? l??u ??? Activity tr?????c
    // audioPath ????? khi record , file record s??? ???????c v??o file trong m??y ??i???n tho???i
    private String groupId,audioPath;
    // request_code ????? l??c ch???n h??nh ???nh m??nh c?? th??? check xem l?? ch???n h??nh ???nh hay ch???p h??nh ???nh
    // PICK_IMAGE ch???n h??nh ???nh , CAPTURE_IMAGE ch???p h??nh
    private static int PICK_IMAGE=123,CAPTURE_IMAGE=100;
    // D??ng ????? l??u path khi user ch???n ???nh t??? folder
    private Uri imagepath;
    // Sau khi x??? l?? h??nh ???nh v?? upload l??n server d??ng bi???n imageToken ????? l??u l???i URL
    // receiverToken d??ng ????? l??u l???i Token c???a ng?????i nh???n ???????c l??u trong intent
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
        // l???y gi?? tr??? trong intent l??u v??o TextView
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
                }
                recyclerView.scrollToPosition(messageList.size()-1);
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        //Ch???p ???nh v?? g???i ??i
        //Y??u c???u c???p qu???n camera camera
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
                //M??? camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAPTURE_IMAGE);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set s??? ki???n quay l???i TabChat
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
                        .setTitle("Cho ph??p Zalo truy c???p v??o qu???n l?? c???a b???n")
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
            // g???i ?????n h??m setup record tr?????c khi ch??ng ta record
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
            // Ch??ng ta c???n x??a file audio sau khi user cancel recording
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
            // N???u user ho??n th??nh record ,upload file l??n firebase storage
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
            // h??m n??y s??? ???????c g???i khi th???i gian recording ??t h??n 1 gi??y
            // v?? ch??ng ta c???n x??a file ????
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
        //X??? l?? s??? ki???n click v??o h??nh g???i video trong khung chat
        imageViewVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChatboxActivity.this, VideoCallOutGoingActivity.class);
                // l??u l???i th??ng tin token c???a user ???????c ch???n
                intent.putExtra("receiver_token",receiverToken);
                // l??u l???i groupId ????? cho 2 user call nhau join v??o room
                intent.putExtra("groupId",groupId);
                intent.putExtra("receiverId",receiverId);
                startActivity(intent);
            }
        });
    }
    // Kh???i t???o c??c gi?? tr??? View v?? Firebase ban ?????u
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
    // D??ng ????? g???i tin nh???n d???ng text
    public void SendMessage(View view){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Messages").child(groupId);
        String key = databaseReference.push().getKey();
        String senderId = firebaseAuth.getCurrentUser().getUid();
        String text = enterMessage.getText().toString();
        // Ki???m tra xem ng?????i d??ng ???? nh???p tin nh???n hay ch??a
        if(text.isEmpty()){
            Toast.makeText(ChatboxActivity.this,"Vui l??ng nh???p tin nh???n",Toast.LENGTH_SHORT).show();
            return;
        }
        // N???u tin nh???n ???????c g???i v??o room c?? d???ng l?? group th?? th??m t??n ng?????i g???i v??o ?????ng
        // tr?????c ????? ph??n bi???t
        if(group.getTypeGroup().equals("group")){
            text = Util.currentUser.getUserName()+": "+text;
        }
        // L??u tin nh???n n??y l??n FirebaseDatabase
        Message message = new Message(key,groupId,senderId,text,"text","");
        databaseReference.child(key).setValue(message);
        // Th??m tinh nh???n v??o listMessage
        messageList.add(message);
        // Cu???n xu???ng tin nh???n m???i nh???t
        recyclerView.scrollToPosition(messageList.size()-1);
        enterMessage.setText("");
        // C???p nh???t l???i adapter
        chatAdapter.notifyDataSetChanged();
        // L??u l???i last message trong Room
        databaseReference = firebaseDatabase.getReference("Group").child(groupId).child("message");
        databaseReference.setValue(message);

        arrangeGroupList(Util.currentUser);
        // V?? nh??m em ch??a l??m k???p n??n ch??? l??m th??ng b??o ?????n c??c Group private
        if(group.getTypeGroup().equals("private")){
            sendNotification(text,receiverToken);
        }
    }
    // S???p x???p l???i th??? t??? tin nh???n v?? d??? ng?????i nh???n tin g???n nh???t s???
    // ??????c ????a l??n ?????u
    public void arrangeGroupList(User user){
        user.getGroupUserList().add(new GroupUser());
        GroupUser groupUser = new GroupUser();
        // S???p x???p l???i m???ng
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
        // C???p nh???t gi?? tr??? n??y l??n database
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
        databaseReference.child("groupUserList").setValue(user.getGroupUserList());
    }
    // S??? d???ng d???ch v??? Message c???a Firebase v?? notification ????? th??ng b??o
    // ?????n Receiver khi c?? tin nh???n m???i. ????? s??? d???ng d???ch v??? Message c???a Firease
    // ta call ?????n API v???i URL
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
    // ????y l?? h??m s??? ???????c ch???y khi c?? c?? l???nh g???i ?????n startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // N???u l?? PICK_IMAGE ng?????i d??ng ch???n ???nh t??? trong ??i???n tho???i
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            sendImageMessage(imagepath);
        }
        // N???u l?? PICK_IMAGE ng?????i d??ng ch???p ???nh
        if(requestCode==CAPTURE_IMAGE){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            imagepath = tempUri;
            sendImageMessage(imagepath);
            Toast.makeText(this, "Take photo complete", Toast.LENGTH_SHORT).show();
        }
    }
    // X??? l?? v?? l???y ra Uri c???a t???m ???nh v???a ch???p ???????c
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    // G???i tin nh???n d???ng Image
    public void sendImageMessage(Uri imagepath){
        String timeStamp = ""+System.currentTimeMillis();
        String fillNamepath = "ChatImages/"+"post_"+timeStamp;
        StorageReference imgref = storageReference.child("Images").child(fillNamepath);
        Bitmap bitmap=null;
        if(imagepath==null){
            Toast.makeText(this,"Vui l??ng ch???n ???nh c???a b???n",Toast.LENGTH_SHORT).show();
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
                    // N???u th??nh c??ng upload th?? l???y ra uri c???a t???m ???nh
                    // v?? l??u Message l??n firebase Database
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
                    // N???u th???t b???i th?? in ra c??u th??ng b??o
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
    // ?????i v???i c??c Android phi??n b???n 10+ th?? y??u c???u truy c???p ?????n c??c file c???a h??? th???ng
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
    // set up tr?????c khi b???t ?????u recording
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
    // Sau khi ???? record xong tin nh???n d???ng voice l??u tin nh???n n??y l??n Database
    public void sendRecordingMessage(String audioPath){
        StorageReference storageReference = firebaseStorage.getReference(  "Media/Recording/" + Util.currentUser.getId() + "/" + System.currentTimeMillis());
        Uri audioFile = Uri.fromFile(new File(audioPath));
        storageReference.putFile(audioFile).addOnSuccessListener(success->{
            Task<Uri> audioUrl = success.getStorage().getDownloadUrl();
            audioUrl.addOnCompleteListener(path->{
                if(path.isSuccessful()){
                    // L??u gi??? li???u l??n Database
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
    // L???y ra th??ng tin c???a Group ????? xem group ???? l?? private hay group
    // tuy v??o type group m?? s??? set th??ng tin kh??c nhau
    public void setInformation(){
        groupId= getIntent().getStringExtra("roomId");
        // L???y ra group ???ng v???i groupId ???? ???????c l??u trong Intent trong Activity tr?????c
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

    // Ki???m tra vi???c y??u c???u c???p quy???n c?? th??nh c??ng hay kh??ng
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
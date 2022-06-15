package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import hcmute.danbaonguyen19110036.appzalo.Adapter.AddGroupAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.Group;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class GroupActivity extends AppCompatActivity {
    private static int PICK_IMAGE=123; // request code
    private Uri imagepath; // đường dẫn của hình ảnh sau khi hình ảnh được chọn
    private ImageView imgGroup; //
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore; // Firestore để lưu hình ảnh
    private AddGroupAdapter addGroupAdapter; // Adapter dùng để set dữ liệu
    private List<User> userList; // Dùng để lưu trữ danh sách user
    private ListView listView; // ListView dùng để set adapter
    private EditText edtGroup; // Tên group
    // Dùng để lưu Url của hình ảnh sau khi được lưu lên FireStore
    private String imageToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_group);
        initData();
        // Cho phép user chọn ảnh từ điện thoại
        imgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Vì khi database thay đổi thì nó sẽ lấy lại dữ liệu vì vậy mình cần clear mảng đi
                userList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    // Chỉ cho phép thêm những user nằm trong list friend vào nhóm
                    if(Util.currentUser.getListFriend().contains(user.getId())){
                        userList.add(user);
                    }
                }
                addGroupAdapter = new AddGroupAdapter(userList,R.layout.layout_add_group,GroupActivity.this);
                listView.setAdapter(addGroupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    // Khơi tạo các giá trị View và Firebase
    public void initData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        imgGroup = findViewById(R.id.imgGroup);
        userList = new ArrayList<>();
        listView = findViewById(R.id.list_item_group);
        edtGroup = findViewById(R.id.edtGroupName);
    }
    // Lưu hình ảnh lên Firestore
    private void sendImageToStore(){
        StorageReference imgref = storageReference.child("Images").child("GroupImage").child(edtGroup.getText().toString()).child("ProfilePic");
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
                        sendDataToDatabase();
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
    // Lưu dữ liệu lên Firebase Database
    private void sendDataToDatabase(){
        // Tạo Room để mọi người có thể nhắn tin
        DatabaseReference databaseReference = firebaseDatabase.getReference("Group");
        // tạo 1 key ngẫu nhiên làm groupId
        String key = databaseReference.push().getKey();
        Group group = new Group(key,"group",edtGroup.getText().toString(),imageToken);
        databaseReference.child(key).setValue(group);
        // lưu dữ liệu lên Database
        Util.getListGroupUser().add(Util.currentUser);
        for(int i=0;i<Util.getListGroupUser().size();i++){
            // Tạo group User
            databaseReference = firebaseDatabase.getReference("GroupUser");
            // Tạo key random
            String keyGroup = databaseReference.push().getKey();
            // Save giá trị lên database
            // Lấy ra user để cập nhật lại groupUser
            User user = Util.getListGroupUser().get(i);
            GroupUser groupUser = new GroupUser(keyGroup, user.getId(), key);
            databaseReference.child(keyGroup).setValue(groupUser);
            String userid =Util.groupListUser.get(i).getId();
            user.getGroupUserList().add(groupUser);
            databaseReference = firebaseDatabase.getReference("Users").child(userid)
                    .child("groupUserList");
            databaseReference.setValue(user.getGroupUserList());
        }
    }
    // Hàm click vào nút tạo nhóm
    public void OnClickCreateGroup(View view){
        // Kiểm tra các thông tin
        if(edtGroup.getText().toString().isEmpty()){
            Toast.makeText(GroupActivity.this,"Vui lòng nhập tên Group",Toast.LENGTH_SHORT).show();
            return;
        }
        // Gửi dữ liệu lên server
        sendImageToStore();
        finish();
    }
    // Dùng để lấy ra imgaepath sau khi người dùng chọn ảnh từ điện thoại
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            imgGroup.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
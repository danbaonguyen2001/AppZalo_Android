package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Adapter.AddFriendAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class AddFriendActivity extends AppCompatActivity {
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    // userList để lưu trữ các user lấy từ Database
    private List<User> userList;
    // Tạo adapter để lưu vào list view
    private AddFriendAdapter addFriendAdapter;
    // Lấy các View ra để xử lý sự kiện
    private ListView listView;
    private ImageView btnBack;
    //  Edittext mà user đã nhập vào
    EditText edtSearch;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_friend);
        initData();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Vì khi database thay đổi thì nó sẽ lấy lại dữ liệu vì vậy mình cần clear mảng đi
                userList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    // Chỉ lấy các user không trong listfriend (chưa kết bạn)
                    if(Util.currentUser.getListFriend().contains(user.getId())==false&&!Util.currentUser.getId().equals(user.getId())){
                        userList.add(user);
                    }
                }
                // khởi tạo adapter
                addFriendAdapter = new AddFriendAdapter(AddFriendActivity.this,userList,R.layout.layout_user_add_request);
                // set adapter vào list view
                listView.setAdapter(addFriendAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddFriendActivity.this,MainActivity.class));
                finish();
            }
        });
    }
    // Khởi tạo các giá trị ban đầu
    private void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        listView = findViewById(R.id.list_user_addfr);
        btnBack = findViewById(R.id.btn_back_addfr);
        edtSearch = findViewById(R.id.search_add_fr);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Nếu data thay đổi cập nhật lại dapter
        if(addFriendAdapter!=null){
            addFriendAdapter.notifyDataSetChanged();
        }
    }
    public void OnClickSearch(View view){
        // Lấy ra key đã nhập vào ô tìm kiếm
        String phoneKey = edtSearch.getText().toString();
        if(phoneKey.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập số điện thoại mà bạn muốn tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Vì khi database thay đổi thì nó sẽ lấy lại dữ liệu vì vậy mình cần clear mảng đi
                userList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    // Chỉ lấy các user không trong listfriend (chưa kết bạn)
                    if(Util.currentUser.getListFriend().contains(user.getId())==false&&!Util.currentUser.getId().equals(user.getId())
                        &&user.getPhoneNumber().contains(phoneKey)==true){
                        userList.add(user);
                    }
                }
                addFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
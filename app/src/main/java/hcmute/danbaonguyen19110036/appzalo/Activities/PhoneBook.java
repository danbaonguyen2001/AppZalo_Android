package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Adapter.PhoneBookAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class
PhoneBook extends AppCompatActivity {
    private ListView listView;// Hiển thị danh sách user ứng với mỗi số điện thoại
    private FirebaseDatabase firebaseDatabase; // Firebase
    private List<String> listContact;// lưu lại số điện thoại user
    private List<User> userList; // Dùng để lưu lại danh sách user
    private PhoneBookAdapter phoneBookAdapter; // Adapter dùng để lưu giữ liệu userlist
    private Button totalFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_phone_book);
        initData();
        requestReadContact();
        getContacts();
        phoneBookAdapter = new PhoneBookAdapter(userList,this,R.layout.layout_tab_phonebook_user_item);
        listView.setAdapter(phoneBookAdapter);

    }
    public void initData(){
        listView = findViewById(R.id.lv_sync_contact);
        listContact = new ArrayList<>();
        userList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        totalFriend = findViewById(R.id.btn_total_friend_pb);
    }
    public void getContacts(){
        // Thông qua Contactscontract Contract để lấy contact trong điện thoại
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // Trả về cursor - quản lý dữ liệu contact trong diện thoại
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        listContact.clear();
        while (cursor.moveToNext()) {
            // Lấy thông tin tên trong danh bạ
            String tencot = ContactsContract.Contacts.DISPLAY_NAME;
            // Lấy thông tin số điện thoại trong điện thoại
            String phone = ContactsContract.CommonDataKinds.Phone.NUMBER;
            int vitri = cursor.getColumnIndex(tencot);
            int vitriphone = cursor.getColumnIndex(phone);
            String name = cursor.getString(vitri);
            String phonenumber = cursor.getString(vitriphone);
            phonenumber = Util.convertToPhoneNumber(phonenumber);
            listContact.add(phonenumber);
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    if(listContact.contains(user.getPhoneNumber())==true){
                        userList.add(user);
                    }
                }
                String total = "Tất cả "+String.valueOf(userList.size());
                totalFriend.setText(total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void requestReadContact(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_NUMBERS)==PackageManager.PERMISSION_GRANTED){

            }
            ActivityCompat.requestPermissions(PhoneBook.this,new String[]{
                    Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS
            },121);
        }
    }
    // Yêu cầu người dùng cho phép ứng dụng đọc danh bạ
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Quay lại trang trước đó
    public void OnClickBackHome(View view){
        onBackPressed();
    }

}
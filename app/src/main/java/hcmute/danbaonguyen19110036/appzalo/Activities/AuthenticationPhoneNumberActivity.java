package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class AuthenticationPhoneNumberActivity extends AppCompatActivity {
    private EditText edtCode; // Editext nơi mà user sẽ nhập otp vào
    // Khai báo các biến Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    // dùng để lưu lại trạng thái được lấy ra từ intent
    public String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_authentication_phone_number);
        initData();
    }
    // Khởi tạo các giá trị View và Firebase
    private void initData() {
        edtCode = findViewById(R.id.edt_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void OnClickVerifyOTP(View view){
        String enteredotp = edtCode.getText().toString();
        // Kiểm tra xem user đã nhập OTP hay chưa
        if(enteredotp.isEmpty()){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập OTP",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            // Lấy ra code đã lưu ở activity trước đó
            String codereciever =getIntent().getStringExtra("otp");
            // Lấy ra xem user đến từ login đến từ login hay register activity
            activity = getIntent().getStringExtra("Activity");
            //Sau khi người dùng nhập mã xác minh mà Firebase đã gửi đến điện thoại của user
            // tạo một đối tượng PhoneAuthCredential , sử dụng verification code
            // Để tạo đối tượng PhoneAuthCredential, ta gọi PhoneAuthProvider.getCredential:
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereciever,enteredotp);
            signInWithPhoneAuthCredential(credential);
        }
    }
    //Sau khi bạn nhận được Object PhoneAuthCredential hoàn tất quy trình đăng nhập bằng cách chuyển đối tượng PhoneAuthCredential tới FirebaseAuth.signInWithCredential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    if(activity.equals("Register")) {
                        // Nếu là từ Activity Register thì ta tạo tài khoản cho User
                        createUser();
                        Intent intent=new Intent(AuthenticationPhoneNumberActivity.this,RegisterInputInforActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        // Nếu từ Activity Login thì ta lưu lại giá trị của User
                        // Lấy ra User với AuthId đã login
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Lưu lại giá trị vào biến static currentuser để sau này lấy dữ liệu
                                Util.currentUser = snapshot.getValue(User.class);
                                Intent intent=new Intent(AuthenticationPhoneNumberActivity.this,MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(getApplicationContext(),"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void OnClickBackHome(View view){
        finish();
    }
    // Nếu user đến từ trang đăng ký thì tạo tài khoản cho User
    private void createUser(){
        // Tạo tài khoản cho user
        //Lấy ra id của user hiện tại
        String id = firebaseAuth.getCurrentUser().getUid();
        // Lấy ra user với id tương ứng
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(id);
        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        // Tạo tài khoản cho User chỉ với những thông tin như id,phonenumber,token của thiết bị
        User user = new User(id,phoneNumber,"","",null,"","",Util.token);
        Util.currentUser = user;
        databaseReference.setValue(user);
        // Lưu giá trị lên Firebase
    }
}
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // Tạo một biến để lưu lại code khi Firebase gửi tin nhắn đến điện thoại
    private String codeSend;
    private FirebaseDatabase firebaseDatabase;
    // Dùng để kiểm tra xem số điện thoại đã tồn tại hay chưa
    private boolean KT=false;
    private EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        initData();
        //Khi gọi PhoneAuthProvider.verifyPhoneNumber,ta phải cung cấp một instance của OnVerificationStateChangedCallbacks,\
        // chứa triển khai các hàm callback xử lý kết quả của yêu cầu.
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegisterActivity.this,"onVerificationCompleted:" + phoneAuthCredential,Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this,"Send Code Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP đã được gửi",Toast.LENGTH_SHORT).show();
                // Lưu lại giá trị code
                codeSend = s;
                Intent intent = new Intent(RegisterActivity.this,AuthenticationPhoneNumberActivity.class);
                // Lưu lại otp đã gửi để đi tới ActivityAuthentication check lại code
                intent.putExtra("otp",codeSend);
                // đánh dấu là từ trang Register đi tới trang authentication
                intent.putExtra("Activity","Register");
                startActivity(intent);
            }
        };
    }
    // Khơi tạo các giá trị View và Firebase
    private void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        phoneNumber = findViewById(R.id.input_phone_rg);
    }
    //Sau khi bạn nhận được Object PhoneAuthCredential hoàn tất quy trình đăng nhập bằng cách chuyển đối tượng PhoneAuthCredential tới FirebaseAuth.signInWithCredential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(RegisterActivity.this,"Tài khoản không chính xác",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    public void OnClickSendOTP(View view){
        // Kiểm tra xem người dùng đã nhập số điện thoại hay chưa
        if(phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(RegisterActivity.this,"Vui lòng nhập số điện thoại",Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber.getText().toString())//Số điện thoại để xác minh
                .setTimeout(60L, TimeUnit.SECONDS)//Thời gian chờ
                .setActivity(RegisterActivity.this)//Activity
                .setCallbacks(mCallbacks)// xác minh trạng thái
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);// verifyPhoneNumber sẽ gửi SMS tới số điện thoại đã set
    }
    public void OnClickBackHome(View view){
        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
    }
}
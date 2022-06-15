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
import java.util.concurrent.TimeUnit;
import hcmute.danbaonguyen19110036.appzalo.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    //Dùng để lưu lại giá trị code khi firebase gửi tin nhắn đến điện thoại
    private EditText phoneNumber;
    private String codeSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initData();
        //Khi gọi PhoneAuthProvider.verifyPhoneNumber,ta phải cung cấp một instance của OnVerificationStateChangedCallbacks,\
        // chứa triển khai các hàm callback xử lý kết quả của yêu cầu.
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this,"onVerificationCompleted:" + phoneAuthCredential,Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this,"Send Code Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is send",Toast.LENGTH_SHORT).show();
                codeSend = s;
                Intent intent = new Intent(LoginActivity.this,AuthenticationPhoneNumberActivity.class);
                // Lưu lại otp đã gửi để đi tới ActivityAuthentication check lại code
                intent.putExtra("otp",codeSend);
                // đánh dấu là từ trang login đi tới trang authentication
                intent.putExtra("Activity","Login");
                startActivity(intent);
            }
        };
    }
    public void initData(){
        // Khởi tạo các giá trị view và các giá trị của Firebase
        phoneNumber = findViewById(R.id.input_phone);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void OnClickBackHome(View view){
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
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
                                Toast.makeText(LoginActivity.this,"Tài khoản không chính xác",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    public void OnClickSendOTP(View view){
        // Kiểm tra xem số điện thoại đã được nhập hay chưa
        if(phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(this,"Vui lòng nhập số điện thoại của bạn",Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber.getText().toString()) //Số điện thoại để xác minh
                .setTimeout(60L, TimeUnit.SECONDS) //Thời gian chờ
                .setActivity(LoginActivity.this)//Activity
                .setCallbacks(mCallbacks) // xác minh trạng thái
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options); // verifyPhoneNumber sẽ gửi SMS tới số điện thoại đã set
    }
}
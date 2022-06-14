package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private String codeSend;
    private FirebaseDatabase firebaseDatabase;
    private boolean KT=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        initData();
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
                Toast.makeText(getApplicationContext(),"OTP is send",Toast.LENGTH_SHORT).show();
                codeSend = s;
                Intent intent = new Intent(RegisterActivity.this,AuthenticationPhoneNumberActivity.class);
                intent.putExtra("otp",codeSend);
                intent.putExtra("Activity","Register");
                startActivity(intent);
            }
        };
    }
    private void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            GotoOTPActivity(user.getPhoneNumber());
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
        String phoneNumber="+84977412456";
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    if(user.getPhoneNumber().equals(phoneNumber)){
                        KT=true;
                    }
                }
                if(KT==true){
                    Toast.makeText(RegisterActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();
                    return;
                }
                PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(RegisterActivity.this)
                        .setCallbacks(mCallbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void OnClickBackHome(View view){
        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
    }
    private void GotoOTPActivity(String phoneNumber) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("phoneNumber",phoneNumber);
        startActivity(intent);
    }
}
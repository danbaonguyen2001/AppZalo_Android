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

import hcmute.danbaonguyen19110036.appzalo.R;

public class AuthenticationPhoneNumberActivity extends AppCompatActivity {
    private EditText edtCode;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_authentication_phone_number);
        edtCode = findViewById(R.id.edt_otp);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void OnClickVerifyOTP(View view){
        String enteredotp = edtCode.getText().toString();
        if(enteredotp.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter OTP",Toast.LENGTH_SHORT).show();
        }
        else {
            String codereciever =getIntent().getStringExtra("otp");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereciever,enteredotp);
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AuthenticationPhoneNumberActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
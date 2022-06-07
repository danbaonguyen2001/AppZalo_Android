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

public class AuthenticationPhoneNumberActivity extends AppCompatActivity {
    private EditText edtCode;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    public String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_authentication_phone_number);
        edtCode = findViewById(R.id.edt_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    public void OnClickVerifyOTP(View view){
        String enteredotp = edtCode.getText().toString();
        if(enteredotp.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter OTP",Toast.LENGTH_SHORT).show();
        }
        else {
            String codereciever =getIntent().getStringExtra("otp");
            activity = getIntent().getStringExtra("Activity");
            System.out.println(activity);
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
                    Intent intent;
                    if(activity.equals("Register")) {
                        createUser();
                        intent=new Intent(AuthenticationPhoneNumberActivity.this,RegisterInputInforActivity.class);
                    }
                    else
                    {
                        intent=new Intent(AuthenticationPhoneNumberActivity.this,MainActivity.class);
                    }
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
    private void createUser(){
        String id = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(id);
        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        User user = new User(id,phoneNumber,"","",null,"","",null,null,null);
        databaseReference.setValue(user);
    }
}
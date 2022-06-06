package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;

public class RegisterInputInforActivity extends AppCompatActivity {
    private String imageToken,userName,birthDay,genDer;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;
    private CardView cardView;
    private ImageView imgProfile;
    private Uri imagepath;
    private EditText edtuserName,edtbirthDay;
    private Button selectBirthDay;
    private RadioButton radioButtonNam,radioButtonNu;
    private RadioGroup radioGroup;
    private static int PICK_IMAGE=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_input_infor);
        initData();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        selectBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterInputInforActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        edtbirthDay.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioButtonNam.isChecked()){
                    genDer = radioButtonNam.getText().toString();
                }
                else {
                    genDer = radioButtonNu.getText().toString();
                }
            }
        });
    }
    private void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        cardView = findViewById(R.id.cardView);
        imgProfile = findViewById(R.id.img_profile);
        edtuserName = findViewById(R.id.edt_username);
        edtbirthDay = findViewById(R.id.edt_birthday);
        radioButtonNam = findViewById(R.id.radio_nam);
        radioButtonNu = findViewById(R.id.radio_nu);
        selectBirthDay = findViewById(R.id.btn_selectBirthDay);
        radioGroup = findViewById(R.id.radio_group);
    }
    private void sendImageToStore(){
        System.out.println("123");
        System.out.println(firebaseAuth.getCurrentUser().getUid());
        StorageReference imgref = storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");;
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
    private void sendDataToDatabase(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        String id = firebaseAuth.getUid();
        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        Map<String,Object> user = new HashMap<>();
        user.put("id",id);
        user.put("img",imageToken);
        user.put("phoneNumber",phoneNumber);
        user.put("gender",genDer);
        user.put("userName",userName);
        user.put("address","BinhDuong");
        user.put("birthDay",birthDay);
        databaseReference.setValue(user);
    }
    public void OnClickSaveProfile(View view){
        userName = edtuserName.getText().toString();
        birthDay = edtbirthDay.getText().toString();
        if(userName.isEmpty()){
            Toast.makeText(RegisterInputInforActivity.this,"Vụi lòng nhập tên của bạn",Toast.LENGTH_SHORT).show();
            return;
        }
        if(birthDay.isEmpty()){
            Toast.makeText(RegisterInputInforActivity.this,"Vụi lòng chọn ngày sinh của bạn",Toast.LENGTH_SHORT).show();
            return;
        }
        if(genDer.isEmpty()){
            Toast.makeText(RegisterInputInforActivity.this,"Vui lòng chọn giới tính của bạn",Toast.LENGTH_SHORT).show();
            return;
        }
        sendImageToStore();
        startActivity(new Intent(this,MainActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            imgProfile.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void OnClickBackHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }
}
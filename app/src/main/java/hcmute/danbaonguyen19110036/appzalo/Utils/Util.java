package hcmute.danbaonguyen19110036.appzalo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcmute.danbaonguyen19110036.appzalo.Model.User;

public class Util {
    public static int TITLE_REQUEST;
    public static User currentUser;
    public static void requestStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, AllConstants.STORAGE_REQUEST_CODE);
    }
    public static boolean isStorageOk(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean recordingOk(Context context){
        return ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
    }
    public static void requestRecording(Activity activity){
        ActivityCompat.requestPermissions(activity,new String[]{
                Manifest.permission.RECORD_AUDIO
        },AllConstants.RECORDING_REQUEST_CODE);
    }
    public static boolean isPermissionGranted(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else {
            int readExStorage = ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExStorage==PackageManager.PERMISSION_GRANTED;
        }
    }
}

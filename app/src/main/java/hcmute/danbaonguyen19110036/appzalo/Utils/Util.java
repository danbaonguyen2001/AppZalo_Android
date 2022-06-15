package hcmute.danbaonguyen19110036.appzalo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Model.User;

public class Util {
    // Dùng để lưu lại user đang đăng nhập vào hệ thống
    public static User currentUser;
    // Dùng để lưu lại token của điển thoại User đang sử dụng hệ thống
    public static String token;
    // Dùng để lưu lại groupId thuận tiện lấy ra trong các Acitivity
    public static String groupId;
    // Dùng để lấy ra group của user hiện tại
    public static List<User> groupListUser;
    public static List<User> getListGroupUser(){
        if(groupListUser==null){
            groupListUser = new ArrayList<>();
        }
        return groupListUser;
    }
    // Đối với các số điện thoại dạng 0344... có nghĩa là người dùng không nhập country code thì
    // ta convert nó về dạng có country code
    public static String convertToPhoneNumber(String phoneNumber){
        phoneNumber=phoneNumber.substring(0,0)+phoneNumber.substring(1);
        String phoneConvert = "+84";
        phoneConvert = phoneConvert.concat(phoneNumber);
        return phoneConvert;
    }
    // Yêu cầu quyền truy cập vào Storage
    public static void requestStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, AllConstants.STORAGE_REQUEST_CODE);
    }
    // Kiểm tra xem đã được cấp quyền truy cập vào Storage hay chưa
    public static boolean isStorageOk(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    // Kiểm tra xem đã cấp quyền truy cập vào mic và recording hay chưa
    public static boolean recordingOk(Context context){
        return ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
    }
    // Yêu cầu quyền truy cập vào Mic và Recording
    public static void requestRecording(Activity activity){
        ActivityCompat.requestPermissions(activity,new String[]{
                Manifest.permission.RECORD_AUDIO
        },AllConstants.RECORDING_REQUEST_CODE);
    }
    // Kiểm tra version android hiện tại từ đó yêu cầu quyền
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

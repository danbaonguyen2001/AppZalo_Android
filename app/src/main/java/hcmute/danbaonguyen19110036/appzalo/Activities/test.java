package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;


public class test extends FragmentActivity implements JitsiMeetActivityInterface {
    private JitsiMeetView view; // là lớp cốt lỗi của Jiti Meet SDK, hiển thị như một phòng hợp hội nghị trực tuyến

    //Hàm hỗ trợ xử lý kết quả các hoạt động của SDK
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(
                this, requestCode, resultCode, data);
    }
    //Hàm hỗ trợ phương thức onBackPressed() của Fragment
    @Override
    public void onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed();
    }

    //Hàm khởi tạo ban đầu của Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new JitsiMeetView(this);
        JitsiMeetConferenceOptions options = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom("GIANG456")
                    .build();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        view.join(options);

        setContentView(view);
    }

    //Hàm làm sạch trạng thái cuối cùng của Fragment
    @Override
    protected void onDestroy() {
        super.onDestroy();

        view.dispose();
        view = null;

        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    // Hàm được gọi khi có muốn khỏi động lại activity
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }
    //Callback cho kết quả từ việc yêu cầu quyền.
    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Hàm cho fragment có thể tiếp tục tương tác với người dùng dựa vào hoạt động chứa nó nối lại
    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetActivityDelegate.onHostResume(this);
    }
    //Hàm Fragment không còn hiển thị cho người dùng vì hoạt động của nó đang bị dừng
    @Override
    protected void onStop() {
        super.onStop();
        JitsiMeetActivityDelegate.onHostPause(this);
    }

    // Hàm yêu cầu cấp quyền camera
    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, AllConstants.CAMERA_PERMISSION_CODE);

    }
    //Hàm chưa được xử lý
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
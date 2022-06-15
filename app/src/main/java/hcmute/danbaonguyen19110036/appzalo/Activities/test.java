package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import com.facebook.react.modules.core.PermissionListener;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import java.net.MalformedURLException;
import java.net.URL;
import hcmute.danbaonguyen19110036.appzalo.Utils.AllConstants;


public class test extends FragmentActivity implements JitsiMeetActivityInterface {
    private JitsiMeetView view;
    private String groupId;

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(
                this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra("groupId");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.dispose();
        view = null;
        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JitsiMeetActivityDelegate.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        JitsiMeetActivityDelegate.onHostPause(this);
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, AllConstants.CAMERA_PERMISSION_CODE);

    }

    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
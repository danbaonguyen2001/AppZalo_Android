package hcmute.danbaonguyen19110036.appzalo.Utils;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class AllConstants {
    public static int STORAGE_REQUEST_CODE = 1000;
    public static int RECORDING_REQUEST_CODE = 3000;
    public static int CAMERA_PERMISSION_CODE = 3222;

    public static int CALL_PERMISSION_CODE = 72839;
    public static String KEY_FCM_TOKEN="fcm_token";
    public static final String KEY_PREFERENCE_NAME="videoMeetingPreference";
    public  static final String KEY_USER_ID="user_id";
    public static final String TOKEN_NGUYEN="d3Dg2VliTuKa8q2kwyhtWa:APA91bEFaJoubpmqsMA8XXwMfSCa7zHhprYpLdFqTcmj05ZFFwhMmL7tFaq3M9gTmVbqwKDh8yrxEIFeoHXv0TBCmsZiYtPnlNKbfJPeFiuQcSmS6-IH6oyoNYcX5miZfcRp32O5F1CI";

    //Nguyen
    public static final String REMOTE_MSG_AUTHORZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content_Type";
    public static final String REMOTE_MSG_INVITATION="invitation";
    public static final String REMOTE_MSG_INVITER_TOKEN="inviterToken";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";
    public static final String REMOTE_MSG_MEETING_TYPE="type";

    public static HashMap<String,String> getRemoteMessageHeaders(){
        HashMap<String,String > headers= new HashMap<>();
        headers.put(
          AllConstants.REMOTE_MSG_AUTHORZATION,
                SERVER_KEY
        );
        headers.put(AllConstants.REMOTE_MSG_CONTENT_TYPE,"application/json");
        return headers;
    }


    public static String SERVER_KEY="AAAAP4k_9rg:APA91bFXpofEUnVvKntLlgROkGMPEAT4gCteAzcW0hwvq6wUXGRL02wvc-UDWqUGENSLZfCDzOriPlU_plzANmXUAbz0JbFwGdz0h33CL2dIu8ICG3E6fec7yThRIfSAKHM8tZ9NDjOV";
    public static String NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";
    public static String Token = "d3Dg2VliTuKa8q2kwyhtWa:APA91bEFaJoubpmqsMA8XXwMfSCa7zHhprYpLdFqTcmj05ZFFwhMmL7tFaq3M9gTmVbqwKDh8yrxEIFeoHXv0TBCmsZiYtPnlNKbfJPeFiuQcSmS6-IH6oyoNYcX5miZfcRp32O5F1CI";

}

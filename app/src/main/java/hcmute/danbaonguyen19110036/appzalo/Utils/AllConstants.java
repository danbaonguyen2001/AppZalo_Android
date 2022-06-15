package hcmute.danbaonguyen19110036.appzalo.Utils;
public class AllConstants {
    public static int STORAGE_REQUEST_CODE = 1000;
    public static int RECORDING_REQUEST_CODE = 3000;
    public static int CAMERA_PERMISSION_CODE = 3222;
    public static final String KEY_PREFERENCE_NAME="videoMeetingPreference";
    //Các hằng số thực hiện call video
    public static final String REMOTE_MSG_TYPE="type";
    public static final String REMOTE_MSG_INVITER_TOKEN="inviterToken";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";
    public static final String REMOTE_MSG_INVITATION_RESPONSE="invitationResponse";
    public static final String REMOTE_ROOM_ID="ROOMID";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED="accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED="rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED="cancelled";
    public static final String REMOTE_MSG_MEETING_ROOM="meetingRoom";
    // Key của account Firebase sử dụng để đăng ký FCM của firebase
    public static String SERVER_KEY="AAAAP4k_9rg:APA91bFXpofEUnVvKntLlgROkGMPEAT4gCteAzcW0hwvq6wUXGRL02wvc-UDWqUGENSLZfCDzOriPlU_plzANmXUAbz0JbFwGdz0h33CL2dIu8ICG3E6fec7yThRIfSAKHM8tZ9NDjOV";
    // API sử dụng để call
    public static String NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";

}
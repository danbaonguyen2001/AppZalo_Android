package hcmute.danbaonguyen19110036.appzalo.listeners;

import hcmute.danbaonguyen19110036.appzalo.Model.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);
}

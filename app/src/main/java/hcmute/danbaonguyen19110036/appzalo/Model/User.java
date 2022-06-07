package hcmute.danbaonguyen19110036.appzalo.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String phoneNumber;
    private String userName;
    private String birthDay;
    private String img;
    private String gender;
    private String address;
    private List<String> listFriend;
    private List<String> listRequest;
    private List<String> listPendingAccept;
    public User(){

    }

    public User(String id, String phoneNumber, String userName, String birthDay, String img, String gender, String address, List<String> listFriend, List<String> listRequest, List<String> listPendingAccept) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.birthDay = birthDay;
        this.img = img;
        this.gender = gender;
        this.address = address;
        this.listFriend = listFriend;
        this.listRequest = listRequest;
        this.listPendingAccept = listPendingAccept;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getListFriend() {
        if(listFriend==null){
            listFriend = new ArrayList<>();
        }
        return listFriend;
    }

    public void setListFriend(List<String> listFriend) {
        this.listFriend = listFriend;
    }

    public List<String> getListRequest() {
        if(listRequest==null){
            listRequest = new ArrayList<>();
        }
        return listRequest;
    }

    public void setListRequest(List<String> listRequest) {
        this.listRequest = listRequest;
    }

    public List<String> getListPendingAccept() {
        if(listPendingAccept==null){
            listPendingAccept = new ArrayList<>();
        }
        return listPendingAccept;
    }

    public void setListPendingAccept(List<String> listPendingAccept) {
        this.listPendingAccept = listPendingAccept;
    }
}

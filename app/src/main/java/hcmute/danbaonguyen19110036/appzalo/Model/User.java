package hcmute.danbaonguyen19110036.appzalo.Model;

import android.net.Uri;

import java.util.Date;

public class User {
    private String id;
    private String phoneNumber;
    private String userName;
    private String birthDay;
    private String img;
    private String gender;
    private String address;
    public User(){

    }

    public User(String id, String phoneNumber, String userName, String birthDay, String img, String gender, String address) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.birthDay = birthDay;
        this.img = img;
        this.gender = gender;
        this.address = address;
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
}

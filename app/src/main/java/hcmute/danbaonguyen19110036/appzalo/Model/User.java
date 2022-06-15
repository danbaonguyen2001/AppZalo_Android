package hcmute.danbaonguyen19110036.appzalo.Model;
import java.util.ArrayList;
import java.util.List;

public class User {
    // Id để phân biệt giữa các User
    private String id;
    // Phone Number mà người dùng đã dùng để đăng ký
    private String phoneNumber;
    // Tên user
    private String userName;
    // Ngày sinh
    private String birthDay;
    // avatar user
    private String img;
    // Giới tính của User
    private String gender;
    // Địa chỉ của User
    private String address;
    // Token của điện thoại User
    private String token;
    // Mảng lưu trữ userId của những User đã kết bạn
    private List<String> listFriend;
    // Mảng lưu trữ userId của những User mà ta đã yêu cầu kết bạn trước đó
    private List<String> listRequest;
    // Mảng lưu trữ userId của những User đang yêu cầu kết bạn
    private List<String> listPendingAccept;
    // Mảng lưu trữ các GroupUser
    private List<GroupUser> groupUserList;
    public User(){

    }

    public User(String id, String phoneNumber, String userName, String birthDay, String img, String gender, String address,String token) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.birthDay = birthDay;
        this.img = img;
        this.gender = gender;
        this.address = address;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public List<GroupUser> getGroupUserList() {
        if(groupUserList==null){
            groupUserList = new ArrayList<>();
        }
        return groupUserList;
    }

    public void setGroupUserList(List<GroupUser> groupUserList) {
        this.groupUserList = groupUserList;
    }
}

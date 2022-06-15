package hcmute.danbaonguyen19110036.appzalo.Model;

public class GroupUser {
    // mỗi user sẽ có nhiều group
    // Dùng để lưu xem user này đang ở trong những group nào
    // id để phân biệt với mỗi groupuser khác
    public String id;
    // Dùng để lưu trữ userid
    public String userId;
    //Dùng để lưu trữ groupId
    public String groupId;
    public GroupUser(){

    }

    public GroupUser(String id, String userId, String groupId) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

package hcmute.danbaonguyen19110036.appzalo.Model;

public class Group {

    //Có 2 dạng Group PRIVATE (chỉ 2 người nhắn với nhau , GROUP nhiều người nhắn với nhau)
    // id để phân biệt giữa các Group
    public String id;
    // dùng để phân biệt xem group này thuộc dạng nào , PRIVATE hay GROUP(nhiều người join)
    public String typeGroup;
    // tên group nếu group này thuộc dang GROUP
    public String groupName;
    // Ảnh group nếu group này thuộc dạng GROUP
    public String imgUrl;
    // Lưu lại last message trong Room
    public Message message;
    public Group(){

    }
    public Group(String id,String typeGroup){
        this.id = id;
        this.typeGroup = typeGroup;
    }
    public Group(String id, String typeGroup, String groupName, String imgUrl) {
        this.id = id;
        this.typeGroup = typeGroup;
        this.groupName = groupName;
        this.imgUrl = imgUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

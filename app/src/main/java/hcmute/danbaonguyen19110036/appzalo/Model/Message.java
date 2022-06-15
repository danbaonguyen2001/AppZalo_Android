package hcmute.danbaonguyen19110036.appzalo.Model;

public class Message {
    // id để phân biệt giữa các tin nhắn
    private String id;
    // groupId để phân biệt xem tin nhắn nằm trong group(room) nào
    private String groupId;
    // senderId dùng để lưu trữ Id của người gửi
    private String senderId;
    // message sẽ được gửi đi
    private String message;
    // type message dùng để phân biệt xem message được gửi đi dưới dạng nào
    // hình ảnh ,text, audio
    private String type;
    // Nếu là ảnh thì lưu trữ vào biến imgUrl
    private String imgUrl;
    public Message(){

    }

    public Message(String id, String groupId, String senderId, String message, String type, String imgUrl) {
        this.id = id;
        this.groupId = groupId;
        this.senderId = senderId;
        this.message = message;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

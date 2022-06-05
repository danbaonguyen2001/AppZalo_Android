package hcmute.danbaonguyen19110036.appzalo.Model;

public class Message {
    private String id;
    private String groupId;
    private String senderId;
    private String message;
    public Message(){

    }

    public Message(String id, String groupId, String senderId, String message) {
        this.id = id;
        this.groupId = groupId;
        this.senderId = senderId;
        this.message = message;
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
}

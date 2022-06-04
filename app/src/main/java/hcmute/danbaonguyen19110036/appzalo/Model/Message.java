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
}

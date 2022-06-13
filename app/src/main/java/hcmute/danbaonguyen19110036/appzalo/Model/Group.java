package hcmute.danbaonguyen19110036.appzalo.Model;

public class Group {
    public String id;
    public String typeGroup;
    public String groupName;
    public String imgUrl;
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
}

package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.Group;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;
import hcmute.danbaonguyen19110036.appzalo.listeners.UsersListener;

public class ListUserAdapter extends BaseAdapter {
    private Context context;
    private List<GroupUser> groupUserList;
    private int layout;
    private FirebaseDatabase firebaseDatabase;
    private User user;
    public Group group;
    private String lastMessage="Hello there !",senderId="",typeMessage="",typeGroup="private",groupName="";

    public ListUserAdapter(Context context, List<GroupUser> groupUserList, int layout) {
        this.context = context;
        this.groupUserList = groupUserList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return groupUserList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        private TextView username,newMessage;
        private ImageView avatar;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        initData();
        if(view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.username= (TextView) view.findViewById(R.id.txt_chat_username);
            holder.newMessage = (TextView) view.findViewById(R.id.txt_chat_newmessage);
            holder.avatar =(ImageView) view.findViewById(R.id.img_avataruser);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference("Group").child(groupUserList.get(i).groupId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                group = snapshot.getValue(Group.class);
                // lưu lại các giá trị của Message vào các biến
                if(group.getMessage()!=null){
                    lastMessage = group.getMessage().getMessage();
                    typeMessage = group.getMessage().getType();
                    senderId = group.getMessage().getSenderId();
                    typeGroup = group.getTypeGroup();
                    groupName = group.getGroupName();
                }
                // Lấy ra thông tin receiver trong room chat
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.keepSynced(true);
        databaseReference = firebaseDatabase.getReference("Users").child(groupUserList.get(i).getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if(typeGroup.equals("private")){
                    holder.username.setText(user.getUserName());
                    setLastMessage(holder,user.getUserName());
                    Picasso.get().load(user.getImg()).into(holder.avatar);
                }
                else {
                    holder.username.setText(groupName);
                    setLastMessage(holder,"");
                    Picasso.get().load(group.getImgUrl()).into(holder.avatar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    public void setLastMessage(ViewHolder holder,String receiverName) {
        // Lấy ra tin nhắn cuối cùng được lưu trong group
        if (senderId.equals(Util.currentUser.getId())){
            // Nếu message là hình ảnh thì set cho 1 chuỗi cố định
            if (typeMessage.equals("image")) {
                lastMessage = "Bạn đã gửi một hình ảnh";
            }
            //  Nếu message là audio thì set cho 1 chuỗi cố định
            else if (typeMessage.equals("audio")){
                lastMessage = "Bạn đã gửi một voice chat";
            }
            // Nếu message là text thì lấy đoạn tin nhắn đó ra và lưu vào View
            else {
                lastMessage = "Bạn: "+lastMessage;
            }
        }
        else {
            // Nếu message là hình ảnh thì set cho 1 chuỗi cố định
            if (typeMessage.equals("image")) {
                lastMessage = receiverName+" đã gửi một hình ảnh";
            }
            //  Nếu message là audio thì set cho 1 chuỗi cố định
            else if (typeMessage.equals("audio")){
                lastMessage = receiverName+" đã gửi một voice chat";
            }
            // Nếu message là text thì lấy đoạn tin nhắn đó ra và lưu vào View
            else {
                lastMessage = lastMessage;
//                if(typeGroup.equals("group")){
//                    lastMessage = receiverName+": "+lastMessage;
//                }
            }
        }
        holder.newMessage.setText(lastMessage);
    }
    public void initData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

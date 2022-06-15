package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.Group;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class ListAcceptPendingAdapter extends BaseAdapter {
    private Context context; // Activity của Adapter
    private int layout; // layout item của adapter
    private List<User> userList;// lưu trữ danh sách user
    // Khai báo các Firebase để truy vấn tới database
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    public ListAcceptPendingAdapter(Context context, int layout, List<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }
    public class ViewHolder{
        private Button btnAccept,btnDenied;
        private ImageView avatar;
        private TextView username;
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        initData();
        if(view==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            viewHolder.avatar=view.findViewById(R.id.img_avatar_pending);
            viewHolder.username = view.findViewById(R.id.txt_username_pending);
            viewHolder.btnAccept = view.findViewById(R.id.btn_accept_fr);
            viewHolder.btnDenied = view.findViewById(R.id.btn_denied_fr);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        // Nếu user chấp nhận thì ta tiến hành cập nhật lại dữ liệu
        viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Room để 2 người có thể nhắn tin
                DatabaseReference databaseReference = firebaseDatabase.getReference("Group");
                // tạo 1 key ngẫu nhiên làm groupId
                String key = databaseReference.push().getKey();
                Group group = new Group(key,"private");
                databaseReference.child(key).setValue(group);
                // Cho 2 user join vào Group
                databaseReference = firebaseDatabase.getReference("GroupUser");
                String keyGroup = databaseReference.push().getKey();
                GroupUser groupUserCurrent = new GroupUser(keyGroup, firebaseAuth.getUid(),key);
                databaseReference.child(keyGroup).setValue(groupUserCurrent);
                keyGroup = databaseReference.push().getKey();
                GroupUser groupUserAccept = new GroupUser(keyGroup, user.getId(), key);
                databaseReference.child(keyGroup).setValue(groupUserAccept);

                // Cập nhật lại các list của User hiện tại
                databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
                Util.currentUser.getListPendingAccept().remove(user.getId());
                Util.currentUser.getListFriend().add(user.getId());
                Util.currentUser.getGroupUserList().add(groupUserAccept);
                databaseReference.child("listPendingAccept").setValue(Util.currentUser.getListPendingAccept());
                databaseReference.child("listFriend").setValue(Util.currentUser.getListFriend());
                databaseReference.child("groupUserList").setValue(Util.currentUser.getGroupUserList());

                // Cập nhật lại các list của User được chọn
                databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
                user.getListRequest().remove(Util.currentUser.getId());
                user.getListFriend().add(Util.currentUser.getId());
                user.getGroupUserList().add(groupUserCurrent);
                databaseReference.child("listRequest").setValue(user.getListRequest());
                databaseReference.child("listFriend").setValue(user.getListFriend());
                databaseReference.child("groupUserList").setValue(user.getGroupUserList());

                userList.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }
    // Khơi tạo các View và Firebase
    public void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

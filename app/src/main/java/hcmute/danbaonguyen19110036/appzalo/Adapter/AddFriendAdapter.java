package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class AddFriendAdapter extends BaseAdapter {
    private Context context; // Activity chứa Adapter
    private List<User> userList; // lưu trữ danh sách user
    private int layout;// Layout item của adpater
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    public AddFriendAdapter(Context context, List<User> userList, int layout) {
        this.context = context;
        this.userList = userList;
        this.layout = layout;
    }
    public class ViewHolder{
        // Khai báo các view trong layout item của adater
        public ImageView avatar;
        public TextView username;
        public Button btnAdd,btnRequested;
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
            // Ánh xạ các View
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            viewHolder.avatar=view.findViewById(R.id.img_user_add);
            viewHolder.username = view.findViewById(R.id.txt_username_add);
            viewHolder.btnAdd = view.findViewById(R.id.btn_addfr_add);
            viewHolder.btnRequested=view.findViewById(R.id.btn_requested_addfr);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        if(Util.currentUser.getListRequest().contains(user.getId())){
            viewHolder.btnAdd.setVisibility(View.GONE);
            viewHolder.btnRequested.setVisibility(View.VISIBLE);
        }
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        // Nếu user click vào button addfriend thì ta listRequest của user hiên tại
        // và listpendingaccept của Receiver sẽ được cập nhật lại
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cập nhật dữ liệu lên Server
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(Util.currentUser.getId());
                Util.currentUser.getListRequest().add(user.getId());
                databaseReference.child("listRequest").setValue(Util.currentUser.getListRequest());
                databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
                user.getListPendingAccept().add(Util.currentUser.getId());
                databaseReference.child("listPendingAccept").setValue(user.getListPendingAccept());
                viewHolder.btnAdd.setVisibility(View.GONE);
                viewHolder.btnRequested.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    // Khởi tạo các view và Firebase
    private void initData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

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

public class ListRequestAdapter extends BaseAdapter {
    private Context context; // Activity của adapter
    private int layout;// Layout item của adapter
    private List<User> userList; // lưu trư danh sách user
    private FirebaseDatabase firebaseDatabase;// Truy vấn dữ liệu trên database
    public ListRequestAdapter(Context context,List<User> userList,int layout) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }
    private class ViewHolder{
        private ImageView avatar;
        private TextView username;
        private Button btnRecall;
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
            viewHolder.avatar=view.findViewById(R.id.img_avatar_recall);
            viewHolder.username = view.findViewById(R.id.txt_username_recall);
            viewHolder.btnRecall = view.findViewById(R.id.btn_recall);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // Lấy ra dữ liệu mà đã được click trên ListView
        User user = userList.get(i);
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        viewHolder.btnRecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lấy ra hàng dữ liệu có id user là user đang sử dụng app
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(Util.currentUser.getId());
                // Thu hồi lời mời kết bạn bằng cách xóa userid trong listrequest
                 Util.currentUser.getListRequest().remove(user.getId());
                // Cập nhật lại dữ liệu trên firebase với hàng dữ liệu được lấy ra
                databaseReference.child("listRequest").setValue(Util.currentUser.getListRequest());
                // lấy ra hàng dữ liệu có id user là user được chọn trên listview
                databaseReference = firebaseDatabase.getReference("Users").child(Util.currentUser.getId());
                // Xóa userid trong listacceptpending trong user đã được chọn
                user.getListPendingAccept().remove(Util.currentUser.getId());
                // Cập nhật lại dữ liệu trên firebase với hàng dữ liệu được lấy ra
                databaseReference.child("listPendingAccept").setValue(user.getListPendingAccept());
                // Cập nhật lại ListView
                userList.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public void initData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

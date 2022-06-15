package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ListFriendAdapter extends BaseAdapter {
    private Context context; // Activity của Adapter
    private List<User> userList; // Lưu trữ danh sách user
    private int layout; // Layout item của user adapter

    public ListFriendAdapter(Context context, List<User> userList, int layout) {
        this.context = context;
        this.userList = userList;
        this.layout = layout;
    }
    public class ViewHolder{
        // Khai báo các view của layout
        private ImageView avatar,phone,video;
        private TextView username;
    }
    // trả về số lượng user có được
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
        if(view==null){
            // Ánh xạ các View
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            viewHolder.avatar=view.findViewById(R.id.img_avatar_lf);
            viewHolder.username = view.findViewById(R.id.txt_username_lf);
            viewHolder.phone = view.findViewById(R.id.btn_phone_lf);
            viewHolder.video = view.findViewById(R.id.btn_video_lf);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // load dữ liệu vào các view
        User user = userList.get(i);
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        return view;
    }
}

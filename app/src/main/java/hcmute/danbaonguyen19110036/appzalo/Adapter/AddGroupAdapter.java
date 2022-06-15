package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class AddGroupAdapter extends BaseAdapter {
    private List<User> userList; // Dùng để lưu trữ danh sách user
    private int layout; // Layout item của adpater
    private Context context;//Activity chứa Adapter

    public AddGroupAdapter(List<User> userList, int layout, Context context) {
        this.userList = userList;
        this.layout = layout;
        this.context = context;
    }
    public class ViewHolder{
        // Khai báo các view trong layout item của adater
        public ImageView avatar;
        public Button add,added;
        public TextView username;
    }
    // trả về số lượng độ lớn của userlist
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
            viewHolder.avatar=view.findViewById(R.id.img_user_add_gr);
            viewHolder.username = view.findViewById(R.id.txt_username_add_gr);
            viewHolder.add = view.findViewById(R.id.btn_addfr_add_gr);
            viewHolder.added = view.findViewById(R.id.btn_requested_addfr_gr);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        // Nếu user nhấn vào buttonadd thì cập nhật lại listgroupuser
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.getListGroupUser().add(user);
                viewHolder.add.setVisibility(View.GONE);
                viewHolder.added.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    public void initData(){

    }
}

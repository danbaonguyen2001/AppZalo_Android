package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class AddFriendAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;
    private int layout;
    // firebaseAuth dùng để lấy ra những thông tin của user hiện tại
    private FirebaseAuth firebaseAuth;
    // firebaseDatabase dùng để lấy ra data trong database
    private FirebaseDatabase firebaseDatabase;
    public AddFriendAdapter(Context context, List<User> userList, int layout) {
        this.context = context;
        this.userList = userList;
        this.layout = layout;
    }
    public class ViewHolder{
        public ImageView avatar;
        public TextView username;
        public Button btnAdd;
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
            viewHolder.avatar=view.findViewById(R.id.img_user_add);
            viewHolder.username = view.findViewById(R.id.txt_username_add);
            viewHolder.btnAdd = view.findViewById(R.id.btn_addfr_add);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        viewHolder.username.setText(user.getUserName());
        Picasso.get().load(user.getImg()).into(viewHolder.avatar);
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
                Util.currentUser.getListRequest().add(user.getId());
                databaseReference.child("listRequest").setValue(Util.currentUser.getListRequest());
                databaseReference = firebaseDatabase.getReference("Users").child(user.getId());
                user.getListPendingAccept().add(Util.currentUser.getId());
                databaseReference.child("listPendingAccept").setValue(user.getListPendingAccept());
            }
        });
        return view;
    }
    private void initData(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

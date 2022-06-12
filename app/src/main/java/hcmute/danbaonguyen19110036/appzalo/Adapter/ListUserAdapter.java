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

import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ListUserAdapter extends BaseAdapter {
    private Context context;
    private List<GroupUser> groupUserList;
    private int layout;
    private FirebaseDatabase firebaseDatabase;
    private User user;

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
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(groupUserList.get(i).getUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                holder.username.setText(user.getUserName());
                holder.newMessage.setText("Pending");
                Picasso.get().load(user.getImg()).into(holder.avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    public void initData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}

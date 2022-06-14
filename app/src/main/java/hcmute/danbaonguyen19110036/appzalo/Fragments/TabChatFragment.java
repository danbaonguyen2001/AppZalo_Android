package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Activities.ChatboxActivity;
import hcmute.danbaonguyen19110036.appzalo.Activities.GroupActivity;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListUserAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.GroupUser;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class TabChatFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private ListUserAdapter listUserAdapter;
    private EditText edtSearch;
    private ImageView newGroup;
    public User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab_chat, container, false);
        initData(view);
        List<GroupUser> groupUserList = Util.currentUser.getGroupUserList();
        listUserAdapter = new ListUserAdapter(getActivity(),groupUserList,R.layout.layout_main_tab_chat_item);
        listView.setAdapter(listUserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Lưu giá trị vào intent để sang ChatboxAcitivy ta có thể lấy những giá trị này ra
                String userId = groupUserList.get(i).getUserId();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(userId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        Intent intent=new Intent(getActivity(), ChatboxActivity.class);
                        intent.putExtra("username",user.getUserName());
                        intent.putExtra("imageUrl",user.getImg());
                        intent.putExtra("receiverId",groupUserList.get(i).getUserId());
                        intent.putExtra("roomId",groupUserList.get(i).groupId);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GroupActivity.class));
            }
        });
        return view;
    }
    public void initData(View view){
        // Khơi tạo các khai báo ban đầu
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listView= view.findViewById(R.id.listview_listuser);
        edtSearch = view.findViewById(R.id.edt_search);
        newGroup = view.findViewById(R.id.new_group);
    }
    @Override
    public void onStart() {
        super.onStart();
        listUserAdapter.notifyDataSetChanged();
    }

}

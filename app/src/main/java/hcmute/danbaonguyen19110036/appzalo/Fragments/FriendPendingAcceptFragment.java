package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Activities.ChatboxActivity;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListAcceptPendingAdapter;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListRequestAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class FriendPendingAcceptFragment extends Fragment {
    // Lấy ra các view để xử lý sự kiện
    private ListView lvfriendpd;
    // Khai báo các biến Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    //
    private ListAcceptPendingAdapter listAcceptPendingAdapter;
    private List<User> userList;
    private ListView listViewpd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_pending_accept, container, false);
        initData(view);
        List<String> listUserAcceptPending = Util.currentUser.getListPendingAccept();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    if(listUserAcceptPending.contains(user.getId())==true){
                        userList.add(user);
                    }
                }
                listAcceptPendingAdapter = new ListAcceptPendingAdapter(getActivity(),R.layout.layout_user_pending_request,userList);
                listViewpd.setAdapter(listAcceptPendingAdapter);
                listViewpd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Lưu giá trị vào intent để sang ChatboxAcitivy ta có thể lấy những giá trị này ra
                        Intent intent=new Intent(getActivity(), ChatboxActivity.class);
                        intent.putExtra("username",userList.get(i).getUserName());
                        intent.putExtra("receiverId",userList.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    public void initData(View view){
        // Khơi tạo các khai báo ban đầu
        listViewpd = view.findViewById(R.id.lv_pending_accept);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userList = new ArrayList<>();
    }
}
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
    // Khai báo các biến Firebase dùng để truy vấn
    private FirebaseDatabase firebaseDatabase;
    private ListAcceptPendingAdapter listAcceptPendingAdapter; // Adapter để xử lý với listvew
    private List<User> userList; // dùng để lưu lại danh sách user
    private ListView listViewpd;// Lưu trữ và render các danh sách
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_pending_accept, container, false);
        initData(view);
        List<String> listUserAcceptPending = Util.currentUser.getListPendingAccept(); // lấy ra danh dách userId đang lưu được chờ để chấp nhận kết bạn
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();// Clear list đi vì khi data thay đổi thì hàm này sẽ được chạy
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class); // Duyệt qua từng user và xem user đó có nằm trong listAcceptpending hay không
                    if(listUserAcceptPending.contains(user.getId())==true){
                        userList.add(user);
                    }
                }
                listAcceptPendingAdapter = new ListAcceptPendingAdapter(getActivity(),R.layout.layout_user_pending_request,userList);
                listViewpd.setAdapter(listAcceptPendingAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return view;
    }
    // Khơi tạo các khai báo ban đầu bao gồm View và Firebase
    public void initData(View view){
        listViewpd = view.findViewById(R.id.lv_pending_accept);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
    }
    // Cập nhật lại adapter khi dữ liệu thay đổi
    @Override
    public void onStart() {
        super.onStart();
        if(listAcceptPendingAdapter!=null){
            listAcceptPendingAdapter.notifyDataSetChanged();
        }
    }
}
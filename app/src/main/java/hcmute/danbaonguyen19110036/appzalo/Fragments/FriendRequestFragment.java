package hcmute.danbaonguyen19110036.appzalo.Fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListRequestAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class FriendRequestFragment extends Fragment {
    // Lấy ra các view để xử lý sự kiện
    private ListView lvfriendrq;
    // Khai báo các biến Firebase
    private FirebaseDatabase firebaseDatabase;
    private ListRequestAdapter listRequestAdapter; // adapter để chưa userList
    private List<User> userList; // lưu trữ danh sách user
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        initData(view);
        List<String> listUserRequest = Util.currentUser.getListRequest(); // Lấy ra danh sách userId có trong list request
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); //Clear list đi vì khi data thay đổi thì hàm này sẽ được chạy
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class); // Duyệt qua từng user để xem user đó có trong list request hay không
                    if(listUserRequest.contains(user.getId())==true){
                        userList.add(user);
                    }
                }
                listRequestAdapter = new ListRequestAdapter(getActivity(),userList,R.layout.layout_user_cancel_request_add_friend);
                lvfriendrq.setAdapter(listRequestAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return view;
    }
    // Khơi tạo các khai báo ban đầu
    public void initData(View view){
        lvfriendrq = view.findViewById(R.id.lv_rq_friend);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
    }
    // cập nhật lại adpter khi dữ liệu thay đổi
    @Override
    public void onStart() {
        super.onStart();
        if(listRequestAdapter!=null){
            listRequestAdapter.notifyDataSetChanged();
        }
    }
}
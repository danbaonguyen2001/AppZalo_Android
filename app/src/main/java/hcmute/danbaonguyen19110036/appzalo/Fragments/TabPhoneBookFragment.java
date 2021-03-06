package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Activities.AddFriendActivity;
import hcmute.danbaonguyen19110036.appzalo.Activities.ChatboxActivity;
import hcmute.danbaonguyen19110036.appzalo.Activities.FriendRequestActivity;
import hcmute.danbaonguyen19110036.appzalo.Activities.PhoneBook;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListFriendAdapter;
import hcmute.danbaonguyen19110036.appzalo.Adapter.ListRequestAdapter;
import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class TabPhoneBookFragment extends Fragment {
    // lấy ra các view trong fragment
    private ImageView addfractivity;
    private ConstraintLayout cstFriendRequest,cstPhonebook;
    private FirebaseDatabase firebaseDatabase;
    private ListView listView; // dùng để render ra danh sách dữ liệu
    private List<User> userList; // dùng để lưu trữ danh sách user
    private ListFriendAdapter listFriendAdapter; // adpater để set vào listview
    private Button btnTotal;
    public TabPhoneBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_tab_phonebook, container, false);
        initData(view);
        addfractivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
            }
        });
        cstFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FriendRequestActivity.class));
            }
        });
        cstPhonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PhoneBook.class));
            }
        });
        // lấy ra danh sách bạn bè đã được có id trong mảng ListFriend hiển thị trên listview
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();//Clear list đi vì khi data thay đổi thì hàm này sẽ được chạy
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    if(Util.currentUser.getListFriend().contains(user.getId())==true){
                        userList.add(user);
                    }
                }
                String total = "Tất cả "+String.valueOf(userList.size());
                btnTotal.setText(total);
                listFriendAdapter = new ListFriendAdapter(getActivity(),userList,R.layout.layout_tab_phonebook_user_item);
                listView.setAdapter(listFriendAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    // Khơi tạo các View và Firebase
    public void initData(View view){
        btnTotal = view.findViewById(R.id.btnTotalFriend);
        addfractivity = view.findViewById(R.id.addfractivity);
        cstFriendRequest = view.findViewById(R.id.friendrequest);
        listView = view.findViewById(R.id.lv_listfriend_phonebook);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        cstPhonebook = view.findViewById(R.id.cst_phonebook);
    }
}
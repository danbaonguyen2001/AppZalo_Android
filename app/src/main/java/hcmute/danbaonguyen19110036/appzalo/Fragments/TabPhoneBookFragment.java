package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hcmute.danbaonguyen19110036.appzalo.Activities.AddFriendActivity;
import hcmute.danbaonguyen19110036.appzalo.R;

public class TabPhoneBookFragment extends Fragment {
    // lấy ra các view trong fragment
    private ImageView addfractivity;
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
        return view;
    }
    public void initData(View view){
        addfractivity = view.findViewById(R.id.addfractivity);
    }
}
package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hcmute.danbaonguyen19110036.appzalo.R;
public class TabDiaryFragment extends Fragment {
    // Fragment này nhóm chưa xử lý chỉ mới có giao diện
    public TabDiaryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tab_diary, container, false);
        return view;
    }
}
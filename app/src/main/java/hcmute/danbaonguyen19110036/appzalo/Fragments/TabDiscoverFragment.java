package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import hcmute.danbaonguyen19110036.appzalo.R;

public class TabDiscoverFragment extends Fragment {
    // Fragment này nhóm chưa xử lý chỉ mới có giao diện
    public TabDiscoverFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_discover, container, false);
        return view;
    }
}

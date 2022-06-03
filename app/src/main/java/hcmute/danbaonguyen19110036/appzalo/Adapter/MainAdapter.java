package hcmute.danbaonguyen19110036.appzalo.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;

import hcmute.danbaonguyen19110036.appzalo.Fragments.TabChatFragment;


public class MainAdapter extends FragmentStatePagerAdapter {


    public MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new TabChatFragment();
            default:
                return new TabChatFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}

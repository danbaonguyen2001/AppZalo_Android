package hcmute.danbaonguyen19110036.appzalo.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.danbaonguyen19110036.appzalo.Fragments.FriendRequestFragment;

public class FriendRequestAdapter extends FragmentStatePagerAdapter {

    public FriendRequestAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new FriendRequestFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title="Đã nhận";
                break;
            case 1:
                title="Đã gửi";
                break;
        }
        return title;
    }
}

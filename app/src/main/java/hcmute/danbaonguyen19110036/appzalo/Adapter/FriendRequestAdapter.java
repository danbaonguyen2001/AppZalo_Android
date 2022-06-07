package hcmute.danbaonguyen19110036.appzalo.Adapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import hcmute.danbaonguyen19110036.appzalo.Fragments.FriendPendingAcceptFragment;
import hcmute.danbaonguyen19110036.appzalo.Fragments.FriendRequestFragment;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class FriendRequestAdapter extends FragmentStatePagerAdapter {

    public FriendRequestAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FriendRequestFragment();
        }
        else {
            return new FriendPendingAcceptFragment();
        }
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
                Util.TITLE_REQUEST=1;
                break;
            case 1:
                title="Đã gửi";
                Util.TITLE_REQUEST=2;
                break;
        }
        return title;
    }
}

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
    // Tùy vào postion mà trả về page tương ứng
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FriendPendingAcceptFragment();
        }
        else {
            return new FriendRequestFragment();
        }
    }

    // Số page cần khởi tạo
    @Override
    public int getCount() {
        return 2;
    }
    // Set title cho page
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

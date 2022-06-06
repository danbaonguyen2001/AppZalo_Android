package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import hcmute.danbaonguyen19110036.appzalo.Adapter.FriendRequestAdapter;
import hcmute.danbaonguyen19110036.appzalo.Fragments.FriendRequestFragment;
import hcmute.danbaonguyen19110036.appzalo.R;

public class FriendRequestActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        FriendRequestAdapter friendRequestAdapter= new FriendRequestAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(friendRequestAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
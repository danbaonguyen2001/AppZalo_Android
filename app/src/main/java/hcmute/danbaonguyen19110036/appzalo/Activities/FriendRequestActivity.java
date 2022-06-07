package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import hcmute.danbaonguyen19110036.appzalo.Adapter.FriendRequestAdapter;
import hcmute.danbaonguyen19110036.appzalo.Fragments.FriendRequestFragment;
import hcmute.danbaonguyen19110036.appzalo.R;

public class FriendRequestActivity extends AppCompatActivity {
    // Lấy ra các View để xử lý sự kiện
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friend_request);
        initData();
        FriendRequestAdapter friendRequestAdapter= new FriendRequestAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(friendRequestAdapter);
        tabLayout.setupWithViewPager(viewPager);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendRequestActivity.this,MainActivity.class));
            }
        });
    }
    public void initData(){
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        btnBack = findViewById(R.id.btn_backHome_rq);
    }
}
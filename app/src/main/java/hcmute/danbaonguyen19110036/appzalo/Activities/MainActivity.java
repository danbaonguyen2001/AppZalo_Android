package hcmute.danbaonguyen19110036.appzalo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.danbaonguyen19110036.appzalo.Adapter.MainAdapter;
import hcmute.danbaonguyen19110036.appzalo.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initData();
        MainAdapter mainAdapter= new MainAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.main_menu_tab_chat).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.main_menu_tab_contact);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.main_menu_tab_discover);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.main_menu_tab_story);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.main_menu_tab_user);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_menu_tab_chat:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.main_menu_tab_contact:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.main_menu_tab_discover:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.main_menu_tab_story:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.main_menu_tab_user:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }
    private void initData(){
        viewPager = findViewById(R.id.main_view_pager);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }
}
package com.congnt.androidbase;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;


@Activity(fullscreen = false,
        transitionAnim = Activity.AnimationType.ANIM_BOTTOM_TO_TOP,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM,
        mainLayoutId = R.layout.recycler,
        enableSearch = true)
@NavigationDrawer
public class MainActivity extends AwesomeActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new CustomActionbar(this);
    }

    @Override
    protected void initialize(View mainView) {
        getNavigationView().inflateMenu(R.menu.activity_main_drawer);
        getNavigationView().inflateHeaderView(R.layout.nav_header_main);
        getNavigationView().setNavigationItemSelectedListener(this);
        //Dagger2
//        startActivity(new Intent(this, SecondActivity.class));
        //ViewPager
        /*ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return BlankFragment.newInstance("FRAGMENT "+position);
            }

            @Override
            public int getCount() {
                return 3;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return "TAB"+position;
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);*/
        //RecyclerView
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_send){
            Toast.makeText(MainActivity.this, "aaaaaaaaa", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

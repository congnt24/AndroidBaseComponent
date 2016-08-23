package com.congnt.androidbase;

import android.content.Intent;
import android.view.View;

import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.view.activity.BaseActivity;
import com.congnt.androidbasecomponent.view.widget.BaseLayout;


@Activity(fullscreen = false,
        transitionAnim = BaseActivity.ANIM_BOTTOM_TO_TOP,
        actionbarType = BaseActivity.ACTIONBAR_CUSTOM,
        mainLayoutId = R.layout.recycler,
        enableSearch = true)
public class MainActivity extends BaseActivity {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected BaseLayout getCustomActionBar() {
        return new CustomActionbar(this);
    }

    @Override
    protected void initialize(View mainView) {
        //Dagger2
        startActivity(new Intent(this, SecondActivity.class));
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

}

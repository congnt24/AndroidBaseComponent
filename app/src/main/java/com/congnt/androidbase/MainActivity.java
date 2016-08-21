package com.congnt.androidbase;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.view.activity.BaseActivity;
import com.congnt.androidbasecomponent.view.fragment.BaseFragment;
import com.congnt.androidbasecomponent.view.widget.BaseLayout;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Activity(fullscreen = false,
        transitionAnim = BaseActivity.ANIM_BOTTOM_TO_TOP,
        actionbarType = BaseActivity.ACTIONBAR_CUSTOM,
        mainLayoutId = R.layout.recycler,
        enableSearch = true)
public class MainActivity extends BaseActivity {
    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private UltimateRecyclerView ultimateRecyclerView;

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

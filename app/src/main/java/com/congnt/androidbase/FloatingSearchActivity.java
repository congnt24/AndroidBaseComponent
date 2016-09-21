package com.congnt.androidbase;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;


@Activity(actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigationDrawer
public class FloatingSearchActivity extends AwesomeActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new FloatingSearchActionbar(this);
    }

    @Override
    protected void initialize(View mainView) {
        getNavigationView().inflateMenu(R.menu.activity_main_drawer);
        getNavigationView().inflateHeaderView(R.layout.nav_header_main2);
        getNavigationView().setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}

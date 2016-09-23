package com.congnt.androidbase;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;

@Activity(mainLayoutId = R.layout.activity_map)
@NavigateUp
public class MapActivity extends AwesomeActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new BackableActionBar(this);
    }

    @Override
    protected void initialize(View mainView) {

    }
}

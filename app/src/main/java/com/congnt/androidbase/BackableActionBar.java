package com.congnt.androidbase;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeActionBar;
import com.congnt.androidbasecomponent.annotation.ActionBar;

/**
 * Created by congn_000 on 9/21/2016.
 */
@ActionBar(actionbarType = ActionBar.ActionbarType.DEFAULT_SEARCH)
public class BackableActionBar extends AwesomeActionBar {

    public BackableActionBar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}

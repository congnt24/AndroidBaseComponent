package com.congnt.androidbase;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeActionBar;
import com.congnt.androidbasecomponent.annotation.ActionBar;

/**
 * Created by congn on 8/20/2016.
 */
@ActionBar(actionbarType = ActionBar.ActionbarType.FLOATING_SEARCH)
public class FloatingSearchActionbar extends AwesomeActionBar {
    public FloatingSearchActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}

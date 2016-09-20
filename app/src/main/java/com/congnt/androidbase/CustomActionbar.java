package com.congnt.androidbase;

import android.content.Context;

import com.congnt.androidbasecomponent.annotation.ActionBar;
import com.congnt.androidbasecomponent.Awesome.AwesomeActionBar;

/**
 * Created by congn on 8/20/2016.
 */
@ActionBar(actionbarType = ActionBar.ActionbarType.DEFAULT_SEARCH, leftText = "What the", centerText = "Title", leftDrawableId = android.R.drawable.ic_menu_view)
public class CustomActionbar extends AwesomeActionBar {
    public CustomActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}

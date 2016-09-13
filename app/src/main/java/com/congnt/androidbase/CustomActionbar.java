package com.congnt.androidbase;

import android.content.Context;
import android.widget.Toast;

import com.congnt.androidbasecomponent.annotation.ActionBar;
import com.congnt.androidbasecomponent.view.widget.BaseActionBar;

/**
 * Created by congn on 8/20/2016.
 */
@ActionBar(actionbarType = ActionBar.ActionbarType.MATERIAL_SEARCH, leftText = "What the", centerText = "Title", leftDrawableId = android.R.drawable.ic_menu_view)
public class CustomActionbar extends BaseActionBar {
    public CustomActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
    }

    @Override
    public void onClickLeft() {
        Toast.makeText(getContext(), "Left Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickCenter() {
        Toast.makeText(getContext(), "Center Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickRight() {
        Toast.makeText(getContext(), "Right Click", Toast.LENGTH_SHORT).show();
    }
}

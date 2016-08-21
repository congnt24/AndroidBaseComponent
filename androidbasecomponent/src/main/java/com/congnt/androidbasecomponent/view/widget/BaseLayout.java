package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.congnt.androidbasecomponent.annotation.Widget;

/**
 * Created by congn on 8/19/2016.
 */
public abstract class BaseLayout extends RelativeLayout {
    protected abstract int getLayoutId();
    protected abstract void initAll(View rootView);

    public BaseLayout(Context context) {
        super(context);
        init();
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), getLayoutId(), this);
        initAll(rootView);
    }
}

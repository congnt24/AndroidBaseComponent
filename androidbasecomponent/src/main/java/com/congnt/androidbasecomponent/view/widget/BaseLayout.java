package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

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

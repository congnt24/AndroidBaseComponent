package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by congn on 8/19/2016.
 */
public abstract class AwesomeLayout extends RelativeLayout {
    public AwesomeLayout(Context context) {
        super(context);
        init();
    }

    public AwesomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected abstract int getLayoutId();

    protected abstract void initAll(View rootView);

    private void init() {
        View rootView = View.inflate(getContext(), getLayoutId(), this);
        initAll(rootView);
    }
}

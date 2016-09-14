package com.congnt.androidbasecomponent.Awesome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by NGUYEN TRUNG CONG on 08/13/2016
 */
public abstract class AwesomeFragment extends Fragment {
    protected abstract int getLayoutId();

    protected abstract void initAll(View rootView);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        initAll(rootView);
        return rootView;
    }

}

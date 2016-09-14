package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.annotation.ActionBar;
import com.congnt.androidbasecomponent.view.searchview.FloatingSearchView;
import com.congnt.androidbasecomponent.view.searchview.OnSearchViewFocusListener;
import com.congnt.androidbasecomponent.view.searchview.OnSearchViewListener;

/**
 * Created by congn on 8/19/2016.
 */
public abstract class BaseActionBar extends AwesomeLayout implements OnActionBarClickListener {
    protected TextView tv_left;
    protected TextView tv_center;
    protected TextView tv_right;
    protected ImageButton ib_left;
    protected ImageButton ib_right;
    String leftText, centerText, rightText;
    int leftDrawableId, rightDrawableId;
    private ActionBar.ActionbarType actionbarType;
    private FloatingSearchView floatingSearchView;

    public BaseActionBar(Context context) {
        super(context);
    }

    protected abstract void initialize();

    @Override
    protected int getLayoutId() {
        return R.layout.actionbar_base;
    }

    @Override
    protected void initAll(View rootView) {
        bind(this);
        if (actionbarType == ActionBar.ActionbarType.MATERIAL_SEARCH){
            ((AppCompatActivity) getContext()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));
            ((AppCompatActivity) getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            tv_left = (TextView) rootView.findViewById(R.id.tv_left);
            tv_center = (TextView) rootView.findViewById(R.id.tv_center);
            tv_right = (TextView) rootView.findViewById(R.id.tv_right);
            ib_left = (ImageButton) rootView.findViewById(R.id.ib_left);
            ib_right = (ImageButton) rootView.findViewById(R.id.ib_right);
            initView();
            initListener();
        }else{
            floatingSearchView = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
            rootView.findViewById(R.id.floating_search_bar_container).setVisibility(VISIBLE);
            rootView.findViewById(R.id.toolbar_container).setVisibility(GONE);
            floatingSearchView.setVoiceSearch(true);
            floatingSearchView.setHintTextStyle("Google Play", "Search on google play", new OnSearchViewFocusListener() {
                @Override
                public void onSearchViewFocus(EditText view) {

                }

                @Override
                public void onSearchViewDefocus(EditText view) {

                }
            });
            floatingSearchView.setOnSearchViewListener(new OnSearchViewListener() {
                @Override
                public void onOpenSearchViewListener() {

                }

                @Override
                public void onQueryTextSubmit(String query) {

                }

                @Override
                public void onQueryTextChange(String newText) {

                }
            });
        }
        initialize();
    }

    private void initView(){
        tv_left.setText(leftText);
        tv_center.setText(centerText);
        tv_right.setText(rightText);
        if (leftText.isEmpty()){
            tv_left.setVisibility(GONE);
        }
        if (centerText.isEmpty()){
            tv_center.setVisibility(GONE);
        }
        if (rightText.isEmpty()){
            tv_right.setVisibility(GONE);
        }
        if (leftDrawableId > 0) {
            ib_left.setVisibility(VISIBLE);
            tv_left.setVisibility(GONE);
            ib_left.setImageResource(leftDrawableId);
        }
        if (rightDrawableId > 0) {
            ib_right.setVisibility(VISIBLE);
            tv_right.setVisibility(GONE);
            ib_right.setImageResource(rightDrawableId);
        }
    }

    private void initListener() {
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLeft();
            }
        });
        ib_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLeft();
            }
        });
        tv_center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCenter();
            }
        });
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRight();
            }
        });
        ib_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRight();
            }
        });
    }

    //Define annotation
    public void bind(@NonNull Object target) {
        Class<?> obj = target.getClass();
        if (obj.isAnnotationPresent(ActionBar.class)) {
            ActionBar activity = obj.getAnnotation((ActionBar.class));
            actionbarType = activity.actionbarType();
            leftText = activity.leftText();
            centerText = activity.centerText();
            rightText = activity.rightText();
            leftDrawableId = activity.leftDrawableId();
            rightDrawableId = activity.rightDrawableId();
        }
    }

}

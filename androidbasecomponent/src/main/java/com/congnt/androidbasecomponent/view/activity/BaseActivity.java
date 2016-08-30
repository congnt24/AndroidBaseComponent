package com.congnt.androidbasecomponent.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.view.searchview.MaterialSearchView;
import com.congnt.androidbasecomponent.view.widget.BaseLayout;

/**
 * Created by congn_000 on 8/18/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static final int ANIM_NONE = 0;
    public static final int ANIM_BOTTOM_TO_TOP = 1;
    public static final int ANIM_TOP_TO_BOTTOM = 2;
    public static final int ANIM_RIGHT_TO_LEFT = 3;
    public static final int ANIM_LEFT_TO_RIGHT = 4;
    public static final int ACTIONBAR_NONE = 0;
    public static final int ACTIONBAR_DEFAULT = 1;
    public static final int ACTIONBAR_CUSTOM = 2;

    private ProgressDialog mProgressDialog;
    //Annotation field
    private boolean enableFullscreen;
    private int transitionAnim;
    private int actionbarType;
    ;
    private int mainLayoutId;
    private boolean enableSearch;
    protected MaterialSearchView searchView;

    /**
     * @return 0 to use default activity layout
     */
    protected abstract int getLayoutId();

    /**
     * Override this method when you wanna custom actionbar. Create a subclass of BaseLayout and return an instance of that class.
     */
    protected abstract BaseLayout getCustomActionBar();

    protected abstract void initialize(View mainView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(this);
        if (enableFullscreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        } else {
            setContentView(R.layout.activity_base);
        }

        //Set toolbar
        FrameLayout toolbarLayout = (FrameLayout) findViewById(R.id.layout_actionbar);
        switch (actionbarType) {
            case ACTIONBAR_NONE: //Do nothing
                break;
            case ACTIONBAR_DEFAULT:
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                break;
            case ACTIONBAR_CUSTOM:
                toolbarLayout.addView(getCustomActionBar());
                break;
        }
        //init search bar
        if (enableSearch) {
            searchView = (MaterialSearchView) findViewById(R.id.search_view);
//            searchView.showSearch();
        }
        //Include main layout
        ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub_main);
        viewStub.setLayoutResource(mainLayoutId);
        View mainView = viewStub.inflate();
        initialize(mainView);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startTransition(transitionAnim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startTransition(transitionAnim);
    }

    //Show dialog
    public void showProgressDialog(String message) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            } else {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(message);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Using try catch to catch the case: The activity is not running but still show the dialog.
    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(getString(messageId));
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    private void startTransition(int animationType) {
        switch (animationType) {
            case ANIM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case ANIM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (enableSearch) {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    //Define annotation
    public void bind(@NonNull Object target) {
        Class<?> obj = target.getClass();
        if (obj.isAnnotationPresent(Activity.class)) {
            Activity activity = obj.getAnnotation((Activity.class));
            enableFullscreen = activity.fullscreen();
            transitionAnim = activity.transitionAnim();
            actionbarType = activity.actionbarType();
            mainLayoutId = activity.mainLayoutId();
            enableSearch = activity.enableSearch();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return enableSearch;
    }
}

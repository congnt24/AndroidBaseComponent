package com.congnt.androidbasecomponent.Awesome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;
import com.congnt.androidbasecomponent.view.searchview.MaterialSearchView;

/**
 * Created by congn_000 on 8/18/2016.
 */

public abstract class AwesomeActivity extends AppCompatActivity {

    protected MaterialSearchView searchView;
    private ProgressDialog mProgressDialog;
    //Annotation field
    private boolean enableFullscreen;
    private Activity.AnimationType transitionAnim;
    private Activity.ActionBarType actionbarType;
    private int mainLayoutId;
    private boolean enableSearch;
    private boolean enableDrawer;
    private NavigationView navigationView;
    private boolean enableNavigateUp;

    public NavigationView getNavigationView() {
        return navigationView;
    }

    /**
     * @return 0 to use default activity layout
     */
    protected abstract int getLayoutId();

    /**
     * Override this method when you wanna custom actionbar. Create a subclass of BaseLayout and return an instance of that class.
     */
    protected abstract AwesomeLayout getCustomActionBar();

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
        RelativeLayout main_content = (RelativeLayout) findViewById(R.id.main_content);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (actionbarType) {
            case ACTIONBAR_NONE: //Do nothing
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case ACTIONBAR_CUSTOM:
                main_content.addView(getCustomActionBar());
                if (enableDrawer) {
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    if (toolbar.getVisibility() != View.INVISIBLE) {
                        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
                    }
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                            this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    //Show navigate up if drawer isn't setup
                    if (enableNavigateUp && getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                }
                break;
        }
        //init search bar
        if (enableSearch) {
            searchView = (MaterialSearchView) findViewById(R.id.search_view);
//            searchView.showSearch();
        }
        //Include main layout
        if (mainLayoutId != 0) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub_main);
            viewStub.setLayoutResource(mainLayoutId);
            View mainView = viewStub.inflate();
            initialize(mainView);
        }else{
            initialize(null);
        }

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

    private void startTransition(Activity.AnimationType animationType) {
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (enableSearch) {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        if (obj.isAnnotationPresent(NavigationDrawer.class)) {
            enableDrawer = true;
        }
        if (obj.isAnnotationPresent(NavigateUp.class)) {
            enableNavigateUp = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (enableSearch) {
            getMenuInflater().inflate(R.menu.menu_custom, menu);
            MenuItem item = menu.findItem(R.id.action_search);
            if (searchView != null) searchView.setMenuItem(item);
        }
        return true;
    }

}

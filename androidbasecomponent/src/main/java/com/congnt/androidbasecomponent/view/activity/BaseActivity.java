package com.congnt.androidbasecomponent.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.congnt.androidbasecomponent.R;

/**
 * Created by congn_000 on 8/18/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int ANIM_NONE = 0;
    public static final int ANIM_BOTTOM_TO_TOP = 1;
    public static final int ANIM_TOP_TO_BOTTOM = 2;
    public static final int ANIM_RIGHT_TO_LEFT = 3;
    public static final int ANIM_LEFT_TO_RIGHT = 4;
    private ProgressDialog mProgressDialog;

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract void initListeners();

    /**
     * Ovverite it
     *
     * @return
     */
    protected int getAnimationType() {
        return ANIM_NONE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        initViews();
        initData();
        initListeners();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startTransition(getAnimationType());
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startTransition(getAnimationType());
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
}

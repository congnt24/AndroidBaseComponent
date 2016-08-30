package com.congnt.androidbasecomponent.view.searchview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author Congnt24
 */
public class FloatingSearchView extends BaseSearchView {

    private String hintTextWhenDefocus;
    private String hintTextWhenFocus;
    private boolean isBackPressed = true;
    //Views
    private OnSearchViewListener mOnSearchViewListener;
    private OnSearchViewFocusListener mOnSearchViewFocusListener;


    public FloatingSearchView(Context context) {
        this(context, null);
    }

    public FloatingSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialize() {
        mSearchLayout.setVisibility(VISIBLE);
        //Change margin
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 0);
        mSearchLayoutMain.setLayoutParams(lp);
        //Change height of Search view
        mSearchTopBar.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, (int) DimensionUtil.dpToPx(mContext, 48)));
        //Change to rounded background
        mSearchTopBar.setBackgroundResource(com.congnt.androidbasecomponent.R.drawable.bg_rounded_layout);
        switchToSearchOrBackMode(SEARCH);
    }

    @Override
    protected void onBackClickListener() {
        if (isBackPressed) {
            switchToSearchOrBackMode(BACK);
            if (mOnSearchViewListener != null)
                mOnSearchViewListener.onOpenSearchViewListener();
        } else {
            switchToSearchOrBackMode(SEARCH);
        }
    }

    @Override
    protected void onTintClickListener() {
        switchToSearchOrBackMode(SEARCH);
    }

    @Override
    protected void onSearchViewFocusListener(boolean hasFocus) {
        if (hasFocus) {
            showKeyboard(mSearchSrcTextView);
            switchToSearchOrBackMode(BACK);
            if (mOnSearchViewFocusListener != null) {
                mSearchSrcTextView.setHint(hintTextWhenFocus);
                mSearchSrcTextView.setTextSize(16);
                mOnSearchViewFocusListener.onSearchViewFocus(mSearchSrcTextView);
            }
            if (mOnSearchViewListener != null)
                mOnSearchViewListener.onOpenSearchViewListener();
        } else {
            mSearchTopBar.setBackgroundResource(com.congnt.androidbasecomponent.R.drawable.bg_rounded_layout);
            if (mOnSearchViewFocusListener != null) {
                mSearchSrcTextView.setHint(hintTextWhenDefocus);
                mSearchSrcTextView.setTextSize(22);
                mOnSearchViewFocusListener.onSearchViewDefocus(mSearchSrcTextView);
            }
        }
    }

    @Override
    public void showSuggestions() {
        super.showSuggestions();
        if (mSuggestionsListView.getVisibility() == VISIBLE){
            //change style of topbar when suggestion is opended
            mSearchTopBar.setBackgroundResource(com.congnt.androidbasecomponent.R.drawable.bg_rounded_top_layout);
        }
    }

    @Override
    protected void onQueryTextChange(String text) {
        if (mOnSearchViewListener != null)
            mOnSearchViewListener.onQueryTextChange(text);
    }

    @Override
    protected void onQueryTextSubmit(String text) {
        switchToSearchOrBackMode(SEARCH);
        if (mOnSearchViewListener != null)
            mOnSearchViewListener.onQueryTextSubmit(text);
    }

    public void setOnSearchViewListener(OnSearchViewListener mOnSearchViewListener) {
        this.mOnSearchViewListener = mOnSearchViewListener;
    }

    public void setHintTextStyle(String hintTextWhenDefocus, String hintTextWhenFocus, OnSearchViewFocusListener onSearchViewFocusListener) {
        mSearchSrcTextView.setHint(hintTextWhenDefocus);
        this.hintTextWhenDefocus = hintTextWhenDefocus;
        this.hintTextWhenFocus = hintTextWhenFocus;
        this.mOnSearchViewFocusListener = onSearchViewFocusListener;
    }

    //Util Method

    protected void switchToSearchOrBackMode(int searchOrBack) {
        switch (searchOrBack) {
            case BACK:  //Open search view
                setBackIcon(BACK);
                mSearchSrcTextView.requestFocus();
                isBackPressed = false;
                //Show tintcolor
                mTintView.setVisibility(VISIBLE);
                break;
            case SEARCH:
                setBackIcon(SEARCH);
                clearFocus();
                mSearchSrcTextView.setText(null);
                isBackPressed = true;
                mTintView.setVisibility(GONE);
                break;
        }
    }

}
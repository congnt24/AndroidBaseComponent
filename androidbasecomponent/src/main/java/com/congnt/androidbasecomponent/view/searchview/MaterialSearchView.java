package com.congnt.androidbasecomponent.view.searchview;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.congnt.androidbasecomponent.utility.AnimationUtil;

/**
 * @author Recreate by congnt24
 */
public class MaterialSearchView extends BaseSearchView {
    private MenuItem mMenuItem;
    private boolean mIsSearchOpen = false;
    private int mAnimationDuration;
    private OnSearchViewListener mOnSearchViewListener;
    private SavedState mSavedState;

    //    private boolean submit = false;
    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSearchViewListener(OnSearchViewListener mOnSearchViewListener) {
        this.mOnSearchViewListener = mOnSearchViewListener;
    }

    @Override
    protected void initialize() {
//        first Init
        closeSearch();
    }

    @Override
    protected void onBackClickListener() {
        //Close search when press back
        closeSearch();
    }

    @Override
    protected void onTintClickListener() {
        //Close search when press the tint view
        closeSearch();
    }

    @Override
    protected void onSearchViewFocusListener(boolean hasFocus) {
        //Handle event when focus or defocus: Do nothing
//        if(hasFocus){
//            mTintView.setVisibility(VISIBLE);
//        }else{
//            mTintView.setVisibility(GONE);
//        }
    }

    @Override
    protected void onQueryTextChange(String text) {
        //Handle event when edit text change: Do nothing
        if (mOnSearchViewListener != null)
            mOnSearchViewListener.onQueryTextChange(text);
    }

    @Override
    protected void onQueryTextSubmit(String text) {
        //Handle Event when submit search: Close search and do something else
        if (mOnSearchViewListener != null)
            mOnSearchViewListener.onQueryTextSubmit(text);
    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }

    /**
     * Return true if search is open
     *
     * @return
     */
    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    /**
     * Sets animation duration. ONLY FOR PRE-LOLLIPOP!!
     *
     * @param duration duration of the animation
     */
    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    /**
     * Open Search View. This will animate the showing of the view.
     */
    public void showSearch() {
        showSearch(true);
    }

    /**
     * Open Search View. If animate is true, Animate the showing of the view.
     *
     * @param animate true for animate
     */
    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }
        //Request Focus
        mSearchSrcTextView.setText(null);
        mSearchSrcTextView.requestFocus();

        if (animate) {
            setVisibleWithAnimation();
        } else {
            mSearchLayout.setVisibility(VISIBLE);
            if (mOnSearchViewListener != null)
                mOnSearchViewListener.onOpenSearchViewListener();
        }
        mIsSearchOpen = true;

    }

    private void setVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (mOnSearchViewListener != null)
                    mOnSearchViewListener.onOpenSearchViewListener();
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSearchLayout.setVisibility(View.VISIBLE);
            AnimationUtil.reveal(mSearchLayout, true, animationListener);

        } else {
            AnimationUtil.fadeInView(mSearchLayout, mAnimationDuration, animationListener);
        }
    }
    private void setInVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                mSearchSrcTextView.setText(null);
                dismissSuggestions();
                clearFocus();
                view.setVisibility(GONE);
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtil.reveal(mSearchLayout, false, animationListener);

        } else {
            AnimationUtil.fadeOutView(mSearchLayout, mAnimationDuration);
        }
    }

    /**
     * Close search view.
     */
    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }
        setInVisibleWithAnimation();
//        AnimationUtil.fadeOutView(mSearchLayout, mAnimationDuration, null);
        mIsSearchOpen = false;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;

        return mSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        mSavedState = (SavedState) state;

        if (mSavedState.isSearchOpen) {
            showSearch(false);
            setQuery(mSavedState.query, false);
        }

        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

}
package com.congnt.androidbasecomponent.view.searchview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Congnt24
 */
public abstract class BaseSearchView extends FrameLayout implements Filter.FilterListener {
    public static final int REQUEST_VOICE = 9999;
    public static final int SEARCH = 1;
    public static final int BACK = 2;
    public EditText mSearchSrcTextView;
    protected CharSequence mUserQuery;
    protected CharSequence mOldQueryText;
    //Views
    protected View mSearchLayout;
    protected View mTintView;
    protected ListView mSuggestionsListView;
    protected RelativeLayout mSearchTopBar;
    protected LinearLayout mSearchLayoutMain;
    //End view
    protected Context mContext;
    private boolean mClearingFocus;
    private boolean ellipsize = false;
    private boolean allowVoiceSearch;
    private ImageButton mBackBtn;
    private ImageButton mVoiceBtn;
    private ImageButton mEmptyBtn;
    private final OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v == mBackBtn) {
                onBackClickListener();
            } else if (v == mVoiceBtn) {
                onVoiceClicked();
            } else if (v == mEmptyBtn) {
                mSearchSrcTextView.setText(null);
            } else if (v == mSearchSrcTextView) {
//                showSuggestions();
            } else if (v == mTintView) {
                onTintClickListener();
            }
        }
    };
    private ListAdapter mAdapter;
    private Drawable suggestionIcon;
    private SavedState mSavedState;
    public BaseSearchView(Context context) {
        this(context, null);
    }

    //<-------------Abstract Methods------------>

    public BaseSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        mContext = context;
        initiateView();
        initSearchViewListener();
        initStyle(attrs, defStyleAttr);
    }

    public EditText getSearchBar() {
        return mSearchSrcTextView;
    }

    protected abstract void initialize();

    protected abstract void onBackClickListener();

    protected abstract void onTintClickListener();

    protected abstract void onSearchViewFocusListener(boolean hasFocus);

    protected abstract void onQueryTextChange(String text);

    protected abstract void onQueryTextSubmit(String text);

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, com.congnt.androidbasecomponent.R.styleable.SearchView, defStyleAttr, 0);
        if (a != null) {
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchBackground)) {
                setBackground(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchBackground));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_android_textColor)) {
                setTextColor(a.getColor(com.congnt.androidbasecomponent.R.styleable.SearchView_android_textColor, 0));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_android_textColorHint)) {
                setHintTextColor(a.getColor(com.congnt.androidbasecomponent.R.styleable.SearchView_android_textColorHint, 0));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_android_hint)) {
                setHint(a.getString(com.congnt.androidbasecomponent.R.styleable.SearchView_android_hint));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchVoiceIcon)) {
                setVoiceIcon(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchVoiceIcon));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchCloseIcon)) {
                setCloseIcon(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchCloseIcon));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchBackIcon)) {
                setBackIcon(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchBackIcon));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchSuggestionBackground)) {
                setSuggestionBackground(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchSuggestionBackground));
            }
            if (a.hasValue(com.congnt.androidbasecomponent.R.styleable.SearchView_searchSuggestionIcon)) {
                setSuggestionIcon(a.getDrawable(com.congnt.androidbasecomponent.R.styleable.SearchView_searchSuggestionIcon));
            }
            a.recycle();
        }
    }

    private void initiateView() {
        //Get View from XML
        LayoutInflater.from(mContext).inflate(com.congnt.androidbasecomponent.R.layout.search_view, this, true);
        mSearchLayout = findViewById(com.congnt.androidbasecomponent.R.id.search_layout);
        mSearchLayoutMain = (LinearLayout) findViewById(com.congnt.androidbasecomponent.R.id.search_layout_main);
        mSearchTopBar = (RelativeLayout) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.search_top_bar);
        mSuggestionsListView = (ListView) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.suggestion_list);
        mSearchSrcTextView = (EditText) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.searchTextView);
        mBackBtn = (ImageButton) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.action_up_btn);
        mVoiceBtn = (ImageButton) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.action_voice_btn);
        mEmptyBtn = (ImageButton) mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.action_empty_btn);
        mTintView = mSearchLayout.findViewById(com.congnt.androidbasecomponent.R.id.transparent_view);
        allowVoiceSearch = false;
        showVoice(true);
        initSearchViewListener();
        mSuggestionsListView.setVisibility(GONE);
        mSearchLayout.setVisibility(INVISIBLE);
        //CustomView
        initialize();
    }

    private void initSearchViewListener() {
        //Set Onclick for Item
        mSearchSrcTextView.setOnClickListener(mOnClickListener);
        mBackBtn.setOnClickListener(mOnClickListener);
        mVoiceBtn.setOnClickListener(mOnClickListener);
        mEmptyBtn.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);
        mSearchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });
        mSearchSrcTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserQuery = s;
                startFilter(s);
                BaseSearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(v);
                } else {
                    hideKeyboard(v);
                }
                onSearchViewFocusListener(hasFocus);
            }
        });
    }

    protected void setBackIcon(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackIcon(mContext.getDrawable(type == SEARCH ? com.congnt.androidbasecomponent.R.drawable.ic_search : com.congnt.androidbasecomponent.R.drawable.ic_arrow_back));
        } else {
            setBackIcon(mContext.getResources().getDrawable(type == SEARCH ? com.congnt.androidbasecomponent.R.drawable.ic_search : com.congnt.androidbasecomponent.R.drawable.ic_arrow_back));
        }
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak an item name or number");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyBtn.setVisibility(VISIBLE);
            showVoice(false);
        } else {
            mEmptyBtn.setVisibility(GONE);
            showVoice(true);
        }
        //Custom on query text change
        if (!TextUtils.equals(newText, mOldQueryText)) {
            onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchSrcTextView.getText();
        if (query != "" && TextUtils.getTrimmedLength(query) > 0) {
            onQueryTextSubmit(query.toString());
        }
    }

    protected boolean isVoiceAvailable() {
        /*if (isInEditMode()) {
            return true;
        }*/
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() == 0;
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    //Public Attributes

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchTopBar.setBackground(background);
        } else {
            mSearchTopBar.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mSearchTopBar.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        mSearchSrcTextView.setTextColor(color);
    }

    public void setHintTextColor(int color) {
        mSearchSrcTextView.setHintTextColor(color);
    }

    public void setHint(CharSequence hint) {
        mSearchSrcTextView.setHint(hint);
    }

    public void setVoiceIcon(Drawable drawable) {
        mVoiceBtn.setImageDrawable(drawable);
    }

    public void setCloseIcon(Drawable drawable) {
        mEmptyBtn.setImageDrawable(drawable);
    }

    public void setBackIcon(Drawable drawable) {
        mBackBtn.setImageDrawable(drawable);
    }

    public void setSuggestionIcon(Drawable drawable) {
        suggestionIcon = drawable;
    }

    public void setSuggestionBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSuggestionsListView.setBackground(background);
        } else {
            mSuggestionsListView.setBackgroundDrawable(background);
        }
    }

    public void setCursorDrawable(int drawable) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mSearchSrcTextView, drawable);
        } catch (Exception ignored) {
            Log.e("SearchView", ignored.toString());
        }
    }

    public void setVoiceSearch(boolean voiceSearch) {
        allowVoiceSearch = voiceSearch;
        showVoice(true);
    }

    //Public Methods

    /**
     * Call this method to show suggestions list. This shows up when adapter is set. Call {@link #setAdapter(ListAdapter)} before calling this.
     */
    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getCount() > 0 && mSuggestionsListView.getVisibility() == GONE) {
            mSuggestionsListView.setVisibility(VISIBLE);
        }
    }

    /**
     * Set Adapter for suggestions list. Should implement Filterable.
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsListView.setAdapter(adapter);
        startFilter(mSearchSrcTextView.getText());
    }

    /**
     * Set Adapter for suggestions list with the given suggestion array
     *
     * @param suggestions array of suggestions
     */
    public void setSuggestions(String[] suggestions) {
        if (suggestions != null && suggestions.length > 0) {
            final SearchAdapter adapter = new SearchAdapter(mContext, suggestions, suggestionIcon, ellipsize);
            setAdapter(adapter);
            mSuggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setQuery((String) adapter.getItem(position), true);
                }
            });
        }
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (mSuggestionsListView.getVisibility() == VISIBLE) {
            mSuggestionsListView.setVisibility(GONE);
        }
    }

    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */
    protected void setQuery(CharSequence query, boolean submit) {
        mSearchSrcTextView.setText(query);
        if (query != null) {
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * if show is true, this will enable voice search. If voice is not available on the device, this method call has not effect.
     *
     * @param show
     */
    public void showVoice(boolean show) {
        if (show && isVoiceAvailable() && allowVoiceSearch) {
            mVoiceBtn.setVisibility(VISIBLE);
        } else {
            mVoiceBtn.setVisibility(GONE);
        }
    }

    /**
     * Ellipsize suggestions longer than one line.
     *
     * @param ellipsize
     */
    public void setEllipsize(boolean ellipsize) {
        this.ellipsize = ellipsize;
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(s, BaseSearchView.this);
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (mClearingFocus) return false;
        // Check if SearchView is focusable.
        if (!isFocusable()) return false;
        return mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mClearingFocus = false;
    }
}
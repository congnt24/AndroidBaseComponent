package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.adapter.LoadMoreAdapter;

import java.util.List;

public class RecyclerViewWithSwipeRefresh extends RelativeLayout {
    public RecyclerView recyclerViewActual;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private View layoutLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvMessageNoItem;
    private String mTextNoItem;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private boolean mIsExistHeader = false;
    private boolean loading = false;

    public RecyclerViewWithSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
        setLayout();
        initCompoundView();
        setListener();
    }

    private void init(AttributeSet attrs) {
//        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.AmazingView, 0, 0);
//        try {
//            mTextNoItem = a.getString(R.styleable.AmazingView_textEmptyList);
//        } catch (Exception e) {
//            Log.e("Amazing View", "Cannot init view");
//        } finally {
//            a.recycle();
//        }
    }

    protected void setLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.customview_recycler_with_swipe_refresh, this, true);
    }

    private void initCompoundView() {
        layoutLoading = findViewById(R.id.layout_loading);
        recyclerViewActual = (RecyclerView) findViewById(R.id.rv_actual);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        tvMessageNoItem = (TextView) findViewById(R.id.tv_message_no_item);

        if (mTextNoItem != null) {
            tvMessageNoItem.setText(mTextNoItem);
        }
    }

    private void setListener() {
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        swipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    /**
     * Set action to load more list.
     *
     * @param listener Load more listener.
     */
    public void setOnLoadMoreListener(final OnLoadMoreListener listener) {
        if (mLayoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager mLayoutManager = (LinearLayoutManager) this.mLayoutManager;
            recyclerViewActual.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = true;
                                listener.onLoadMore();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * If you know the list loaded all item, call me to disable load more function.
     */
    public void disableLoadMore() {
        loading = true;
    }

    /**
     * Set exist header or not.
     *
     * @param isExistHeader
     */
    public void setIsExistHeader(boolean isExistHeader) {
        this.mIsExistHeader = isExistHeader;
    }

    /**
     * Custom method setLayoutManager.
     * Only using for Linear layout and Grid layout.
     * If numberColumn < 2, recycle view will have list view layout
     * else it will have grid layout with column = numberColumn
     *
     * @param context
     */
    public void setLayoutManager(Context context, int numberColumn) {
        if (numberColumn < 2) {
            setLayoutManager(new LinearLayoutManager(context));
        } else {
            setLayoutManager(new GridLayoutManager(context, numberColumn));
        }
    }

    public void setLayoutManagerForGridLayoutWithHeader(Context context, final int numberColumn) {
        GridLayoutManager manager = new GridLayoutManager(context, numberColumn);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? numberColumn : 1;
            }
        });
        recyclerViewActual.setLayoutManager(manager);
        mLayoutManager = manager;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * Define layout for recycler view.
     *
     * @param layout
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerViewActual.setLayoutManager(layout);
        this.mLayoutManager = layout;
    }

    /**
     * Set action for pull and refresh list.
     *
     * @param listener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * Call me if the list have no action pull to refresh.
     */
    public void disableRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        recyclerViewActual.setAdapter(this.mAdapter);
    }

    /**
     * Set string content when the list is empty.
     *
     * @param stringId
     */
    public void setTextEmptyList(int stringId) {
        tvMessageNoItem.setText(stringId);
    }

    /**
     * Set string content when the list is empty.
     *
     * @param s
     */
    public void setTextEmptyList(String s) {
        tvMessageNoItem.setText(s);
    }

    /**
     * Refresh list to update newest data.
     */
    public void refreshList() {
        if (isNoItem()) {
            tvMessageNoItem.setVisibility(VISIBLE);
        } else {
            tvMessageNoItem.setVisibility(GONE);
        }
        layoutLoading.setVisibility(GONE);
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    /**
     * Add new list item. Using it when use LoadMoreAdapter
     *
     * @param listItems
     */
    public void addListItems(List listItems) {
        if (mAdapter instanceof LoadMoreAdapter) {
            List list = ((LoadMoreAdapter) mAdapter).getListItem();
            if (list != null && list.size() > 0 && list.get(list.size() - 1) == null) {
                list.remove(list.size() - 1);
            }

            list.addAll(listItems);

            refreshList();
        }
    }

    private boolean isNoItem() {
        if (mAdapter == null) {
            return true;
        }
        return (mAdapter.getItemCount() == 0 && !mIsExistHeader) || (mAdapter.getItemCount() == 1 && mIsExistHeader);
    }

    /**
     *
     */
    public void startLoading() {
        layoutLoading.setVisibility(View.VISIBLE);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position);

        void onItemClick(T t);
    }
}

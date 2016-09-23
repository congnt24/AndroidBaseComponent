package com.congnt.androidbasecomponent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by congn_000 on 9/14/2016.
 */
public abstract class AwesomeRecyclerAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    protected View.OnClickListener onClickListener;
    protected List<T> mList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public AwesomeRecyclerAdapter(Context context, List<T> mList, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.context = context;
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(getItemLayoutId(), parent, false);
        return getViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindHolder(holder, position);
        holder.itemView.setOnClickListener(onClickListener);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract int getItemLayoutId();
    protected abstract VH getViewHolder(View itemView);
    protected abstract void bindHolder(VH holder, int position);
}

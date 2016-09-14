/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.congnt.androidbasecomponent.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.view.widget.RecyclerViewWithSwipeRefresh;

import java.util.List;

public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROGRESS = 2;
    public static final int VIEW_HEADER = 3;

    public List<T> mListItem;
    protected RecyclerViewWithSwipeRefresh.OnItemClickListener onItemClickListener;

    @Override
    public int getItemViewType(int position) {
        return mListItem.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getIdLayoutItem(), parent, false);
            vh = createItemViewHolder(v);
        } else if (viewType == VIEW_PROGRESS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customview_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            bindItemViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem == null ? 0 : mListItem.size();
    }

    public void setOnItemClickListener(RecyclerViewWithSwipeRefresh.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public List<T> getListItem() {
        return mListItem;
    }

    protected abstract int getIdLayoutItem();

    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder createItemViewHolder(View v);

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
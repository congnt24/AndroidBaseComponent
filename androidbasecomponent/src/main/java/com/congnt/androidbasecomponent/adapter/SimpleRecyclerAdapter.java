package com.congnt.androidbasecomponent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by congn_000 on 9/14/2016.
 */
public class SimpleRecyclerAdapter extends AwesomeRecyclerAdapter<SimpleRecyclerAdapter.ViewHolder, String> {

    public SimpleRecyclerAdapter(Context context, List<String> mList, View.OnClickListener onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        String item = mList.get(position);
        if (!item.isEmpty()) {
            holder.bind(item);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(String str) {
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text1.setText(str);
        }
    }
}

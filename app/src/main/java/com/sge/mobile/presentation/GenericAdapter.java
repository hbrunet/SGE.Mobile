package com.sge.mobile.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Daniel on 16/07/13.
 */
public abstract class GenericAdapter extends BaseAdapter {
    private ArrayList<?> items;
    private int R_layout_ViewId;
    private Context context;

    public GenericAdapter(Context context, int R_layout_ViewId, ArrayList<?> items) {
        super();
        this.context = context;
        this.items = items;
        this.R_layout_ViewId = R_layout_ViewId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_ViewId, null);
        }
        onItem(items.get(i), view);
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public abstract void onItem(Object item, View view);

}

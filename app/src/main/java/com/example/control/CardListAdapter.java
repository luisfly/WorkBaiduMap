package com.example.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CardListAdapter extends ArrayAdapter {
    private int resourceId;

    public CardListAdapter() {
        super(null, 0);
    }

    public CardListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

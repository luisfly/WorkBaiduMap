package com.example.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * 弹出式卡片的适配器
 */
public class CardListAdapter extends ArrayAdapter<NerItem> {
    private int resourceId;

    public CardListAdapter() {
        super(null, 0);
    }

    //
    public CardListAdapter(Context context, int textViewResourceId, List<NerItem> nerItemList) {
        super(context, textViewResourceId, nerItemList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        return null;
    }

    public NerItem getItem() {

    }

    public void setNerItems(NerItem nerItems) {

    }
}

package com.example.control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.TruckTask;
import com.example.workbaidumap.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 弹出式卡片的适配器
 */
public class CardListAdapter extends ArrayAdapter<HttpMessageObject> {
    private int resourceId;

    public CardListAdapter() {
        super(null, 0);
    }

    /**
     * 适配器构造函数
     * @param context view所在的活动名称
     * @param textViewResourceId 适配器应用的自定义项视图的布局id,如list_item为listview中每一项的布局layout
     * @param nerItemList 导入的数据列表
     */
    public CardListAdapter(Context context, int textViewResourceId, List<HttpMessageObject> nerItemList) {
        super(context, textViewResourceId, nerItemList);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 从输入的数据参数列表中找到对应的数据项
        HttpMessageObject nerItem =  getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView item_title = (TextView) view.findViewById(R.id.item_title);
        TextView item_detail = (TextView) view.findViewById(R.id.item_detail);

        Log.d("nerItem", nerItem.getTitleText() + " : " + nerItem.getDetailText());

        // 设置文本信息
        item_title.setText(nerItem.getTitleText());
        item_detail.setText(nerItem.getDetailText());
        return view;
    }


    public void setNerItems(NerItem nerItems) {

    }
}

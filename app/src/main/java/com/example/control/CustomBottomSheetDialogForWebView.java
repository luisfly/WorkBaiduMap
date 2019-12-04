package com.example.control;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.FitEntity.HttpMessageObject;
import com.example.workbaidumap.ControllerTestActivity;
import com.example.workbaidumap.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CustomBottomSheetDialogForWebView extends BottomSheetDialog {
    private Context context;
    private NestViewEmbeddedListView listView;
    private List<? extends HttpMessageObject> nerResult;
    private CardListAdapter cardListAdapter;

    public CustomBottomSheetDialogForWebView(@NonNull Context context, List<HttpMessageObject> nerItem) {
        super(context);
        this.context = context;
        this.nerResult = nerItem;
        cardListAdapter = new CardListAdapter(context, R.layout.list_item, nerItem);
        createView();
    }

    /**
     * 配置 view
     */
    public void createView() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.list_show, null);
        setContentView(bottomSheetView);

        // 注意：这里要给layout的parent设置peekHeight，而不是在layout里给layout本身设置，下面设置背景色同理，坑爹！！！
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(((View) bottomSheetView.getParent()));
        bottomSheetBehavior.setPeekHeight(730);

        ((View) bottomSheetView.getParent()).setBackgroundColor(context.getResources().getColor(R.color.transparent));
        listView = bottomSheetView.findViewById(R.id.card_list_view);

        // 配置适配器数据信息
        // cardListAdapter.setNerItems(nerResult);
        // listView配置对应的适配器
        listView.setAdapter(cardListAdapter);

        // 设置listView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder (context);
                dialog.setTitle("装载明细");
                dialog.setMessage("这里显示装载明细");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                   }
                });
                dialog.show();
            }
        });
    }

}

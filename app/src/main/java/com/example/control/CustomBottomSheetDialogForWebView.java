package com.example.control;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.workbaidumap.LoadMessage;
import com.example.workbaidumap.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomBottomSheetDialogForWebView extends BottomSheetDialog {
    private Context context;
    private NestViewEmbeddedListView listView;
    private NerItem nerResult;
    private CardListAdapter cardListAdapter = new CardListAdapter();

    public CustomBottomSheetDialogForWebView(@NonNull Context context, NerItem nerItem) {
        super(context);
        this.context = context;
        this.nerResult = nerItem;
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

        cardListAdapter.setNerItems(nerResult);
        listView.setAdapter(cardListAdapter);
    }

}

package com.example.control;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.DCEntity;
import com.example.FitEntity.Goods;
import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.TruckTask;
import com.example.FitEntity.TruckTaskShow;
import com.example.workbaidumap.ControllerTestActivity;
import com.example.workbaidumap.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class CustomBottomSheetDialogForWebView extends BottomSheetDialog {
    // 活动
    private Context context;
    private NestViewEmbeddedListView listView;
    // 传入的数据列表
    private List<? extends HttpMessageObject> nerResult;
    // 使用的适配器
    private CardListAdapter cardListAdapter;
    // 状态选择
    private int state;
    // 点击事件处理选择参数
    public static final int GOODSHOWS = 0;
    public static final int LOADSELECT = 1;


    // 消息处理器
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    // 展示字符串
                    String goodsDtl = (String) msg.obj;

                    AlertDialog.Builder dialog = new AlertDialog.Builder (context);
                    dialog.setTitle("装载商品明细");
                    dialog.setMessage(goodsDtl);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }break;
                default:break;
            }
        }
    };

    /**
     * 构建
     * @param context 活动主题
     * @param nerItem 数据
     */
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
                // 获得选中项的装在好
                TruckTaskShow ts = (TruckTaskShow) nerResult.get(position);

                // 进行商品明细查询
                new Thread(() -> {
                    // 获取商品信息
                    List<HttpMessageObject> goods = HttpUtils.GetData("@Get_AGoodsDtl", ts, Goods.class);
                    // 消息发送
                    Message message = new Message();
                    // 出错时暂时不进行任何操作
                    if (goods == null) {

                        return;
                    }

                    // 字符串生成
                    StringBuffer show = new StringBuffer();

                    for (HttpMessageObject obj : goods) {
                        Goods good = (Goods) obj;
                        show.append(good.getDetailText() + "\n");
                    }

                    message.obj = show.toString();
                    message.what = 0;

                    // 发送消息
                    handler.sendMessage(message);

                }).start();
            }
        });

        /*switch (state) {
            case GOODSHOWS : {
                // 设置listView的点击事件
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 获得选中项的装在好
                        TruckTaskShow ts = (TruckTaskShow) nerResult.get(position);

                        // 进行商品明细查询
                        new Thread(() -> {
                            // 获取商品信息
                            List<HttpMessageObject> goods = HttpUtils.GetData("@Get_AGoodsDtl", ts, Goods.class);
                            // 消息发送
                            Message message = new Message();
                            // 出错时暂时不进行任何操作
                            if (goods == null) {

                                return;
                            }

                            // 字符串生成
                            StringBuffer show = new StringBuffer();

                            for (HttpMessageObject obj : goods) {
                                Goods good = (Goods) obj;
                                show.append(good.getDetailText() + "\n");
                            }

                            message.obj = show.toString();
                            message.what = 0;

                            // 发送消息
                            handler.sendMessage(message);

                        }).start();
                    }
                });
            }break;
            case LOADSELECT: {

            }break;
            default: {
                Log.e("CustomBottomSheetDialog", "状态选择错误");
            }break;
        }*/
    }

}

package com.example.workbaidumap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FitEntity.Driver;
import com.example.FitEntity.HttpMessageObject;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.DCEntity;
import com.example.FitEntity.Rerror;
import com.example.FitEntity.TruckTask;
import com.example.FitEntity.TruckTaskShow;
import com.example.control.CustomBottomSheetDialogForWebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 装载跟踪页面
 */
public class LoadTrackActivity extends AppCompatActivity {
    private Button road_track;
    private Spinner select_ware;
    private EditText input_paperno;
    private Button paperdtl_qty;
    private TextView paper_show;
    private Button qr_scan;

    private int selectedDC;
    private List<DCEntity> loDC = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    // 获取门店实体信息传输
                    List<String> dcList = (List<String>) msg.obj;

                    // 建立Adapter并且绑定数据源,配置spinner的样式属性
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoadTrackActivity.this, android.R.layout.simple_spinner_item, dcList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 绑定adapter
                    select_ware.setAdapter(adapter);
                    // 配置监听器
                    select_ware.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int pos, long id) {
                            String stores = dcList.get(pos);
                            selectedDC = pos;
                            //Toast.makeText(PurchaseActivity.this, "你点击的仓库是:"+ stores, 2000).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Another interface callback
                        }
                    });
                }break;
                case 1: {
                    HashMap<String, List<HttpMessageObject>> res = (HashMap<String, List<HttpMessageObject>>) msg.obj;
                    TruckTaskShow paperNO = (TruckTaskShow) res.get("tPaperNO").get(0);
                    List<HttpMessageObject> storeDescs = res.get("tStore");
                    List<HttpMessageObject> loadNO = res.get("tLoadNO");

                    StringBuffer show
                            = new StringBuffer().append("当前装车单号： " + paperNO.getTruckPaperNO() +
                            "\n装载数量：" + paperNO.getLoadNum() + "\n目标门店：");

                    for (HttpMessageObject storeDesc : storeDescs   ) {
                        TruckTaskShow each = (TruckTaskShow) storeDesc;
                        show.append(each.getStoreDesc()+":");
                    }

                    Log.d("Taskshow", show.toString());

                    // 设置装车单号初略描述
                    paper_show.setText(show.toString());

                    // 点击事件配置
                    paper_show.setOnClickListener((View v)->{
                        // 装载明细点击展开,弹出菜单
                        CustomBottomSheetDialogForWebView dialog =
                                new CustomBottomSheetDialogForWebView(LoadTrackActivity.this, loadNO);
                        dialog.show();
                    });
                }break;
                default:break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全透明沉浸式状态栏
        setStatusBarFullTransparent();

        setContentView(R.layout.activity_load_track);

        road_track = (Button) findViewById(R.id.road_track_commit);
        select_ware = (Spinner) findViewById(R.id.select_ware_lt);
        paperdtl_qty = (Button) findViewById(R.id.lt_paperdtl_qty);
        input_paperno = (EditText) findViewById(R.id.input_lt_paperno);
        paper_show = (TextView) findViewById(R.id.paper_show_lt);
        qr_scan = (Button) findViewById(R.id.qr_lt);


        // 仓库信息获取
        LoadTruck();

        // 控件事件初始化
        initController();
    }

    private void LoadTruck() {
        //
        new Thread(()->{
            // 获取仓库信息
            List<HttpMessageObject> dcEntities = HttpUtils.GetData("@Get_ADC", new DCEntity());
            // 消息发送
            Message message = new Message();
            // 出错时暂时不进行任何操作
            if(dcEntities == null) {

                return ;
            }

            List<String> dcList = new ArrayList<String>();
            DCEntity dc;

            for(HttpMessageObject obj : dcEntities) {
                dc = (DCEntity) obj;
                dcList.add(dc.getsDCDesc());
                loDC.add(dc);
            }

            message.obj = dcList;
            message.what = 0;

            // 发送消息
            handler.sendMessage(message);

        }).start();

    }

    private void initController() {
        road_track.setOnClickListener((View v)->{
            Intent intent = new Intent(LoadTrackActivity.this, LocationActivity.class);
            TruckTask truckTask = new TruckTask();
            truckTask.setSiteNO(loDC.get(selectedDC).getsDCNO());
            truckTask.setDriverNO(((Driver) getIntent().getSerializableExtra("Driver")).getDriverNO());
            truckTask.setTruckPaperNO(input_paperno.getText().toString());
            intent.putExtra("PaperNO", truckTask);
            startActivity(intent);
        });

        // 查询装载单号信息
        paperdtl_qty.setOnClickListener((View v)->{
            new Thread(()->{
                TruckTask truckTask = new TruckTask();
                truckTask.setDCNO(loDC.get(selectedDC).getsDCNO());
                truckTask.setTruckPaperNO(input_paperno.getText().toString());

                // 各返回值接收
                HashMap<String, Class<? extends HttpMessageObject>> parm = new HashMap<>();
                parm.put("tPaperNO", TruckTaskShow.class);
                parm.put("tStore", TruckTaskShow.class);
                parm.put("tLoadNO", TruckTaskShow.class);

                // 查询
                HashMap<String, List<HttpMessageObject>> res =
                        HttpUtils.GetData("@Get_AqryPaperNO", truckTask, parm);

                Message message = new Message();

                // 出错时返回错误信息
                if(res.get("error") != null) {
                    message.what = 999;
                    message.obj = ((Rerror) res.get("error").get(0)).getError();

                    // 发送消息
                    handler.sendMessage(message);
                    return ;
                }

                message.obj = res;
                message.what = 1;

                // 发送消息
                handler.sendMessage(message);
            }).start();
        });

        qr_scan.setOnClickListener((View v)->{

        });
    }

    /**
     * 全透明沉浸式状态栏方法
     * 注意：方法要放在 super.onCreate 后，还有是 setContentView 前
     * 并且要在本活动布局文件根布局中增添 android:fitsSystemWindows="false"
     */
    private void setStatusBarFullTransparent() {
        // 21版本 Android 5.0,4.4以下不支持沉浸试共嗯
        if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            View decorView = window.getDecorView();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 注意 SYSTEM_UI_FLAG_LIGHT_STATUS_BAR , SYSTEM_UI_FLAG_LAYOUT_STABLE 影响装填栏字体，stable为浅色 bar 为深色
            // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}

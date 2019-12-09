package com.example.workbaidumap;

import android.app.Activity;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.FitEntity.HttpMessageObject;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.DCEntity;
import com.example.FitEntity.Driver;
import com.example.FitEntity.Rerror;
import com.example.FitEntity.TruckTask;
import com.example.FitEntity.TruckTaskShow;
import com.example.FitEntity.UpdateTruckTask;
import com.example.control.CustomBottomSheetDialogForWebView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 司机货物发车模块
 */
public class StartTransationActivity extends AppCompatActivity {
    private Spinner start_DC;
    private Button start_commit;
    private EditText start_input_paperno;
    private Button paperNO_qry;
    private TextView paper_show_st;
    private Button start_cancel;
    private Button start_endTrc;
    private Button start_Task;
    private Button start_LoadFollow;


    // 全局通用变量
    private List<DCEntity> loDC = new ArrayList<>();
    private int selectedDC = 0;

    // 消息处理器
    private Handler handler = new Handler() {

        public void handleMessage(@NotNull Message msg) {
            switch (msg.what) {
                case 0: {
                    // 获取门店实体信息传输
                    List<String> dcList = (List<String>) msg.obj;

                    // 建立Adapter并且绑定数据源,配置spinner的样式属性
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartTransationActivity.this, android.R.layout.simple_spinner_item, dcList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 绑定adapter
                    start_DC.setAdapter(adapter);
                    // 配置监听器
                    start_DC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    paper_show_st.setText(show.toString());

                    // 点击事件配置
                    paper_show_st.setOnClickListener((View v)->{
                        // 装载明细点击展开,弹出菜单
                        CustomBottomSheetDialogForWebView test =
                                new CustomBottomSheetDialogForWebView(StartTransationActivity.this, loadNO);
                        test.show();
                    });

                }break;
                case 999: {
                    // 出错处理
                    AlertDialog.Builder dialog = new AlertDialog.Builder (StartTransationActivity.this);
                    dialog.setTitle("错误");
                    dialog.setMessage((String) msg.obj);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                default:break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全透明沉浸式状态栏
        setStatusBarFullTransparent();

        setContentView(R.layout.activity_start_transation);
        start_DC = (Spinner) findViewById(R.id.start_select_DC);
        start_commit = (Button) findViewById(R.id.start_commit);
        start_input_paperno = (EditText) findViewById(R.id.start_input_paperno);
        paperNO_qry = (Button) findViewById(R.id.s_paperdtl_qty);
        paper_show_st = (TextView) findViewById(R.id.paper_show_st);
        start_cancel = (Button) findViewById(R.id.start_cancel);
        start_endTrc = (Button) findViewById(R.id.start_endTrc);
        start_Task = (Button) findViewById(R.id.start_Task);
        start_LoadFollow = (Button) findViewById(R.id.start_LoadFollow);

        LoadStore();
        initController();
    }

    /**
     * 加载仓库信息
     */
    private void LoadStore() {
        // 加载仓库信息
        new Thread(()->{
            // 获取仓库信息
            List<HttpMessageObject> dcEntities = HttpUtils.GetData("@Get_ADC", new DCEntity());
            // 消息发送
            Message message = new Message();
            // 出错时返回错误信息
            if(dcEntities.get(0) instanceof Rerror) {
                message.what = 999;
                message.obj = ((Rerror) dcEntities.get(0)).getError();

                // 发送消息
                handler.sendMessage(message);
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

    /**
     * 初始化控件事件
     */
    private void initController() {
        // 初始化控件
        start_commit.setOnClickListener((View v)->{

            // 访问 fit
           new Thread(()->{
               Driver driver = (Driver) getIntent().getSerializableExtra("Driver");
                // 更新参数初始化
               UpdateTruckTask updateTruckTask = new UpdateTruckTask();
               updateTruckTask.setDCNO(loDC.get(selectedDC).getsDCNO());
               updateTruckTask.setStartDriver(driver.getDriverName());
               updateTruckTask.setDriverNO(driver.getDriverNO());
               updateTruckTask.setPaperNO(start_input_paperno.getText().toString());

               HttpUtils.PostSingleData("@Post_ATruckStart", updateTruckTask);
           }).start();
        });

        paperNO_qry.setOnClickListener((View v)->{
            // 获得装载明细
            new Thread(()->{
                TruckTask truckTask = new TruckTask();
                truckTask.setDCNO(loDC.get(selectedDC).getsDCNO());
                truckTask.setTruckPaperNO(start_input_paperno.getText().toString());

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

        // 返回
        start_cancel.setOnClickListener((View v)->{
            StartTransationActivity.this.finish();
        });


        start_endTrc.setOnClickListener((View v)->{
            Intent intent = new Intent(StartTransationActivity.this, PurchaseActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
        });

        start_Task.setOnClickListener((View v)->{
            Intent intent = new Intent(StartTransationActivity.this, TaskActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
        });

        start_LoadFollow.setOnClickListener((View v)->{
            Intent intent = new Intent(StartTransationActivity.this, LoadTrackActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
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

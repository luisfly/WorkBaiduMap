package com.example.workbaidumap;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FitEntity.HttpMessageObject;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.DCEntity;
import com.example.FitEntity.Driver;
import com.example.FitEntity.Store;
import com.example.FitEntity.UpdateTruckTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 司机交货活动
 */
public class PurchaseActivity extends AppCompatActivity {
    // 控件
    private Spinner spinner;
    private Spinner wareSelect;
    private Button purchase_commit;
    private EditText input_ploadno;
    // 全局通用变量
    private List<Store> loStore = new ArrayList<>();
    private List<DCEntity> loDC = new ArrayList<>();
    private int selectedStore = 0;
    private int selectedDC = 0;

    private Handler handler = new Handler() {

        public void handleMessage(@NotNull Message msg) {
            switch (msg.what) {
                case 0 :{
                    // 获取门店实体信息传输
                    List<String> storeList = (List<String>) msg.obj;

                    // 建立Adapter并且绑定数据源,配置spinner的样式属性
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PurchaseActivity.this, android.R.layout.simple_spinner_item, storeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 绑定adapter
                    spinner.setAdapter(adapter);
                    // 配置监听器
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int pos, long id) {
                            String stores = storeList.get(pos);
                            selectedStore = pos;
                            //Toast.makeText(PurchaseActivity.this, "你点击的仓库是:"+ stores, 2000).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Another interface callback
                        }
                    });
                }break;
                case 1:{
                    List<String> dcList = (List<String>) msg.obj;

                    // 建立Adapter并且绑定数据源,配置spinner的样式属性
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PurchaseActivity.this, android.R.layout.simple_spinner_item, dcList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 绑定adapter
                    wareSelect.setAdapter(adapter);
                    // 配置监听器
                    wareSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int pos, long id) {
                            String stores = dcList.get(pos);
                            selectedDC = pos;
                            //Toast.makeText(PurchaseActivity.this, "你点击的门店是:"+ stores, 2000).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Another interface callback
                        }
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

        setContentView(R.layout.activity_purchase);
        spinner = (Spinner) findViewById(R.id.select_store_et);
        purchase_commit = (Button) findViewById(R.id.purchase_commit);
        input_ploadno = (EditText) findViewById(R.id.input_ploadno);
        wareSelect = (Spinner) findViewById(R.id.select_ware_et);

        // 加载门店下拉列表
        LoadStore();

        // 初始化控件事件
        initController();

    }

    /**
     * 初始化控件事件
     */
    private void initController() {
        // 交货确认按钮事件初始化
        purchase_commit.setOnClickListener((View v)->{
            // 普通方式创建线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 传送对象初始化
                    UpdateTruckTask updateTruckTask = new UpdateTruckTask();
                    updateTruckTask.setStoreNO(loStore.get(selectedStore).getsStoreNO());
                    updateTruckTask.setDCNO(loDC.get(selectedDC).getsDCNO());
                    updateTruckTask.setTruckLoadNO(input_ploadno.getText().toString());

                    Driver driver = (Driver) getIntent().getSerializableExtra("Driver");
                    updateTruckTask.setDriverNO(driver.getDriverNO());
                    updateTruckTask.setSignDriver(driver.getDriverName());
                    HttpUtils.PostSingleData("@Post_ATruckToStore", updateTruckTask);
                }
            }).start();
        });
    }

    /**
     * 加载门店信息
     */
    private void LoadStore() {
        // lamda表达式创建线程
        new Thread(()->{
            // 获取门店信息
            List<HttpMessageObject> storeEntities = HttpUtils.GetData("@Get_AStore", new Store());
            // 消息发送
            Message message = new Message();
            // 出错时暂时不进行任何操作
            if(storeEntities == null) {

                return ;
            }

            List<String> store = new ArrayList<String>();
            Store st;

            for(HttpMessageObject obj : storeEntities) {
                st = (Store) obj;
                store.add(st.getsStoreDesc());
                loStore.add(st);
            }

            message.obj = store;
            message.what = 0;

            // 发送消息
            handler.sendMessage(message);

        }).start();

        // 加载仓库信息
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
            message.what = 1;

            // 发送消息
            handler.sendMessage(message);

        }).start();

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

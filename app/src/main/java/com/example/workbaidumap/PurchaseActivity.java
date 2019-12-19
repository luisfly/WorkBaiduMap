package com.example.workbaidumap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.FitEntity.HttpMessageObject;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.DCEntity;
import com.example.FitEntity.Driver;
import com.example.FitEntity.Rerror;
import com.example.FitEntity.Store;
import com.example.FitEntity.TruckTask;
import com.example.FitEntity.TruckTaskShow;
import com.example.FitEntity.UpdateTruckTask;
import com.example.control.CustomBottomSheetDialogForWebView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 司机交货活动
 */
public class PurchaseActivity extends AppCompatActivity {
    // 控件
    private Spinner spinner;
    private Spinner wareSelect;
    private Button purchase_commit;
    private Button pur_cancel;
    private Button pur_task;
    private Button pur_loadfollow;
    private Button pur_startTrc;
    private Button paperDtl;
    private EditText input_ploadno;
    private EditText input_ppaperno;
    private TextView loaddtl_et;
    private ImageButton paperno_qr_et;
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
                case 2: {
                    List<HttpMessageObject> res = (List<HttpMessageObject>) msg.obj;
                    TruckTaskShow show = (TruckTaskShow)(res.get(0));

                    // 更新明细框，构建点击事件
                    loaddtl_et.setText("装车单号：" + input_ppaperno.getText().toString() + "\n装载号："
                            + input_ploadno.getText().toString() + "\n商品种类数量：" + show.getGoodsSort()
                            + "\n出发时间：" + show.getStartTime());

                    loaddtl_et.setOnClickListener((View v)->{

                    });

                    // 数据初始化
                    // List<HttpMessageObject> truckData = new ArrayList<>();


                    // 点击展开，进行多选操作
                    /*loaddtl_et.setOnClickListener((View v)->{
                        CustomBottomSheetDialogForWebView test =
                                new CustomBottomSheetDialogForWebView(PurchaseActivity.this, truckData);
                        test.show();
                    });*/

                }break;
                case 3: {
                    AlertDialog.Builder dialog = new AlertDialog.Builder (PurchaseActivity.this);
                    dialog.setTitle("交货");
                    dialog.setMessage((String) msg.obj);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }break;
                case 999: {
                    // 出错处理
                    AlertDialog.Builder dialog = new AlertDialog.Builder (PurchaseActivity.this);
                    dialog.setTitle("错误");
                    dialog.setMessage((String) msg.obj);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        paperDtl = (Button) findViewById(R.id.p_paperdtl_qty);
        input_ppaperno = (EditText) findViewById(R.id.input_p_paperno);
        loaddtl_et = (TextView) findViewById(R.id.loaddtl_et);
        paperno_qr_et = (ImageButton) findViewById(R.id.paperno_qr_et);
        pur_cancel = (Button) findViewById(R.id.pur_cancel);
        pur_loadfollow = (Button) findViewById(R.id.pur_loadfollow);
        pur_startTrc = (Button) findViewById(R.id.pur_startTrc);
        pur_task = (Button) findViewById(R.id.pur_task);


        // 加载门店下拉列表
        LoadStore();

        // 初始化控件事件
        initController();

    }

    /**
     * 初始化控件事件
     */
    private void initController() {

        paperDtl.setOnClickListener((View v)->{
            String paperNO = input_ppaperno.getText().toString();
            String truckLoadNO = input_ploadno.getText().toString();
            if (paperNO.equals("")) {

            } else {
                new Thread(()->{
                    // 数据查询
                    TruckTask truckTask = new TruckTask();
                    // fit 配置
                    truckTask.setTruckPaperNO(paperNO);
                    truckTask.setTruckLoadNO(truckLoadNO);
                    truckTask.setDriverNO(((Driver)getIntent().getSerializableExtra("Driver")).getDriverNO());
                    truckTask.setSiteNO(loDC.get(selectedDC).getsDCNO());

                    // 返回结构接收
                    List<HttpMessageObject> res
                            = HttpUtils.GetData("@Get_ATruckToStore", truckTask, TruckTaskShow.class);
                    Message message = new Message();

                    // 出错时返回错误信息
                    if(res.get(0) instanceof Rerror) {
                        message.what = 999;
                        message.obj = ((Rerror) res.get(0)).getError();

                        // 发送消息
                        handler.sendMessage(message);
                        return ;
                    }

                    message.obj = res;
                    message.what = 2;

                }).start();
            }
        });


        // 交货确认按钮事件初始化,12.05需要修改
        purchase_commit.setOnClickListener((View v)->{
            initLocationOption();
        });

        paperno_qr_et.setOnClickListener((View v)->{
            Intent intent = new Intent(PurchaseActivity.this, QrScanActivity.class);
            startActivityForResult(intent, 1);
        });

        // 返回
        pur_cancel.setOnClickListener((View v)->{
            PurchaseActivity.this.finish();
        });

        // 上侧控件初始化
        pur_startTrc.setOnClickListener((View v)->{
            Intent intent = new Intent(PurchaseActivity.this, StartTransationActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
            finish();
        });

        pur_task.setOnClickListener((View v)->{
            Intent intent = new Intent(PurchaseActivity.this, TaskActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
            finish();
        });

        pur_loadfollow.setOnClickListener((View v)->{
            Intent intent = new Intent(PurchaseActivity.this, LoadTrackActivity.class);
            intent.putExtra("Driver", (Driver) getIntent().getSerializableExtra("Driver"));
            startActivity(intent);
            finish();
        });

        // textview 滚动设置
        loaddtl_et.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * 下拉菜单选项初始化
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
     * 扫码返回值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:{
                if (resultCode == RESULT_OK) {
                    String resultdata = data.getStringExtra("data_return");
                    input_ppaperno.setText(resultdata);
                }
                break;
            }
            default:break;
        }
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

    /**
     * 定位监听器
     */
    private class PurchaseLoactionListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();

            if (latitude != 4.94065645841247E-324 && longitude != 4.94065645841247E-324) {

                // 上传当前位置还有单号信息进行验证
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 传送对象初始化
                        UpdateTruckTask updateTruckTask = new UpdateTruckTask();
                        updateTruckTask.setStoreNO(loStore.get(selectedStore).getsStoreNO());
                        updateTruckTask.setDCNO(loDC.get(selectedDC).getsDCNO());
                        updateTruckTask.setTruckLoadNO(input_ploadno.getText().toString());
                        updateTruckTask.setPaperNO(input_ppaperno.getText().toString());

                        Driver driver = (Driver) getIntent().getSerializableExtra("Driver");
                        updateTruckTask.setDriverNO(driver.getDriverNO());
                        updateTruckTask.setSignDriver(driver.getDriverName());

                        // 添加当前位置信息，同时上传验证
                        updateTruckTask.setLatitude(latitude);
                        updateTruckTask.setLongitude(longitude);

                        // 传输成功或者失败, 返回信息
                        String res = HttpUtils.PostSingleData("@Post_ATruckToStore", updateTruckTask);
                        Message message = new Message();

                        message.what = 3;
                        message.obj = res;

                        handler.sendMessage(message);
                    }
                }).start();
            } else {
                Message message = new Message();

                message.what = 3;
                message.obj = "定位失败，请打开GPS";

                handler.sendMessage(message);
            }
        }
    }

    /**
     * 2019.9.10 连续定位功能测试，定位功能设置
     *      9.11 发现若开启autoNotifyMode将会屏蔽掉自动扫描功能，只有当位置发生变化才会执行回调函数
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        PurchaseActivity.PurchaseLoactionListener myLocationListener = new PurchaseActivity.PurchaseLoactionListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);

        /* 9.11 标注开启下面的自动回调模式之后定时扫描位置功能关闭，只有当位置发生变化是才会执行listener中的回调函数 */
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        //locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        //locationOption.setOpenAutoNotifyMode(3000,3, LocationClientOption.LOC_SENSITIVITY_HIGHT);


        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
    }
}

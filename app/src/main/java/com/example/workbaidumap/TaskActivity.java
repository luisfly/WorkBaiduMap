package com.example.workbaidumap;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.FitEntity.HttpMessageObject;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.Driver;
import com.example.FitEntity.TruckGoods;
import com.example.FitEntity.TruckTask;
import com.example.FitEntity.TruckTaskShow;
import com.example.control.CustomBottomSheetDialogForWebView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * 运输任务查询 可查看运输线路、运行轨迹、装载号明细。
 */
public class TaskActivity extends AppCompatActivity {
    private HashMap<String, List<HttpMessageObject>> res = new HashMap<>();
    private TextView loadtask;

    private Handler handler = new Handler(){

        public void handleMessage(@NotNull Message msg) {
            switch (msg.what) {
                case 0:{
                    HashMap<String, List<HttpMessageObject>> nowtask =
                            (HashMap<String, List<HttpMessageObject>>) msg.obj;
                    List<HttpMessageObject> nowPaperNO = nowtask.get("tTruckLoadingDriver");
                    List<HttpMessageObject> nowLoadNO = nowtask.get("tTruckTransTask");

                    StringBuffer show = new StringBuffer();

                    for (HttpMessageObject each : nowPaperNO) {
                        TruckTaskShow ts = (TruckTaskShow) each;
                        show.append("装载单号：" + ts.getTruckPaperNO() + "\n出发时间：" + ts.getStartTime() + "\n");
                    }

                    // 显示文本设计
                    loadtask.setText(show.toString());

                    // 点击事件配置
                    loadtask.setOnClickListener((View v)->{
                        // 装载明细点击展开,弹出菜单
                        CustomBottomSheetDialogForWebView sheet =
                                new CustomBottomSheetDialogForWebView(TaskActivity.this, nowLoadNO);
                        sheet.show();
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

        /* 百度地图初始化，必须要有 */
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        setContentView(R.layout.activity_task);

        loadtask = (TextView) findViewById(R.id.loadtask);

        // 获取当前司机的装载单号详细信息
        LoadTruck();

        // 测试
        // GoodsDtl();
    }

    // 获取当前司机的装载单情况
    private void LoadTruck() {
        // 加载装载单号以及装载号信息
        new Thread(()->{
            // 获取司机信息
            Driver driver = (Driver) getIntent().getSerializableExtra("Driver");

            // 参数条件
            TruckTask truckTask = new TruckTask();
            truckTask.setDriverNO(driver.getDriverNO());
            // 要接收的表以及用于接收的类
            HashMap<String, Class<? extends HttpMessageObject>> parm = new HashMap<>();
            parm.put("tTruckLoadingDriver", TruckTaskShow.class);
            parm.put("tTruckTransTask", TruckTaskShow.class);


            // 获取装载单号信息
            HashMap<String, List<HttpMessageObject>> TruckEntity
                    = HttpUtils.GetData("@Get_NowTruckLoading", truckTask, parm);
            // 消息发送
            Message message = new Message();
            // 出错时暂时不进行任何操作
            if(TruckEntity == null || TruckEntity.size() == 0) {

                return ;
            }

            /*List<String> dcList = new ArrayList<String>();
            DCEntity dc;

            for(HttpMessageObject obj : dcEntities) {
                dc = (DCEntity) obj;
                dcList.add(dc.getsDCDesc());
                loDC.add(dc);
            }*/

            message.obj = TruckEntity;
            message.what = 0;
            res = TruckEntity;

            // 发送消息
            handler.sendMessage(message);

            // 数据测试
            /*List<HttpMessageObject> truckNO = TruckEntity.get("tTruckLoadingDriver");
            TruckTask truckTask1 = (TruckTask) truckNO.get(0);
            Log.i("tTruckLoadingDriver", truckTask1.getTruckPaperNO() + ":" + truckTask1.getDriverNO() + ":" +
                    truckTask1.getStartTime() + ":" + truckTask1.getEndTime());

            List<HttpMessageObject> LoadNOs = TruckEntity.get("tTruckTransTask");
            for(HttpMessageObject loadNO : LoadNOs) {
                TruckTask LoadNO = (TruckTask) loadNO;
                Log.i("tTruckTransTask", LoadNO.getTruckPaperNO() + " : " + LoadNO.getTruckLoadNO() + " : " + LoadNO.getDCNO()
                    + " : " + LoadNO.getSiteNO() + " : " + LoadNO.getDriver());
            }*/

        }).start();

    }

    /**
     * 获取商品明细,现在未配置参数
     * 现在废弃
     */
    /*private void GoodsDtl() {
        // 启动线程处理网络任务
        new Thread(()->{
            TruckGoods truckGoods = new TruckGoods();
            // 设置查找参数
            truckGoods.setTruckPaperNO("");
            truckGoods.setTruckLoadNO("");

            // 消息发送
            Message message = new Message();

            List<HttpMessageObject> goods = HttpUtils.GetData("@Get_ATruckLoadDtl", truckGoods);
            if (goods == null || goods.size() == 0) {
                return ;
            }

            for (HttpMessageObject good : goods) {
                TruckGoods tg = (TruckGoods) good;
                Log.d("Goods", tg.getDCNO() + " : " + tg.getGoodsNO() + " : " + tg.getTruckLoadNO() + " : " +
                        tg.getTruckPaperNO());
            }

            message.obj = truckGoods;
            message.what = 0;

            // 发送消息
            handler.sendMessage(message);

        }).start();
    }*/

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

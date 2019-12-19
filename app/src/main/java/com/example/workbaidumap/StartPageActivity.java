package com.example.workbaidumap;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 启动页设计
 */
public class StartPageActivity extends AppCompatActivity {
    private Button main = null;
    private Button anim_test;
    private TextSwitcher title;
    private TextSwitcher subtitle;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            title.setText("锐志启航");
        }
    };

    private Runnable subtask = new Runnable() {
        @Override
        public void run() {
            subtitle.setText("万里商海与您一同启航");
        }
    };

    private Runnable nextPage = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        title = (TextSwitcher) findViewById(R.id.title);
        subtitle = (TextSwitcher) findViewById(R.id.subtitle);

        // 字体配置
        //从asset 读取字体
        AssetManager mgr = getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/STXINGKA.TTF");

        title.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView titletv = new TextView(StartPageActivity.this);
                titletv.setTextSize(50);
                titletv.setGravity(Gravity.CENTER);
                // 参数1：字体，参数2：字体粗细
                titletv.setTypeface(tf, Typeface.BOLD);
                titletv.setTextColor(Color.WHITE);
                return titletv;
            }
        });

        subtitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView subtitletv = new TextView(StartPageActivity.this);
                subtitletv.setTextSize(32);
                subtitletv.setGravity(Gravity.CENTER);
                subtitletv.setTypeface(tf);
                subtitletv.setTextColor(Color.WHITE);
                return subtitletv;
            }
        });

        delay();

        // 状态栏自动隐藏
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
    }

    /**
     * 延时设置
     */
    private void delay() {
        // 使用 handler 延时展示
        handler.postDelayed(task, 2000);
        handler.postDelayed(subtask, 2000);
        handler.postDelayed(nextPage, 6000);
    }
}

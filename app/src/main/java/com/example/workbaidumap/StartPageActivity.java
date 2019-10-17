package com.example.workbaidumap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 启动页设计
 */
public class StartPageActivity extends AppCompatActivity {
    Button main = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        main = (Button) findViewById(R.id.changeToMain);

        // 状态栏自动隐藏
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

        // 点击事件,启动主页面
        main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

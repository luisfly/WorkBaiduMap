package com.example.workbaidumap;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 添加 Web 商城展示用的活动
 * 调用 webview 访问链接
 * 现存在问题，公司商城网站不支持键盘弹出调整页面，而我访问淘宝是可以的
 */
public class WebShopActivity extends AppCompatActivity {
    // 连接商场用的webview控件
    private WebView webshop = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_shop);

        webshop = (WebView) findViewById(R.id.WebShop);
        webshop.setWebViewClient(new WebViewClient());
        webshop.getSettings().setJavaScriptEnabled(true);
        // webshop.loadUrl("http://wx.gdrzcd.com/");
        webshop.loadUrl("https://www.taobao.com/");

        /*WebSettings webSettings = webshop.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webshop.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://www.baidu.com");
                return true;
            }
        });*/
    }
}

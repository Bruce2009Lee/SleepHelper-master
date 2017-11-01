package com.example.developerhaoz.sleephelper.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ActivityTestMyWebViewMain extends Activity {

    private MyWebView wView;
    private Button btn_icon;
    ProgressBar mProgressBar;
    TextView webTitle ;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmywebview_main);

        webTitle = (TextView) findViewById(R.id.tv_webtitle_2);
        btn_icon = (Button) findViewById(R.id.my_button_2);
        wView = (MyWebView) findViewById(R.id.web_view_2);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_2);

        wView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = wView.getSettings();
        settings.setUseWideViewPort(true);  // 设定支持viewport
        settings.setLoadWithOverviewMode(true);   // 自适应屏幕
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);  // 设定支持缩放
        wView.setInitialScale(25);

        btn_icon.setVisibility(View.INVISIBLE);


        wView.loadUrl("http://www.jianshu.com/p/2cf0aca9a57d");
        wView.setWebViewClient(new WebViewClient() {
            // 在webview里打开新链接
            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString()); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }
        });

        wView.setWebChromeClient(new WebChromeClient() {
            // 这里设置获取到的网站title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(GONE);
                } else {
                    if (mProgressBar.getVisibility() == GONE) {
                        mProgressBar.setVisibility(VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        // 比如这里做一个简单的判断，当页面发生滚动，显示那个Button
        wView.setOnScrollChangedCallback(new MyWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy > 0) {
                    btn_icon.setVisibility(VISIBLE);
                } else {
                    btn_icon.setVisibility(GONE);
                }
            }
        });

        btn_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wView.setScrollY(0);
                btn_icon.setVisibility(GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (wView.canGoBack()) {
            wView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }
}

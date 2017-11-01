package com.example.developerhaoz.sleephelper.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ActivityTestWebViewMain extends Activity {


    private WebView webView;

    long exitTime = 0;

    TextView webTitle ;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testwebview_main);

        webView = (WebView) findViewById(R.id.web_view_1);
        webTitle = (TextView) findViewById(R.id.tv_webtitle);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);

        // 设置WebView属性,运行执行js脚本
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString()); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
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

        webView.loadUrl("http://www.jianshu.com/p/2cf0aca9a57d");
    }

    // 我们需要重写回退按钮的时间,当用户点击回退按钮时：
    // 1.webView.canGoBack()判断网页是否能后退，可以则goback()
    // 2.如果不可以连续点击两次退出App,否则弹出提示Toast
    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }


}

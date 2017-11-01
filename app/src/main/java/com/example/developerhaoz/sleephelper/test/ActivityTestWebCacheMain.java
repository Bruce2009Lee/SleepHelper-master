package com.example.developerhaoz.sleephelper.test;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;

/**
 * Created by Administrator on 2017/10/28.
 */

public class ActivityTestWebCacheMain extends AppCompatActivity {



    private static final String TAG = ActivityTestWebCacheMain.class.getSimpleName();

    private WebView mWebView;
    private Button btn_clear_cache;
    private Button btn_refresh;
    private ImageView img_error_back;

    private static final String APP_CACHE_DIRNAME = "/webviewcache";
    private static final String URL = "http://blog.csdn.net/comwill";

    IntentFilter mIntentFilter;
    NetworkChangeReceiver mNetworkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testwebcache_main);

        img_error_back =  (ImageView) findViewById(R.id.img_error_back);
        mWebView = (WebView) findViewById(R.id.mWebView);
        btn_clear_cache = (Button) findViewById(R.id.btn_clear_cache);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);

        mWebView.loadUrl(URL);
        img_error_back.setVisibility(View.GONE);

        mWebView.setWebViewClient(new WebViewClient() {
            // 设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "shouldOverrideUrlLoading url : " + request.getUrl().toString());
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
                img_error_back.setVisibility(View.VISIBLE);
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启database storage API功能
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i(TAG, "cacheDirPath : " + cacheDirPath);
        // 设置数据库缓存路径
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        Log.i(TAG, "DatabasePath : " + settings.getDatabasePath());

        btn_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.clearCache(true);
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_error_back.setVisibility(View.GONE);
                mWebView.reload();
            }
        });

        init();
    }
    private void init() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);
    }


    // 重写回退按钮的点击事件
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {

        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    /**
     * 接收系统广播：动态的监听网络变化
     */
    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

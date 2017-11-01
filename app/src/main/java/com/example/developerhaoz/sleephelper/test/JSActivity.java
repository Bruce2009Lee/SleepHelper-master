package com.example.developerhaoz.sleephelper.test;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;

/**
 * Created by Administrator on 2017/10/27.
 */

public class JSActivity extends AppCompatActivity {

    //assets下的文件的test.html所在的绝对路径
    private static final String DEFAULT_URL = "file:///android_asset/test.html";

    private EditText et_url;

    private WebView webView;

    private ProgressDialog progressDialog;//加载界面的菊花

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js);

        initView();
        initWebView();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        webView = (WebView) findViewById(R.id.webView);
        et_url = (EditText) findViewById(R.id.et_url);
    }

    /**
     * 初始化webview
     */
    private void initWebView() {

        //首先设置Webview支持JS代码
        webView.getSettings().setJavaScriptEnabled(true);

        //Webview自己处理超链接(Webview的监听器非常多，封装一个特殊的监听类来处理)
        webView.setWebViewClient(new WebViewClient() {

            /**
             * 当打开超链接的时候，回调的方法
             * WebView：自己本身webView
             * url：即将打开的url
             */
            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //自己处理新的url
                webView.loadUrl(request.getUrl().toString());
                //true就是自己处理
                return true;
            }

            //重写页面打开和结束的监听。添加友好，弹出菊花
            /**
             * 界面打开的回调
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //弹出菊花
                progressDialog = new ProgressDialog(JSActivity.this);
                progressDialog.setTitle("提示");
                progressDialog.setMessage("软软正在拼命加载……");
                progressDialog.show();

            }

            /**
             * 界面打开完毕的回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                //隐藏菊花:不为空，正在显示。才隐藏
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }

        });

        //设置进度条
        //WebChromeClient与webViewClient的区别
        //webViewClient处理偏界面的操作：打开新界面，界面打开，界面打开结束
        //WebChromeClient处理偏js的操作
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 进度改变的回调
             * WebView：就是本身
             * newProgress：即将要显示的进度
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.setMessage("软软正在拼命加载……" + newProgress + "%");
            }
            /**
             * 重写alert、confirm和prompt的回调
             */
            /**
             * Webview加载html中有alert()执行的时候，回调
             * url:当前Webview显示的url
             * message：alert的参数值
             * JsResult：java将结果回传到js中
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                AlertDialog.Builder builder = new AlertDialog.Builder(JSActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);//这个message就是alert传递过来的值
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //处理确定按钮了，且通过jsresult传递，告诉js点击的是确定按钮
                        result.confirm();
                    }
                });
                builder.show();
                //自己处理
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JSActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);//这个message就是alert传递过来的值
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //处理确定按钮了，且通过jsresult传递，告诉js点击的是确定按钮
                        result.confirm();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //处理取消按钮，且通过jsresult传递，告诉js点击的是取消按钮
                        result.cancel();

                    }
                });
                builder.show();
                //自己处理
                return true;
            }

            /**
             * defaultValue就是prompt的第二个参数值，输入框的默认值
             * JsPromptResult：向js回传数据
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      final JsPromptResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JSActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);//这个message就是alert传递过来的值
                //添加一个EditText
                final EditText editText = new EditText(JSActivity.this);
                editText.setText(defaultValue);//这个就是prompt 输入框的默认值
                //添加到对话框
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取edittext的新输入的值
                        String newValue = editText.getText().toString().trim();
                        //处理确定按钮了，且过jsresult传递，告诉js点击的是确定按钮(参数就是输入框新输入的值，我们需要回传到js中)
                        result.confirm(newValue);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //处理取消按钮，且过jsresult传递，告诉js点击的是取消按钮
                        result.cancel();

                    }
                });
                builder.show();
                //自己处理
                return true;
            }
        });

        //java与js回调，自定义方法
        //1.java调用js
        //2.js调用java
        //首先java暴露接口，供js调用
        /**
         * obj:暴露的要调用的对象
         * interfaceName：对象的映射名称 ,object的对象名，在js中可以直接调用
         * 在html的js中：JSTest.showToast(msg)
         * 可以直接访问JSTest，这是因为JSTest挂载到js的window对象下了
         */
        webView.addJavascriptInterface(new Object() {
            //定义要调用的方法，注意4.2及以后的则多了注释语句@JavascriptInterface
            //msg由js调用的时候传递
            @JavascriptInterface
            public void showToast(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }, "JSTest");

    }

    public void onClick(View view){
        String url = et_url.getText().toString().trim();
        if(TextUtils.isEmpty(url)){
            url = DEFAULT_URL;
        }
        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            //返回上一个页
            webView.goBack();
            return ;
        }
        super.onBackPressed();
    }

}

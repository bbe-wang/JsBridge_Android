package com.lyk.jsbridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.lyk.jsbridge.modle.User;

public class MainActivity2 extends AppCompatActivity {

    private BridgeWebView mWebView;

    ValueCallback<Uri> mUploadMessage;

    int RESULT_CODE = 0;

    private static final String TAG = "MainActivity";
    private Button btnSnedToJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mWebView = (BridgeWebView) findViewById(R.id.webView);
        initWebView();
    }

    private void initWebView() {
        // 设置具体WebViewClient
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack
        mWebView.setDefaultHandler(new myHadlerCallBack());
        // setWebChromeClient
        mWebView.loadUrl("file:///android_asset/js.html");
        //以下代码是android 发送消息给js
        btnSnedToJs=(Button)findViewById(R.id.button3);
        btnSnedToJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNative(v);
            }
        });
    }

    //android调js （button按钮）
    public void sendNative(View view) {
        //模拟用户信息 获取本地位置，用户名返回给html
        User user = new User();
        user.setLocation("上海");
        user.setName("Bruce");
        // 回调 "functionInJs"
        mWebView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

                Toast.makeText(MainActivity2.this, "网页在获取你的位置，"+ data, Toast.LENGTH_SHORT).show();

            }
        });
        mWebView.send("hello");
    }

    /*
   * 自定义的WebViewClient
   */
    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }



    /**
     * 自定义回调----------1:此回调可以接受html直接send（）过来的信息 是一个数组   可以通过id来做具体的判断
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if(function != null){
                Toast.makeText(MainActivity2.this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }




}

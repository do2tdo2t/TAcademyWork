package com.example.tacademy.webproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    WebView webView;
    WebSettings webSettings;
    EditText etURL ;
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button :
                    doAction1();
                    break;
                case R.id.button2 :
                    doAction2();
                    break;
                case R.id.button3 :
                    doAction3();
                    break;
                case R.id.button4 :
                    doAction4();
                    break;
                case R.id.button5 :
                    doAction5();
                    break;
            }
        }
    };
    void doAction5(){
       webView.reload();
    }
    void doAction4(){
        if(webView.canGoForward()){
            webView.goForward();
        }
    }
    void doAction3(){
        if(webView.canGoBack()){
            webView.goBack();
        }
    }
    void doAction2(){
        webView.stopLoading();
    }
    void doAction1(){
        webView.loadUrl(etURL.getText().toString());
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }
    //
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_BACK :
//                if(webView.canGoBack()){
//                    webView.goBack();
//                }else{
//                    finish();
//                }
//                return  true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(handler);
        findViewById(R.id.button2).setOnClickListener(handler);
        findViewById(R.id.button3).setOnClickListener(handler);
        findViewById(R.id.button4).setOnClickListener(handler);
        findViewById(R.id.button5).setOnClickListener(handler);


        etURL = (EditText)findViewById(R.id.editText);
        webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl("http://m.naver.com");
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
    }
    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}

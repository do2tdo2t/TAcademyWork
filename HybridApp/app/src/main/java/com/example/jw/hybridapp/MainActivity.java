package com.example.jw.hybridapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    TextView mTextView;
    EditText mEditText;
    Button mButton;
    String mStringHead;
    private final Handler handler = new Handler();

    /** Called when the activity is first created. */
    @Override
    @JavascriptInterface
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);
        mTextView = (TextView) findViewById(R.id.textview);
        mButton = (Button) findViewById(R.id.button);
        mEditText = (EditText) findViewById(R.id.EditText01);
        mStringHead = this.getString(R.string.string_head);




        // 자바스크립트 초기화 작업
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new AndroidBridge(), "HybridApp");

//		mWebView.setFocusable(true);
//		mWebView.setFocusableInTouchMode(true);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });



        mWebView.loadUrl("file:///android_asset/test.html");  // test.html 불러오기

        mWebView.setWebViewClient(new HelloWebViewClient());  // WebViewClient 설정

        mButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View view) {
                mWebView.loadUrl("javascript:setMessage('"+mEditText.getText()+"')");
            }
        });

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setMessage(final String arg) { // must be final
            handler.post(new Runnable() {
                public void run() {
                    Log.d("HybridApp", "setMessage(" + arg + ")");
                    mTextView.setText(mStringHead+arg);
                }
            });
        }
        @JavascriptInterface
        public void test(){
            Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private class HelloWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url.startsWith("http://"))
                return false;
            else {
                boolean override = false;
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                if( url.startsWith("sms:")){
                    Intent i = new Intent( Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(i);
                    return true;
                } else if( url.startsWith("tel:")){
                    Intent i = new Intent( Intent.ACTION_CALL, Uri.parse(url));
                    if (Build.VERSION.SDK_INT > 23 && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED ) {
                        return true;
                    }
                    startActivity(i);
                    return true;
                } else if( url.startsWith("mailto:")){
                    Intent i = new Intent( Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                try {
                    startActivity(intent);
                    override = true;
                }
                catch (ActivityNotFoundException e) {
                    return override;
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}

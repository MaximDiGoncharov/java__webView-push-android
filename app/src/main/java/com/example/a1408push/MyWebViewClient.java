package com.example.a1408push;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        view.evaluateJavascript("Object.keys(localStorage)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d("LocalStorageObjects", "Objects in localStorage: " + value);
            }
        });
    }
}
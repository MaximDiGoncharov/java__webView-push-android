package com.example.a1408push;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class _MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static String WebViewCookies;




    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView) findViewById(R.id.webView);
//        TextView myAwesomeTextView = (TextView)findViewById(R.id.myAwesomeTextView);
//        View copyButton = findViewById(R.id.copyButton);
//        copyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                copyTextToClipboard(myAwesomeTextView.getText().toString());
//            }
//        });
        myWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage() {
                return onConsoleMessage((ConsoleMessage) null);
            }

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Handler handler = new Handler();
                System.out.println(consoleMessage.message());
//                System.out.println("WebViewConsole"+ consoleMessage.message() + " line "
//                        + consoleMessage.lineNumber() + " of "
//                        + consoleMessage.sourceId());
                return true;
            }
        });

        CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true);
//        myWebView.loadUrl("https://universal.laxo.one/");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();

                    Log.d(TAG,token);
//                    myAwesomeTextView.setText(token);
                  myWebView.loadUrl("https://universal.laxo.one/enter/" + token );

                });
        FirebaseMessaging.getInstance().subscribeToTopic("web_app")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });


    }
    private void copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

}
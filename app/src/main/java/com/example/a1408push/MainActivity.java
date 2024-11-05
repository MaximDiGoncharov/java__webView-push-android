package com.example.a1408push;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.content.ClipboardManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ValueCallback<Uri[]> uploadMessage;
    private static final int FILECHOOSER_RESULTCODE = 1;

    static int IS_PUSH_LINK = 0;
    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    public static String WebViewCookies;
    private static final int PICK_CONTACT = 1;
    @SuppressLint("StaticFieldLeak")
    public static WebView webView;
    private ValueCallback<Uri[]> mFilePathCallback;

    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webView);

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        requestFilePermissions();


        // Проверяем версию Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        } else {
            // Для более старых версий не нужно запрашивать разрешение
//            sendNotification();
        }



        CookieManager.getInstance().setAcceptThirdPartyCookies(myWebView, true);
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
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Метод для Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessage = filePathCallback;

                // Создаем интент для выбора файла
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*"); // Можно указать конкретный тип файла, если нужно
                startActivityForResult(Intent.createChooser(intent, "Выберите файл"), FILECHOOSER_RESULTCODE);

                return true; // Возвращаем true, чтобы сообщить, что мы обработали запрос
            }

        });
//        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        myWebView.clearCache(true);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, token);
                    requestNotificationPermission();
                    myWebView.loadUrl("https://universal.laxo.one/enter/" + token);
//                                myWebView.loadUrl("https://universal.laxo.one/web/" + token);

//                    myWebView.loadUrl("javascript:" + "window.location.href = 'https://yandex.com/';");
                    //     myWebView.evaluateJavascript("window.location.href = 'https://yandex.com/';", null);


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

    private void requestFilePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }





// Запрос разрешения на уведомления
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (uploadMessage == null) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

            // Возвращаем результат в uploadMessage
            uploadMessage.onReceiveValue(result != null ? new Uri[]{result} : null);
            uploadMessage = null; // Обнуляем uploadMessage после обработки
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

package com.example.a1408push;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    public static String WebViewCookies;
    private static final int PICK_CONTACT = 1;
    private WebView webView;


    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        // Проверяем версию Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        } else {
            // Для более старых версий не нужно запрашивать разрешение
//            sendNotification();
        }
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
                return true;
            }
        });

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
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();

                    Log.d(TAG,token);
                    //myAwesomeTextView.setText(token);
                    requestNotificationPermission();
                    myWebView.loadUrl("https://universal.laxo.one/enter/" + token );
                    // myWebView.loadUrl("https://contact-picker.glitch.me/");
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
//    private void copyTextToClipboard(String text) {
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("Copied Text", text);
//        clipboard.setPrimaryClip(clip);
//    }
//    }
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

//    // Метод для открытия Contact Picker
//    public void pickContact() {
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(intent, PICK_CONTACT);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
//            Uri contactUri = data.getData();
//            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
//
//            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                cursor.close();
//
//                // Теперь вы можете передать имя контакта обратно в WebView или обработать его по своему усмотрению
//                webView.evaluateJavascript("javascript:handleContactName('" + contactName + "')", null);
//            }
//        }
//    }
//




}
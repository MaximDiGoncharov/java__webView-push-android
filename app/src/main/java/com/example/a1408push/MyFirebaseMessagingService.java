package com.example.a1408push;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Проверяем, если есть данные в уведомлении
        if (remoteMessage.getData().size() > 0) {
            // Получаем данные уведомления
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String link = remoteMessage.getData().get("link");

            // Если приложение на переднем плане, показываем уведомление вручную
            if (MyApplication.isAppInForeground()) {
                // Здесь можно использовать кастомное отображение уведомлений в UI
                showCustomNotification(title, body, link);  // Обработка уведомлений в приложении
            } else {
                // Если приложение на заднем плане, просто показываем стандартное уведомление
                showNotification(title, body);  // Показ уведомления в фоновом режиме
            }
        }
    }

    // Метод для отображения кастомного уведомления в активном приложении


    public void showCustomNotification(String title, String body, String link) {
        // Получаем NotificationManager для отображения уведомлений
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel";  // Укажите ваш канал уведомлений

        // Создаем канал уведомлений для Android 8.0 (API 26) и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Создаем Intent для открытия PushActivity при нажатии на уведомление
        Intent intent = new Intent(this, PushActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Обеспечивает открытие нового Activity из background



        // Если нужно, передайте данные в активность через Intent
        intent.putExtra("link", link); // Пример передачи данных

        // Создаем PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, // контекст
                0, // requestCode (можно оставить 0)
                intent, // Intent, который будет запущен при нажатии
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Создаем уведомление
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Установите свой иконку
                .setAutoCancel(true)  // Уведомление исчезает после нажатия
                .setContentIntent(pendingIntent)  // Устанавливаем PendingIntent
                .build();

        // Показываем уведомление
        notificationManager.notify(1, notification);  // ID уведомления = 1
    }

    // Метод для отображения стандартного уведомления, если приложение в фоновом режиме
    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel";

        // Для Android 8 и выше необходимо создать канал уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("123")
                .setContentText("456")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        notificationManager.notify(0, notification);
    }
}

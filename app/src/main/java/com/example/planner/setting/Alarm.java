package com.example.planner.setting;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.example.planner.weekly.CalendarUtils.formattedDate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.myday.MyDayEvent;
import com.example.planner.myday.MyDayEventList;

import java.time.LocalDate;
import java.util.ArrayList;

public class Alarm extends BroadcastReceiver {
    private final String DEFAULT = "DEFAULT";
    private String title = "오늘의 할일";
    private String content = "운동하기";

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));

        for(Object object : dailyEvents) {
//            content += dailyEvents.toString();
        }
        createNotificationChannel(context, DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);

        Intent alarmintent = new Intent(context, MainActivity.class);
        alarmintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        createNotification(context, DEFAULT, 1, title, content, alarmintent);
    }

    private void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    private void createNotification(Context context, String channelId, int id, String title, String text, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_background) // 알림 보여지는 아이콘. 반드시 필요
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent) // notificatin 클릭시 실행될 PendingIntent 지정
                .setAutoCancel(true) // 알람 클릭시 사라짐 (true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE); // 효과음, 진동 여부

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}


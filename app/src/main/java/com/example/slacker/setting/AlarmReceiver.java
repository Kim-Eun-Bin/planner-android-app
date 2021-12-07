package com.example.slacker.setting;

import static com.example.slacker.weekly.CalendarUtils.formattedDate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.slacker.MainActivity;
import com.example.slacker.R;
import com.example.slacker.myday.MyDayEvent;
import com.example.slacker.myday.MyDayEventList;

import java.time.LocalDate;
import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "channel_01";
    private final String channel_name = "myChannel01";
    private String title = "오늘의 할일";
    private String content = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));    //일정 리스트

        //오늘의 일정들을 스트링에 저장
        for (MyDayEvent event : dailyEvents) {
            if (event.getDate().equals(formattedDate(LocalDate.now()))) {
                content += event.getContent();
                content += "\r\n";
            }
        }

        createNotificationChannel(context, CHANNEL_ID, channel_name);

        Intent alarmIntent = new Intent(context, MainActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_mainicon_background) // 알림 보여지는 아이콘. 반드시 필요
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent) // notificatin 클릭시 실행될 PendingIntent 지정
                .setAutoCancel(true) // 알람 클릭시 사라짐 (true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE); // 효과음, 진동 여부

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID, String channel_name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
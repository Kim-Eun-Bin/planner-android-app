package com.example.couchpotatosplan.setting;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.app.AlarmManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.MainActivity;
import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.month.MonthEvent;
import com.example.couchpotatosplan.month.MonthEventList;
import com.example.couchpotatosplan.myday.MyDayEvent;
import com.example.couchpotatosplan.myday.MyDayEventList;
import com.example.couchpotatosplan.utils.Theme;
import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.WeeklyEventAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;


//설정화면..
public class SettingFragment extends Fragment {

    private Button theme_btn;
    private BottomNavigationView bottomNavigation;
    private AlarmManager alarmManager;
    private Switch alarm_switch;
    private DatabaseReference mDatabase;
    private Button custom_btn;
    private Boolean on_click = false;
    private String theme_num;
    private int hour = LocalTime.now().getHour();
    private int min = LocalTime.now().getMinute();
    private boolean isAlarmSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        theme_btn = view.findViewById(R.id.theme_btn);
        bottomNavigation = view.findViewById(R.id.bottomNavi);
        alarm_switch = view.findViewById(R.id.alarm_switch);
        custom_btn = view.findViewById(R.id.custom_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    theme_num = snapshot.child("theme").getValue().toString();
                    MainActivity.changeTheme(theme_num);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addEventAction();

        return view;
    }

    private void addEventAction() {
        theme_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
                dlg.setTitle("테마");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.changeTheme(String.valueOf(theme_num));
                        mDatabase.child("theme").setValue(theme_num);
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final String[] Theme = new String[]{"Basic", "Yellow", "Coral", "BabyPink", "Purple", "Midnight"};

                dlg.setSingleChoiceItems(Theme, Integer.parseInt(theme_num) - 1, new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (Theme[which]) {
                            case "Basic":
                                theme_num = "1";
                                break;
                            case "Yellow":
                                theme_num = "2";
                                break;
                            case "Coral":
                                theme_num = "3";
                                break;
                            case "BabyPink":
                                theme_num = "4";
                                break;
                            case "Purple":
                                theme_num = "5";
                                break;
                            case "Midnight":
                                theme_num = "6";
                                break;
                        }
                    }
                });
                dlg.show();
            }
        });

        alarm_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!on_click) {
                    custom_btn.setEnabled(true);
                    custom_btn.setTextColor(Color.BLACK);
                    on_click = true;
                } else {
                    custom_btn.setEnabled(false);
                    custom_btn.setTextColor(Color.GRAY);
                    on_click = false;
                    if(isAlarmSet) {
                        destroyNotification();
                        unregist();
                    }
                    isAlarmSet = false;
                }
            }
        });

        custom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener, hour, min, false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setTitle("알람 시간 설정");
                dialog.show();
            }
        });

    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isAlarmSet = true;
            hour = hourOfDay;
            min = minute;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent intent = new Intent(getContext(), Alarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    };

    public void unregist() {
        Intent intent = new Intent(getContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    void destroyNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }
}
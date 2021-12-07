package com.example.slacker.setting;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.AlarmManager;
import android.widget.Toast;

import com.example.slacker.login.UserLoginActivity;
import com.example.slacker.utils.Alarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.slacker.MainActivity;
import com.example.slacker.R;
import com.example.slacker.utils.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.Calendar;


//설정화면..
public class SettingFragment extends Fragment {

    private Button theme_btn;
    private BottomNavigationView bottomNavigation;
    private AlarmManager alarmManager;
    private Switch alarm_switch;
    private Button sign_out_btn;
    private TextView name_tv;
    private TextView email_tv;
    private Alarm alarm;
    private TimePickerDialog dialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button custom_btn;
    private String theme_num = "0";
    private boolean isAlarmSet = false;
    private View bar;
    private Intent intent;
    private Drawable drawable_basic;
    private Drawable drawable_yellow;
    private Drawable drawable_coral;
    private Drawable drawable_pink;
    private Drawable drawable_purple;
    private Drawable drawable_midnight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        theme_btn = view.findViewById(R.id.theme_btn);
        sign_out_btn = view.findViewById(R.id.signout_btn);
        bottomNavigation = view.findViewById(R.id.bottomNavi);
        alarm_switch = view.findViewById(R.id.alarm_switch);
        custom_btn = view.findViewById(R.id.custom_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bar = view.findViewById(R.id.view4);
        name_tv = view.findViewById(R.id.setting_name_tv);
        email_tv = view.findViewById(R.id.setting_email_tv);

        drawable_basic = getResources().getDrawable(R.drawable.switch_track_selector);
        drawable_yellow = getResources().getDrawable(R.drawable.switch_track_selector_yellow);
        drawable_coral = getResources().getDrawable(R.drawable.switch_track_selector_coral);
        drawable_pink = getResources().getDrawable(R.drawable.switch_track_selector_pink);
        drawable_purple = getResources().getDrawable(R.drawable.switch_track_selector_purple);
        drawable_midnight = getResources().getDrawable(R.drawable.switch_track_selector_midnight);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    User user = snapshot.child("user").child(currentUser.getUid()).getValue(User.class);
                    if (user != null) {
                        String name = user.getName() + "님";
                        name_tv.setText(name);
                        email_tv.setText(user.getEmail());
                    }
                    if (snapshot.child(currentUser.getUid()).child("theme").getValue() != null) {
                        theme_num = snapshot.child(currentUser.getUid()).child("theme").getValue().toString();
                    }
                    alarm = snapshot.child(currentUser.getUid()).child("alarm").getValue(Alarm.class);
                    if (alarm != null) {
                        if (alarm.isChecked()) {
                            alarm_switch.setChecked(true);
                            custom_btn.setEnabled(true);
                            custom_btn.setTextColor(Color.BLACK);
                        } else {
                            alarm_switch.setChecked(false);
                            custom_btn.setEnabled(false);
                            custom_btn.setTextColor(Color.GRAY);
                        }
                    }

                    int theme = Integer.parseInt(theme_num);
                    switch (theme) {
                        case 0:
                            bar.setBackgroundColor(Color.rgb(143, 186, 216));
                            sign_out_btn.setBackgroundColor(Color.rgb(143, 186, 216));
                            alarm_switch.setTrackDrawable(drawable_basic);
                            break;
                        case 1:
                            bar.setBackgroundColor(Color.rgb(255, 196, 0));
                            sign_out_btn.setBackgroundColor(Color.rgb(255, 196, 0));
                            alarm_switch.setTrackDrawable(drawable_yellow);
                            break;
                        case 2:
                            bar.setBackgroundColor(Color.rgb(214, 92, 107));
                            sign_out_btn.setBackgroundColor(Color.rgb(214, 92, 107));
                            alarm_switch.setTrackDrawable(drawable_coral);
                            break;
                        case 3:
                            bar.setBackgroundColor(Color.rgb(201, 117, 127));
                            sign_out_btn.setBackgroundColor(Color.rgb(201, 117, 127));
                            alarm_switch.setTrackDrawable(drawable_pink);
                            break;
                        case 4:
                            bar.setBackgroundColor(Color.rgb(97, 95, 133));
                            sign_out_btn.setBackgroundColor(Color.rgb(97, 95, 133));
                            alarm_switch.setTrackDrawable(drawable_purple);
                            break;
                        case 5:
                            bar.setBackgroundColor(Color.rgb(48, 52, 63));
                            sign_out_btn.setBackgroundColor(Color.rgb(48, 52, 63));
                            alarm_switch.setTrackDrawable(drawable_midnight);
                            break;
                    }
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

        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), UserLoginActivity.class);
                startActivity(intent);
            }
        });

        theme_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
                dlg.setTitle("테마");

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.changeTheme(theme_num);
                        mDatabase.child(currentUser.getUid()).child("theme").setValue(theme_num);
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final String[] Theme = new String[]{"Basic", "Yellow", "Coral", "BabyPink", "Purple", "Midnight"};

                dlg.setSingleChoiceItems(Theme, Integer.parseInt(theme_num), new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (Theme[which]) {
                            case "Basic":
                                theme_num = "0";
                                break;
                            case "Yellow":
                                theme_num = "1";
                                break;
                            case "Coral":
                                theme_num = "2";
                                break;
                            case "BabyPink":
                                theme_num = "3";
                                break;
                            case "Purple":
                                theme_num = "4";
                                break;
                            case "Midnight":
                                theme_num = "5";
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
                if (alarm_switch.isChecked()) {
                    alarm = new Alarm(LocalTime.now().getHour(), LocalTime.now().getMinute(), true);
                    custom_btn.setEnabled(true);
                    custom_btn.setTextColor(Color.BLACK);
                    mDatabase.child(currentUser.getUid()).child("alarm").setValue(alarm);
                } else {
                    custom_btn.setEnabled(false);
                    custom_btn.setTextColor(Color.GRAY);
                    mDatabase.child(currentUser.getUid()).child("alarm").child("checked").setValue(false);
//                    if (isAlarmSet) {
//                        destroyNotification();
//                        unregist();
//                    }
                    isAlarmSet = false;
                }
            }
        });

        custom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener, alarm.getHour(), alarm.getMinute(), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setTitle("알람 시간 설정");
                dialog.show();
            }
        });

    }

    private void createNotificationChannel(Context context, String CHANNEL_ID, String channel_name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isAlarmSet = true;
            alarm = new Alarm(hourOfDay, minute, alarm_switch.isChecked());
            mDatabase.child(currentUser.getUid()).child("alarm").setValue(alarm);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

            intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            String alert = "알람이 " + hourOfDay + "시 " + minute + "분으로 설정되었습니다.";
            Toast.makeText(getContext(), alert, Toast.LENGTH_LONG).show();
        }
    };


    public void unregist() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    void destroyNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

}
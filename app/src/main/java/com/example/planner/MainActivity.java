package com.example.couchpotatosplan;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.couchpotatosplan.month.MonthFragment;
import com.example.couchpotatosplan.myday.MyDayFragment;
import com.example.couchpotatosplan.setting.SettingFragment;
import com.example.couchpotatosplan.weekly.WeeklyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new WeeklyFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.bottom_weekly:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new WeeklyFragment()).commit();
                        break;
                    case R.id.bottom_myday:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyDayFragment()).commit();
                        break;
                    case R.id.bottom_month:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
                        break;
                    case R.id.bottom_set:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    public static void changeTheme(String theme_num){
        int theme = Integer.parseInt(theme_num);
        switch (theme) {
            case 1:
                bottomNavigationView.setBackgroundColor(Color.rgb(143,186,216));
                break;
            case 2:
                bottomNavigationView.setBackgroundColor(Color.rgb(255,211,26));
                break;
            case 3:
                bottomNavigationView.setBackgroundColor(Color.rgb(234,102,118));
                break;
            case 4:
                bottomNavigationView.setBackgroundColor(Color.rgb(248,213,224));
                break;
            case 5:
                bottomNavigationView.setBackgroundColor(Color.rgb(102,100,139));
                break;
            case 6:
                bottomNavigationView.setBackgroundColor(Color.rgb(48,52,63));
                break;
        }
    }
}

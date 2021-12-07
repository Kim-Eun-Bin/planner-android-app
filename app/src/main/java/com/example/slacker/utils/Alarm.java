package com.example.slacker.utils;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Alarm {
    private int hour;
    private int minute;
    private boolean checked;

    public Alarm() {

    }

    public Alarm(int hour, int minute, boolean checked) {
        this.hour = hour;
        this.minute = minute;
        this.checked = checked;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

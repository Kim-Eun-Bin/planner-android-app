package com.example.planner.myday;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyDayEvent {
    private Long id;
    private String date;
    private int time;
    private String content;
    private boolean checked;

    public MyDayEvent() {

    }

    public MyDayEvent(Long id, String date, int time, String content, boolean checked)
    {
        this.id = id;
        this.date = date;
        this.time = time;
        this.content = content;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

package com.example.couchpotatosplan.month;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MonthEvent {
    private Long id;
    private int start;
    private int end;
    private String content;

    public MonthEvent() {

    }

    public MonthEvent(Long id, int start, int end, String content)
    {
        this.id = id;
        this.start = start;
        this.end = end;
        this.content = content;
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


    public int getStartTime()
    {
        return start;
    }

    public void setStartTime(int time)
    {
        this.start = time;
    }

    public int getEndTime()
    {
        return end;
    }

    public void setEndTime(int time)
    {
        this.end = time;
    }

}
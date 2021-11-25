package com.example.planner.month;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planner.R;

import java.util.List;

public class MonthEventAdapter extends ArrayAdapter<MonthEvent> {
    private TextView excludeCon;
    private TextView start_tv;
    private TextView end_tv;

    public MonthEventAdapter(@NonNull Context context, List<MonthEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MonthEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.month_exclude_cell, parent, false);

        excludeCon = view.findViewById(R.id.excludeContent);
        start_tv = view.findViewById(R.id.time_start);
        end_tv = view.findViewById(R.id.time_end);

        String content = event.getContent();

        String start;
        if(event.getStart_hour() < 10) //00:00
            start = "0"+ event.getStart_hour() + ":" + event.getStart_min();
        else //12:00
            start = event.getStart_hour() + ":" + event.getStart_min();

        String end;
        if(event.getEnd_hour() < 10)
            end = "0"+ event.getEnd_hour() + ":" + event.getEnd_min();
        else
            end = event.getEnd_hour() + ":" + event.getEnd_min();

        excludeCon.setText(content);
        start_tv.setText(start);
        end_tv.setText(end);

        return view;
    }

}

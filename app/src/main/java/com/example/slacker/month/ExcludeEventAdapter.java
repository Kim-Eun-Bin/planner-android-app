package com.example.slacker.month;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.slacker.R;

import java.util.List;

public class ExcludeEventAdapter extends ArrayAdapter<ExcludeEvent> {
    private TextView excludeCon;
    private TextView start_tv;
    private TextView end_tv;

    public ExcludeEventAdapter(@NonNull Context context, List<ExcludeEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        ExcludeEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.month_exclude_cell, parent, false);

        excludeCon = view.findViewById(R.id.excludeContent);
        start_tv = view.findViewById(R.id.time_start);
        end_tv = view.findViewById(R.id.time_end);

        String content = event.getContent();

        String start;
        if(event.getStart_hour() < 10) //00:00
            if(event.getStart_min() < 10)
                start = "0"+ event.getStart_hour() + ":0" + event.getStart_min();
            else
                start = "0"+ event.getStart_hour() + ":" + event.getStart_min();
        else //12:00
            if(event.getStart_min() < 10)
                start = event.getStart_hour() + ":0" + event.getStart_min();
            else
                start = event.getStart_hour() + ":" + event.getStart_min();

        String end;
        if(event.getEnd_hour() < 10)
            if(event.getEnd_min() < 10)
                end = "0"+ event.getEnd_hour() + ":0" + event.getEnd_min();
            else
                end = "0"+ event.getEnd_hour() + ":" + event.getEnd_min();
        else
        if(event.getEnd_min() < 10)
            end = event.getEnd_hour() + ":0" + event.getEnd_min();
        else
            end = event.getEnd_hour() + ":" + event.getEnd_min();

        excludeCon.setText(content);
        start_tv.setText(start);
        end_tv.setText(end);

        return view;
    }

}
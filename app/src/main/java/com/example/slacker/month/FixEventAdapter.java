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

public class FixEventAdapter extends ArrayAdapter<FixEvent> {
    private TextView fixCon;
    private TextView start_tv;
    private TextView end_tv;

    public FixEventAdapter(@NonNull Context context, List<FixEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        FixEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.month_fix_cell, parent, false);

        fixCon = view.findViewById(R.id.fixContent);
        start_tv = view.findViewById(R.id.fix_time_start);
        end_tv = view.findViewById(R.id.fix_time_end);

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

        fixCon.setText(content);
        start_tv.setText(start);
        end_tv.setText(end);

        return view;
    }

}
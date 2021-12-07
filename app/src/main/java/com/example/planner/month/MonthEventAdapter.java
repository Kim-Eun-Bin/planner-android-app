package com.example.planner.month;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planner.R;
import com.example.planner.myday.MyDayEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MonthEventAdapter extends ArrayAdapter<MonthEvent> {
    private DatabaseReference mDatabase;

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.exclude_cell, parent, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView excludeCon = view.findViewById(R.id.excludeContent);
        TextView start = view.findViewById(R.id.time_start);
        TextView end = view.findViewById(R.id.time_end);


        String content = event.getContent();

        String starting;
        if(event.getStartTime() < 10) //00:00
            starting = "0"+ event.getStartTime() + ":00";
        else //12:00
            starting = event.getStartTime() + ":00";

        String ending;
        if(event.getStartTime() < 10)
            ending = "0"+ event.getStartTime() + ":00";
        else
            ending = event.getStartTime() + ":00";

        excludeCon.setText(content);
        start.setText(starting);
        end.setText((ending));

        return view;
    }

}

package com.example.planner.myday;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyDayEventAdapter extends ArrayAdapter<MyDayEvent> {
    private DatabaseReference mDatabase;
    public TextView time_tv;
    public TextView content_tv;
    public CheckBox checkBox;

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public MyDayEventAdapter(@NonNull Context context, List<MyDayEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MyDayEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.myday_event_cell, parent, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        time_tv = view.findViewById(R.id.time_tv);
        content_tv = view.findViewById(R.id.content_tv);
        checkBox = view.findViewById(R.id.checkBox);

        String time = event.getTime() + ":00";
        String content = event.getContent();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("event").child(String.valueOf(event.getId())).child("checked").setValue(checkBox.isChecked());
            }
        });

        time_tv.setText(time);
        content_tv.setText(content);
        checkBox.setChecked(event.isChecked());

        if(checkBox.isChecked()) {
            content_tv.setPaintFlags(content_tv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            content_tv.setPaintFlags(0);
        }

        return view;
    }
}

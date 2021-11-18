package com.example.couchpotatosplan.myday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.couchpotatosplan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyDayEventAdapter extends ArrayAdapter<MyDayEvent> {
    private DatabaseReference mDatabase;

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

        TextView time_tv = view.findViewById(R.id.time_tv);
        TextView content_tv = view.findViewById(R.id.content_tv);
        CheckBox check = view.findViewById(R.id.checkBox);

        String time = event.getTime() + ":00";
        String content = event.getContent();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("event").child(String.valueOf(event.getId())).child("checked").setValue(check.isChecked());
            }
        });

        time_tv.setText(time);
        content_tv.setText(content);
        check.setChecked(event.isChecked());

        return view;
    }

}

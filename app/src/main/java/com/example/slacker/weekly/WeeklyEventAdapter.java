package com.example.slacker.weekly;

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

import com.example.slacker.R;
import com.example.slacker.myday.MyDayEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WeeklyEventAdapter extends ArrayAdapter<MyDayEvent> {
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private CheckBox check;
    private TextView time_tv;
    private TextView content_tv;
    private String theme;

    public WeeklyEventAdapter(@NonNull Context context, List<MyDayEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        MyDayEvent event = getItem(position);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        time_tv = view.findViewById(R.id.time_tv);
        content_tv = view.findViewById(R.id.content_tv);
        check = view.findViewById(R.id.checkBox);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    theme = snapshot.child(currentUser.getUid()).child("theme").getValue().toString();
                    setTheme(theme);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(event.getId())).child("checked").setValue(check.isChecked());
            }
        });

        String time = event.getTime() + ":00";
        String content = event.getContent();

        time_tv.setText(time);
        content_tv.setText(content);
        check.setChecked(event.isChecked());

        if(check.isChecked()) {
            content_tv.setPaintFlags(content_tv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            content_tv.setPaintFlags(0);
        }

        return view;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
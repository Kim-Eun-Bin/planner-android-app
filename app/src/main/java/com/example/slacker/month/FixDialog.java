package com.example.slacker.month;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.slacker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

public class FixDialog extends DialogFragment {
    private EditText eventNameET;
    private TextView start_tv;
    private TextView end_tv;
    private Button save_btn;
    private Button cancel_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TimePickerDialog dialog;
    private long postNum;
    private int start_time_hour = -1;
    private int start_time_min = -1;
    private int end_time_hour = -1;
    private int end_time_min = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("fix").getChildren()) {
                        FixEvent post = dataSnapshot.getValue(FixEvent.class);
                        if (post != null) {
                            postNum = post.getId();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.month_fix_dialog, container, false);

        save_btn = (Button) view.findViewById(R.id.save_btn);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);

        initWidgets(view);
        EventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.fix_eventNameET);
        start_tv = view.findViewById(R.id.fix_starttime);
        end_tv = view.findViewById(R.id.fix_endtime);
    }

    public void EventAction(View view)
    {
        start_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, startTimelistener, LocalTime.now().getHour(), LocalTime.now().getMinute(), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
        end_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TimePickerDialog(getContext(),  android.R.style.Theme_Holo_Light_Dialog_NoActionBar, endTimelistener, LocalTime.now().getHour(), LocalTime.now().getMinute(), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                if(eventName.equals("")) {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                } else if(start_time_hour == -1) {
                    Toast.makeText(getContext(), "시작 시간을 설정하세요", Toast.LENGTH_LONG).show();
                } else if(end_time_hour == -1) {
                    Toast.makeText(getContext(), "종료 시간을 설정하세요", Toast.LENGTH_LONG).show();
                } else {
                    writeNewEvent(start_time_hour, start_time_min, end_time_hour, end_time_min, eventName);
                }
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void writeNewEvent(int start_hour, int start_min, int end_hour, int end_min, String content) {
        FixEvent event = new FixEvent(postNum+1, start_hour, start_min, end_hour, end_min, content);
        mDatabase.child(currentUser.getUid()).child("fix").child(String.valueOf(postNum+1)).setValue(event);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFixFragment()).commit();
    }

    private TimePickerDialog.OnTimeSetListener startTimelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            start_time_hour = hourOfDay;
            start_time_min = minute;
            if(hourOfDay < 10) {
                String start = "0" + hourOfDay + ":" + minute;
                start_tv.setText(start);
            } else {
                String start = hourOfDay + ":" + minute;
                start_tv.setText(start);
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            end_time_hour = hourOfDay;
            end_time_min = minute;
            if(hourOfDay < 10) {
                String end = "0" + hourOfDay + ":" + minute;
                end_tv.setText(end);
            } else {
                String end = hourOfDay + ":" + minute;
                end_tv.setText(end);
            }
        }
    };
}
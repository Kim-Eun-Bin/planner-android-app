package com.example.planner.weekly;

import static com.example.planner.weekly.CalendarUtils.formattedDate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.planner.R;
import com.example.planner.myday.MyDayEvent;
import com.example.planner.myday.MyDayFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;

public class WeeklyFragmentDialog extends DialogFragment {

    private EditText eventNameET;
    private Button save_btn;
    private DatabaseReference mDatabase;
    private long postNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    postNum = (snapshot.child("event").getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.myday_dialog, container, false);
        save_btn = (Button) view.findViewById(R.id.save_btn);

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                if(!eventName.equals("")) {
                    writeNewEvent(formattedDate(CalendarUtils.selectedDate), RandomNum(), eventName, false);
                } else {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    public void writeNewEvent(String date, int time, String content, boolean checked) {
        MyDayEvent event = new MyDayEvent(postNum+1, date, time, content, checked);
        mDatabase.child("event").child(String.valueOf(postNum+1)).setValue(event);
    }

    public int RandomNum() {
        Random random = new Random();
        int time = random.nextInt(24) + 1;

        return time;
    }
}

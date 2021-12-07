package com.example.planner.month;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Random;

public class ExcludeDialog extends DialogFragment {

    private EditText eventNameET;
    private EditText startET;
    private EditText endET;
    private Button save_btn;
    private Button cancel_btn;
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
                    postNum = (snapshot.child("exclude").getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.exclude_dialog, container, false);

        save_btn = (Button) view.findViewById(R.id.save_btn);
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventNameET);
        startET = view.findViewById(R.id.starttime);
        endET = view.findViewById(R.id.endtime);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                int starttime = Integer.parseInt(startET.getText().toString());
                int endtime = Integer.parseInt(endET.getText().toString());
                if(!eventName.equals("")) {
                    writeNewEvent(starttime, endtime, eventName);
                } else {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    public void writeNewEvent(int start, int end, String content) {
        MonthEvent event = new MonthEvent(postNum+1, start, end, content);
        mDatabase.child("exclude").child(String.valueOf(postNum+1)).setValue(event);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
    }
}
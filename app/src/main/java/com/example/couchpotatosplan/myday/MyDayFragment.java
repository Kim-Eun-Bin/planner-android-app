package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.MainActivity;
import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.month.MonthEvent;
import com.example.couchpotatosplan.month.MonthEventList;
import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.WeeklyEventAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class MyDayFragment extends Fragment {


    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat;
    private ListView eventListView;
    private MyDayEventAdapter adapter;
    private ImageButton add_btn;
    private DatabaseReference mDatabase;

    private FragmentDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myday_fragment, container, false);

        TextView mTextView = view.findViewById(R.id.todayDate);
        add_btn = (ImageButton) view.findViewById(R.id.add_btn);
        eventListView = view.findViewById(R.id.eventListView);

        mFormat = new SimpleDateFormat("yyyy.MM.dd");

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setEventAdpater();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mTextView.setText(getTime());
        addEventAction();

        setEventAdpater();

        return view;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    private void addEventAction() {
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new FragmentDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    public void setEventAdpater()
    {
        try {
            ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));
            adapter = new MyDayEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            eventListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO
        }
    }
}
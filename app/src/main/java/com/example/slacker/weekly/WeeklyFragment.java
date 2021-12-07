package com.example.slacker.weekly;

import static com.example.slacker.weekly.CalendarUtils.daysInWeekArray;
import static com.example.slacker.weekly.CalendarUtils.formattedDate;
import static com.example.slacker.weekly.CalendarUtils.monthDayFromDate;
import static com.example.slacker.weekly.CalendarUtils.monthYearFromDate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slacker.MainActivity;
import com.example.slacker.R;
import com.example.slacker.month.ExcludeEvent;
import com.example.slacker.month.ExcludeEventList;
import com.example.slacker.month.FixEvent;
import com.example.slacker.month.FixEventList;
import com.example.slacker.myday.MyDayEventList;
import com.example.slacker.myday.MyDayEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class WeeklyFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private TextView monthDayText;
    private RecyclerView calendarRecyclerView;
    private ListView weeklyEventListView;
    private DatePickerDialog dialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private WeeklyEventAdapter weeklyEventAdapter;
    public long postNumOfWeekly;
    public long postNumOfExclude;
    public long postNumOfFix;
    private View view;
    public ImageButton add_btn;
    private WeeklyFragmentDialog fragmentDialog;
    private Drawable drawable_basic;
    private Drawable drawable_yellow;
    private Drawable drawable_coral;
    private Drawable drawable_pink;
    private Drawable drawable_purple;
    private Drawable drawable_midnight;

    Button previous_btn;
    Button next_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weekly_fragment, container, false);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        monthDayText = view.findViewById(R.id.monthDayTV);
        weeklyEventListView = view.findViewById(R.id.weeklyEventListView);
        add_btn = (ImageButton) view.findViewById(R.id.weekly_add_btn);

        drawable_basic = getResources().getDrawable(R.drawable.ic_baseline_add_circle_basic);
        drawable_yellow = getResources().getDrawable(R.drawable.ic_baseline_add_circle_yellow);
        drawable_coral = getResources().getDrawable(R.drawable.ic_baseline_add_circle_coral);
        drawable_pink = getResources().getDrawable(R.drawable.ic_baseline_add_circle_pink);
        drawable_purple = getResources().getDrawable(R.drawable.ic_baseline_add_circle_purple);
        drawable_midnight = getResources().getDrawable(R.drawable.ic_baseline_add_circle_midnight);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        CalendarUtils.selectedDate = LocalDate.now();

        monthYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle, listener, CalendarUtils.selectedDate.getYear(), CalendarUtils.selectedDate.getMonth().getValue() - 1, CalendarUtils.selectedDate.getDayOfMonth());
                dialog.show();
            }
        });

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyDayEventList.eventsList.clear();
                ExcludeEventList.eventsList.clear();
                FixEventList.fixeventsList.clear();
                if (snapshot.exists()) {
                    if (snapshot.child(currentUser.getUid()).child("theme").getValue() != null) {
                        String theme_num = snapshot.child(currentUser.getUid()).child("theme").getValue().toString();
                        MainActivity.changeTheme(theme_num);
                        btnChangeColor(Integer.parseInt(theme_num));
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("event").getChildren()) {
                    MyDayEvent post = dataSnapshot.getValue(MyDayEvent.class);
                    MyDayEventList.eventsList.add(post);
                    if (post != null) {
                        postNumOfWeekly = post.getId();
                    }
                }

                for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("exclude").getChildren()) {
                    ExcludeEvent post = dataSnapshot.getValue(ExcludeEvent.class);
                    ExcludeEventList.eventsList.add(post);
                    if (post != null) {
                        postNumOfExclude = post.getId();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("fix").getChildren()) {
                    FixEvent post = dataSnapshot.getValue(FixEvent.class);
                    FixEventList.fixeventsList.add(post);
                    if (post != null) {
                        postNumOfFix = post.getId();
                    }
                }
                setEventAdpater();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        weeklyEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDayEvent item = (MyDayEvent) weeklyEventAdapter.getItem(position);
                mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(item.getId())).child("checked").setValue(!item.isChecked());
            }
        });
        weeklyEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDatePickerStyle);
                builder.setTitle("일정 삭제").setMessage("정말로 삭제하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyDayEvent item = (MyDayEvent) weeklyEventAdapter.getItem(position);
                        mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(item.getId())).removeValue();
                        weeklyEventAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDialog = new WeeklyFragmentDialog();
                fragmentDialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });


        previous_btn = (Button) view.findViewById(R.id.previous_btn);
        next_btn = (Button) view.findViewById(R.id.next_btn);

        previousWeekAction();
        nextWeekAction();
        setWeekView();

        return view;
    }

    private void btnChangeColor(int theme) {

        switch (theme) {
            case 0:
                next_btn.setTextColor(Color.rgb(143, 186, 216));
                previous_btn.setTextColor(Color.rgb(143, 186, 216));
                add_btn.setBackground(drawable_basic);
                break;
            case 1:
                next_btn.setTextColor(Color.rgb(255, 196, 0));
                previous_btn.setTextColor(Color.rgb(255, 196, 0));
                add_btn.setBackground(drawable_yellow);
                break;
            case 2:
                next_btn.setTextColor(Color.rgb(214, 92, 107));
                previous_btn.setTextColor(Color.rgb(214, 92, 107));
                add_btn.setBackground(drawable_coral);
                break;
            case 3:
                next_btn.setTextColor(Color.rgb(201, 117, 127));
                previous_btn.setTextColor(Color.rgb(201, 117, 127));
                add_btn.setBackgroundColor(Color.rgb(201, 117, 127));
                add_btn.setBackground(drawable_pink);
                break;
            case 4:
                next_btn.setTextColor(Color.rgb(97, 95, 133));
                previous_btn.setTextColor(Color.rgb(97, 95, 133));
                add_btn.setBackground(drawable_purple);
                break;
            case 5:
                next_btn.setTextColor(Color.rgb(48, 52, 63));
                previous_btn.setTextColor(Color.rgb(48, 52, 63));
                add_btn.setBackground(drawable_midnight);
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            CalendarUtils.selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
            setWeekView();
        }
    };

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        monthDayText.setText(monthDayFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    public void previousWeekAction() {
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
    }

    public void nextWeekAction() {
        next_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater() {
        try {
            ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(CalendarUtils.selectedDate));
            dailyEvents.sort(new comparator());
            weeklyEventAdapter = new WeeklyEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            weeklyEventListView.setAdapter(weeklyEventAdapter);
        } catch (Exception e) {
            //TODO
        }
    }

    class comparator implements Comparator<MyDayEvent> {
        @Override
        public int compare(MyDayEvent item1, MyDayEvent item2) {
            if (item1.getTime() > item2.getTime()) {
                return 1;
            } else if (item1.getTime() < item2.getTime()) {
                return -1;
            }
            return 0;
        }
    }
}
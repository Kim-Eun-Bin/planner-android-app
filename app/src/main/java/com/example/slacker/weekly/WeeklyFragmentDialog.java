package com.example.slacker.weekly;

import static com.example.slacker.weekly.CalendarUtils.formattedDate;

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

import com.example.slacker.R;
import com.example.slacker.month.ExcludeEvent;
import com.example.slacker.month.ExcludeEventList;
import com.example.slacker.month.FixEvent;
import com.example.slacker.month.FixEventList;
import com.example.slacker.myday.MyDayEvent;
import com.example.slacker.myday.MyDayEventList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class WeeklyFragmentDialog extends DialogFragment {

    private EditText eventNameET;
    private Button save_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;;
    private long postNum;

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
                if(snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.child(currentUser.getUid()).child("event").getChildren()) {
                        MyDayEvent post = dataSnapshot.getValue(MyDayEvent.class);
                        if (post != null) {
                            postNum = post.getId();
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.add_event_dialog, container, false);
        save_btn = (Button) view.findViewById(R.id.save_btn);

        initWidgets(view);
        saveEventAction(view);

        return view;
    }

    private void initWidgets(View view)
    {
        eventNameET = view.findViewById(R.id.eventContentET);
    }

    public void saveEventAction(View view)
    {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventNameET.getText().toString();
                int randomNum = RandomNum();

                if (!eventName.equals("")) {
                    boolean flag; // randomNumber check
                    ArrayList<MyDayEvent> myDayEvents = MyDayEventList.eventsForDate(formattedDate(LocalDate.now()));

                    while(true) {
                        flag = false;
                        // event list 확인
                        for (MyDayEvent item : myDayEvents) {
                            if (randomNum == item.getTime()) {
                                flag = true;
                                break;
                            }
                        }
                        // Exclude Time 범위 안에 랜덤 숫자가 들어있는지 확인
                        for (ExcludeEvent item : ExcludeEventList.eventsList) {
                            if (item.getStart_hour() <= randomNum && item.getEnd_hour() > randomNum) {
                                flag = true;
                                break;
                            }
                        }
                        // Fix Time 범위 안에 랜덤 숫자가 들어있는지 확인
                        for (FixEvent item : FixEventList.fixeventsList) {
                            if (item.getStart_hour() <= randomNum && item.getEnd_hour() > randomNum) {
                                flag = true;
                                break;
                            }
                        }

                        if(!flag) {
                            break;
                        } else {
                            randomNum = RandomNum();
                        }
                    }

                    // 최종 선택된 랜덤 숫자로 이벤트 생성
                    writeNewEvent(formattedDate(CalendarUtils.selectedDate), randomNum, eventName, false);
                } else {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    public void writeNewEvent(String date, int time, String content, boolean checked) {
        MyDayEvent event = new MyDayEvent(postNum+1, date, time, content, checked);
        mDatabase.child(currentUser.getUid()).child("event").child(String.valueOf(postNum+1)).setValue(event);
    }

    public int RandomNum() {
        Random random = new Random();
        int time = random.nextInt(24) + 1;

        return time;
    }
}

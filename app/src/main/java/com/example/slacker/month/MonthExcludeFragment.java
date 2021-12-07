package com.example.slacker.month;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.slacker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MonthExcludeFragment extends Fragment {
    private ImageButton add_btn;
    private ImageButton back_btn;
    private ListView eventListView;
    private ExcludeDialog dialog;
    private ExcludeEventAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_exclude_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        add_btn = view.findViewById((R.id.add_btn));
        eventListView = view.findViewById((R.id.monthEventListView));
        back_btn = view.findViewById((R.id.back_btn));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFragment()).commit();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ExcludeDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setEventAdpater();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setEventAdpater();

        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDatePickerStyle);
                builder.setTitle("일정 삭제").setMessage("정말로 삭제하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExcludeEvent item = (ExcludeEvent) adapter.getItem(position);
                        mDatabase.child(currentUser.getUid()).child("exclude").child(String.valueOf(item.getId())).removeValue();

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


        return view;
    }

    public void setEventAdpater()
    {
        try {
            ArrayList<ExcludeEvent> dailyEvents = ExcludeEventList.eventsList;
            adapter = new ExcludeEventAdapter(getActivity().getApplicationContext(), dailyEvents);
            eventListView.setAdapter(adapter);
        } catch (Exception e) {
            //TODO
        }
    }
}
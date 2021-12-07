package com.example.slacker.month;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.slacker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonthFragment extends Fragment {
    public TextView exclude_btn;
    public TextView fix_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String theme_num = "0";
    private View bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        exclude_btn = view.findViewById(R.id.exclude);
        fix_btn = view.findViewById(R.id.fix);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bar = view.findViewById(R.id.view3);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if(snapshot.child(currentUser.getUid()).child("theme").getValue() != null) {
                        theme_num = snapshot.child(currentUser.getUid()).child("theme").getValue().toString();
                    }
                    int theme = Integer.parseInt(theme_num);

                    switch (theme) {
                        case 0:
                            bar.setBackgroundColor(Color.rgb(143, 186, 216));
                            break;
                        case 1:
                            bar.setBackgroundColor(Color.rgb(255, 196, 0));
                            break;
                        case 2:
                            bar.setBackgroundColor(Color.rgb(214, 92, 107));
                            break;
                        case 3:
                            bar.setBackgroundColor(Color.rgb(201, 117, 127));
                            break;
                        case 4:
                            bar.setBackgroundColor(Color.rgb(97, 95, 133));
                            break;
                        case 5:
                            bar.setBackgroundColor(Color.rgb(48, 52, 63));
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        exclude_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthExcludeFragment()).commit();
            }
        });

        fix_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthFixFragment()).commit();
            }
        });

        return view;
    }
}
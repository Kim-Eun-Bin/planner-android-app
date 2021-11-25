package com.example.planner.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.planner.R;

import java.util.ArrayList;

public class MonthExcludeFragment extends Fragment {
    private ImageButton add_btn;
    private ListView eventListView;
    private ExcludeDialog dialog;
    private MonthEventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_exclude_fragment, container, false);

        add_btn = view.findViewById((R.id.weekly_add_btn));
        eventListView = view.findViewById((R.id.monthEventListView));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ExcludeDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        setEventAdpater();

        return view;
    }

    public void setEventAdpater()
    {
        ArrayList<MonthEvent> dailyEvents = MonthEventList.eventsList;
        adapter = new MonthEventAdapter(getActivity().getApplicationContext(), dailyEvents);
        eventListView.setAdapter(adapter);
    }
}

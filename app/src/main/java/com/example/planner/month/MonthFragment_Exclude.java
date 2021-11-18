package com.example.couchpotatosplan.month;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.FragmentDialog;
import com.example.couchpotatosplan.myday.MyDayEvent;
import com.example.couchpotatosplan.myday.MyDayEventAdapter;
import com.example.couchpotatosplan.myday.MyDayEventList;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthFragment_Exclude extends Fragment {

    private View view;
    private ImageButton add_btn;
    private ListView eventListView;
    private ExcludeDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_exclude, container, false);
        add_btn = view.findViewById((R.id.add_btn));
        eventListView = view.findViewById((R.id.eventListView));

        addEventAction();

        return view;
    }

    private void addEventAction() {
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ExcludeDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
    }



}
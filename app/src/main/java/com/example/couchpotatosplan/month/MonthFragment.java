package com.example.couchpotatosplan.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.R;

public class MonthFragment extends Fragment {
    public TextView exclude_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_fragment, container, false);

        exclude_btn = view.findViewById(R.id.exclude);

        exclude_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MonthExcludeFragment()).commit();
            }
        });

        return view;
    }
}
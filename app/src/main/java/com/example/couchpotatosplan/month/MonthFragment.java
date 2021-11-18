package com.example.couchpotatosplan.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.couchpotatosplan.R;


public class MonthFragment extends Fragment {

    private View view;
    private TextView Fix;
    private TextView Exclude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_fragment, container, false);

//        TextView Fix = view.findViewById(R.id.fix);
        TextView Exclude = view.findViewById((R.id.exclude));

        Exclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MonthFragment_Exclude exclude = new MonthFragment_Exclude();
                transaction.replace(R.id.framelayout, exclude);
                transaction.commit();
            }
        });

        return view;
    }
}
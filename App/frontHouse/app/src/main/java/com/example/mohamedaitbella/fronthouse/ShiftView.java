package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShiftView extends Fragment {

    TextView time, job, state;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shift_view, container, false);


        time = view.findViewById(R.id.Time);
        //time.setText(getIntent().getStringExtra("Time"));
        job = view.findViewById(R.id.Title);
        //job.setText(getIntent().getStringExtra("job"));
        state = view.findViewById(R.id.State);
        //state.setText(getIntent().getStringExtra("state"));

        return view;


    }

}

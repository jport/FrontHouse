package com.example.mohamedaitbella.fronthouse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private String[] am_shifts;
    private String[] pm_shifts;
    private String[] days;     // In case dates are passed
    private Context context;
    private String[] week = {"Monday","Tuesday","Wedsnesday","Thursday","Friday","Saturday","Sunday"};

    public Adapter(String[] shifts1, String[] shifts2, String[] days, Context context){
        am_shifts = shifts1;
        pm_shifts = shifts2;
        this.days = days;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int veiwType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        Log.d("Adapter", "returning viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.day_date.setText(week[i]);
        Log.d("Adapter", "week[i] = " + week[i]);
        // Assuming unscheduled shifts shall be saved as empty strings
        viewHolder.am_shift.setText(am_shifts[i]);
        viewHolder.pm_shift.setText(pm_shifts[i]);

        Log.d("Adapter", "finished binding viewholder");
    }

    @Override
    public int getItemCount() {
        return am_shifts.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView am, pm, am_shift, pm_shift, day_date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            am = itemView.findViewById(R.id.am_button);
            pm = itemView.findViewById(R.id.pm_button);
            am_shift = itemView.findViewById(R.id.am_shift);
            pm_shift = itemView.findViewById(R.id.pm_shift);
            day_date = itemView.findViewById(R.id.day);
        }
    }
}

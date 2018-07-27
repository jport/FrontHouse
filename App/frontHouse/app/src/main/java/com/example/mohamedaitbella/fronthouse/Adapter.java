package com.example.mohamedaitbella.fronthouse;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private String[] am_shifts;
    private String[] pm_shifts;
    private String[] days;     // In case dates are passed
    private Context context;
    private String[] week = {"Sunday", "Monday","Tuesday","Wedsnesday","Thursday","Friday","Saturday"};

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.day_date.setText(week[i] + ": " + days[i]);
        Log.d("Adapter", "week[i] = " + week[i]);
        // Assuming unscheduled shifts shall be saved as empty strings
        viewHolder.am_shift.setText(am_shifts[i]);
        viewHolder.pm_shift.setText(pm_shifts[i]);

        Log.d("Adapter", "finished binding viewholder");

        viewHolder.cell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked on: " + am_shifts[i]);

                Intent intent = new Intent(context, ShiftView.class);
                intent.putExtra("am_shifts", am_shifts[i]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return am_shifts.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView am, pm, am_shift, pm_shift, day_date;
        LinearLayout cell_layout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            am = itemView.findViewById(R.id.am_button);
            pm = itemView.findViewById(R.id.pm_button);
            am_shift = itemView.findViewById(R.id.am_shift);
            pm_shift = itemView.findViewById(R.id.pm_shift);
            day_date = itemView.findViewById(R.id.day);
            cell_layout = itemView.findViewById(R.id.shift_view_button);
        }
    }
}

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

import com.google.gson.Gson;

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
    private ArrayList<Shift>[] weekShifts;
    private Shift[] myShifts;

    public Adapter(String[] shifts1, String[] shifts2, String[] days, Context context, ArrayList<Shift>[] weekShifts, Shift[] myShifts){
        am_shifts = shifts1;
        pm_shifts = shifts2;
        this.days = days;
        this.context = context;
        this.weekShifts = weekShifts;
        this.myShifts = myShifts;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.day_date.setText(week[i] + ": " + days[i]);
        Log.d("Adapter", "week[i] = " + week[i]);
        // Assuming unscheduled shifts shall be saved as empty strings
        viewHolder.am_shift.setText(am_shifts[i]);
        viewHolder.pm_shift.setText(pm_shifts[i]);

        Log.d("Adapter", "finished binding viewholder");

        viewHolder.cell_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked on: " + am_shifts[viewHolder.getAdapterPosition()]);

                Intent intent = new Intent(context, ShiftView.class);
                intent.putExtra("State", "AM");
                intent.putExtra("MyShift", (am_shifts[viewHolder.getAdapterPosition()]== null)?
                        "":
                        am_shifts[viewHolder.getAdapterPosition()] + pm_shifts[viewHolder.getAdapterPosition()]);
                if(myShifts[viewHolder.getAdapterPosition()] != null) {
                    intent.putExtra("MyShiftID", myShifts[viewHolder.getAdapterPosition()].ScheduleID);
                    intent.putExtra("ShiftStatus", myShifts[viewHolder.getAdapterPosition()].ShiftStatus);
                }
                // Send all shifts for the day and have ShiftView control the ones to be displayed
                // by using Home.Time() and [String].equals()
                intent.putExtra("Others", new Gson().toJson(weekShifts[viewHolder.getAdapterPosition()]));
                context.startActivity(intent);
            }
        });
        viewHolder.cell_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked on: " + pm_shifts[viewHolder.getAdapterPosition()]);

                Intent intent = new Intent(context, ShiftView.class);
                intent.putExtra("State", "PM");
                intent.putExtra("MyShift", (pm_shifts[viewHolder.getAdapterPosition()] == null)?
                        "":
                        am_shifts[viewHolder.getAdapterPosition()] + pm_shifts[viewHolder.getAdapterPosition()]);

                if(myShifts[viewHolder.getAdapterPosition()] != null) {
                    intent.putExtra("MyShiftID", myShifts[viewHolder.getAdapterPosition()].ScheduleID);
                    intent.putExtra("ShiftStatus", myShifts[viewHolder.getAdapterPosition()].ShiftStatus);
                }
                // Send all shifts for the day and have ShiftView control the ones to be displayed
                // by using Home.Time() and [String].equals()
                intent.putExtra("Others", new Gson().toJson(weekShifts[viewHolder.getAdapterPosition()]));
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
        LinearLayout cell_layout1, cell_layout2;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            am = itemView.findViewById(R.id.am_button);
            pm = itemView.findViewById(R.id.pm_button);
            am_shift = itemView.findViewById(R.id.am_shift);
            pm_shift = itemView.findViewById(R.id.pm_shift);
            day_date = itemView.findViewById(R.id.day);
            cell_layout1 = itemView.findViewById(R.id.shift_view_button1);
            cell_layout2 = itemView.findViewById(R.id.shift_view_button2);
        }
    }
}

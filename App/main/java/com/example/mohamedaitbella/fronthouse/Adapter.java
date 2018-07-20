package com.example.mohamedaitbella.fronthouse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> am_shifts;
    private ArrayList<String> pm_shifts;
    private ArrayList<String> days;
    private Context context;

    public Adapter(ArrayList<String> shifts1, ArrayList<String> shifts2, ArrayList<String> days, Context context){
        am_shifts = shifts1;
        pm_shifts = shifts2;
        this.days = days;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int veiwType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
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

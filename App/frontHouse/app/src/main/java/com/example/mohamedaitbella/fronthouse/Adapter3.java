package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {

    Adapter3(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell3, parent, false);
        Adapter3.ViewHolder holder = new Adapter3.ViewHolder(view);
        Log.d("Adapter3", "returning viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

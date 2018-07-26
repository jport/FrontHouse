package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {

    Shift[] list;
    String state;

    Adapter3(String json, String state){

        Gson gson = new Gson();
        list = gson.fromJson(json, Shift[].class);
        this.state = state;
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

        viewHolder.state.setText(state);
        viewHolder.employee.setText(list[i].EmpFirstName + " " + list[i].EmpLastName);
        //viewHolder.job.setText(list[i].);
        Gson gson = new Gson();

        String shifts[] = {"", ""};
        try{
        Home.Time(new JSONObject(gson.toJson(list[i])), 0);
        }catch (Exception e){
            Log.d("Adapter3", e.getMessage());
        }
        viewHolder.shift.setText(state.equals("AM")? shifts[0] : shifts[1]);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView state, employee, shift, job;


        public ViewHolder(@NonNull View view) {
            super(view);
            state = view.findViewById(R.id.State);
            employee = view.findViewById(R.id.MyShift);
            shift = view.findViewById(R.id.Time);
            job = view.findViewById(R.id.Title);

        }
    }
}

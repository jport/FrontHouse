package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {

    Shift[] list;
    String state;

    Adapter3(Shift[] list, String state){

        this.list = list;
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

        String shifts[] = {"", ""};
        try{
            Gson gson = new Gson();
            Home.Time(new JSONObject(gson.toJson(list[i])), 0);
        }catch (Exception e){
            Log.d("Adapter3", e.getMessage());
        }
        viewHolder.shift.setText(state.equals("AM")? shifts[0] : shifts[1]);

        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            // Should open swap/pickup dialog box, with cancel button
            // Button determined by employee shift status
            // Send to firebase on completion (for pickup)
            public void onClick(View view) {
                // Do buttons. Link:
                // https://stackoverflow.com/questions/17622622/how-to-pass-data-from-a-fragment-to-a-dialogfragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView state, employee, shift, job;
        LinearLayout click;

        public ViewHolder(@NonNull View view) {
            super(view);
            state = view.findViewById(R.id.State);
            employee = view.findViewById(R.id.MyShift);
            shift = view.findViewById(R.id.Time);
            job = view.findViewById(R.id.Title);
            click = view.findViewById(R.id.other_shift);

        }
    }
}

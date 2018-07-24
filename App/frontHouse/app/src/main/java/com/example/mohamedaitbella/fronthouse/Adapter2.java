package com.example.mohamedaitbella.fronthouse;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.sql.Time;
import java.util.Calendar;

import java.util.HashSet;

import javax.xml.validation.Validator;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder>{


    String[] days = {"Monday", "Tuesday", "Wedsnesday", "Thusday", "Friday", "Saturday", "Sunday"};
    String[] am_shifts = new String[7], pm_shifts = new String[7];
    HashSet<Integer> hash = new HashSet<>();
    Button submit;


    public Adapter2(JSONArray json, Button b){

        submit = b;

        JSONArray data = null;
        Log.d("GET_AVAIL-", json.toString());
        try {
            data = json.getJSONObject(0).getJSONArray("days");
        }catch(Exception e){
            Log.d("BLAH", e.getMessage());
        }

        for(int i = 0; i < data.length(); i++){
            try {
                Log.d("Debug", "Start " + data.getJSONObject(i).getString("StartTime"));
            }catch(Exception e){
                Log.d("GET_AVAIL", e.getMessage());
            }
            try {
                am_shifts[i] = data.getJSONObject(i).getString("StartTime").substring(11);
                Log.d("AvailShift1", am_shifts[i]);

                pm_shifts[i] = data.getJSONObject(i).getString("EndTime").substring(11);
                Log.d("AvailShift2", pm_shifts[i]);
            }
            catch(Exception e){
                Log.d("AvailabilityPayload", e.getMessage());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int veiwtype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell2, parent, false);
        Adapter2.ViewHolder holder = new Adapter2.ViewHolder(view);
        Log.d("Adapter2", "returning viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.day.setText(days[i]);

        Log.d("OnBind", "sub1, sub2: " + am_shifts[i] + " " + pm_shifts[i]);
        viewHolder.am.setText(am_shifts[i]);
        viewHolder.pm.setText(pm_shifts[i]);

        viewHolder.setup();
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        EditText am, pm;
        LinearLayout shift1, shift2;
        int start = 0;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.title_Day);
            shift1 = itemView.findViewById(R.id.shift1);
            shift2 = itemView.findViewById(R.id.shift2);
            am = shift1.findViewById(R.id.am);
            pm = shift2.findViewById(R.id.pm);
        }

        void setup(){

            am.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("ENABLE_BUTTON", "HERE");
                    Log.d("ENABLE_BUTTON", "itemview = " + itemView.toString());
                    if(!am.getText().equals("") && !submit.isEnabled()){
                        submit.setEnabled(true);
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    if(!am.getText().toString().equals("") && !hash.contains(getAdapterPosition()))
                        hash.add(getAdapterPosition());
                    if(am.getText().toString().equals(""))
                        hash.remove(getAdapterPosition());
                    if(am.getText().toString().equals("") && hash.isEmpty())
                        submit.setEnabled(false);
                }
            });

            am.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if(!hasFocus){
                        if(!validate(am.getText().toString())) {
                            am.setText(am_shifts[getAdapterPosition()]);
                            am.setError("Incorrect format: (12:00-11:59)\nPlease try again");
                        }
                        else {
                            Log.d("BeforeSet", am_shifts[getAdapterPosition()]);
                            am_shifts[getAdapterPosition()] = am.getText().toString();
                            Log.d("AfterSet", am_shifts[getAdapterPosition()]);
                        }
                    }
                }
            });

            pm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("ENABLE_BUTTON", "HERE");
                    if(!pm.getText().equals("") && !submit.isEnabled()){
                       submit.setEnabled(true);
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {

                    if(!pm.getText().toString().equals("") && !hash.contains(getAdapterPosition()+getItemCount()))
                        hash.add(getAdapterPosition()+getItemCount());
                    if(pm.getText().toString().equals(""))
                        hash.remove(getAdapterPosition()+getItemCount());
                    if(pm.getText().toString().equals("") && hash.isEmpty()){
                        submit.setEnabled(false);
                    }
                }
            });

            pm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if(!hasFocus){
                        if(!validate(pm.getText().toString())) {
                            pm.setText(pm_shifts[getAdapterPosition()]);
                            pm.setError("Incorrect format: (12:00-11:59)\nPlease try again");

                        }
                        else {
                            pm_shifts[getAdapterPosition()] = pm.getText().toString();
                            Log.d("ShouldSetB", "GOT HERE");
                        }
                    }
                }
            });
        }

        // Validate time inputs
        boolean validate(String time){

            if(time.equals("")) return true;
            if (time.length() < 3 || time.charAt(time.length()-1) == '-') return false;

            boolean hyphen = false;
            int mid = 0;

            for(int i = 0; i < time.length(); i++){

                if(time.charAt(i) == '-' && !hyphen){
                    hyphen = true;
                    mid = i;
                }
                else if(time.charAt(i) == '-' && hyphen)
                    return false;

            }

            if(!hyphen) return false;

            String sub1 = time.substring(0,mid), sub2 = time.substring(mid+1);

            Calendar cal = Calendar.getInstance();

            Log.d("TIME1", "sub1 = " + sub1);
            Log.d("TIME2", "sub2 = " + sub2);

            return checkTime(sub1) && checkTime(sub2);
        }

        // Checking times
        boolean checkTime(String time){

            if(time.charAt(0) == ':' || time.length() < 4) return false;

            int colon= 0;

            for(int i = 0; i < time.length(); i++)
                if(time.charAt(i) == ':')
                    colon = i;

            String hours, minutes;

            hours = (time.charAt(0) == 0)?time.substring(1,colon) : time.substring(0, colon);
            minutes = time.substring(colon+1);

            if(minutes.length() != 2 || hours.length()>2)
                return false;
            if(hours.compareTo("12") > 0 || hours.compareTo("1")<0)
                return false;
            if(minutes.compareTo("59") > 0 || hours.compareTo("00")<0)
                return false;

            return true;
        }
    }
}
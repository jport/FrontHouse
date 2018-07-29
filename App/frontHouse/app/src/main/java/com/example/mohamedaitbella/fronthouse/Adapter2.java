package com.example.mohamedaitbella.fronthouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.support.constraint.Constraints.TAG;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder>{


    String[] days = {"Monday", "Tuesday", "Wedsnesday", "Thusday", "Friday", "Saturday", "Sunday"};
    String[] am_shifts = new String[7], pm_shifts = new String[7];
    HashSet<Integer> hash = new HashSet<>();
    Button submit;
    SharedPreferences share;
    Context context;
    String url="http://knightfinder.com/WEBAPI/SetAvailability.aspx";

    //public Adapter2(JSONArray json, Button b){
    public Adapter2(JSONArray json, Button b, Context context){

        this.context= context;

        submit = b;

        JSONArray data = null;
        try {
            data = json.getJSONObject(0).getJSONArray("days");
        }catch(Exception e){
            Log.d("BLAH", e.getMessage());
        }


        for(int i = 0; i < data.length(); i++){

            String temp1, temp2;

            try {
                Log.d("Debug", "Start " + (temp1 = data.getJSONObject(i).getString("StartTime")));
                Log.d("Debug", "End " + (temp2 = data.getJSONObject(i).getString("EndTime")));
            }catch(Exception e){
                Log.d("GET_AVAIL2", e.getMessage());
            }


            String[] shifts = {};
            try {
                // For now, use '0' for Schedule and '1' for MyAvailability
                shifts = Home.Time(data.getJSONObject(i), 1);
            }catch (Exception e){
                Log.d("TIME", e.getMessage());
            }

            am_shifts[i] = shifts[0];
            Log.d("AvailShift1", am_shifts[i]);

            pm_shifts[i] = shifts[1];
            Log.d("AvailShift2", pm_shifts[i]);
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
        viewHolder.am.setText(am_shifts[i]);
        viewHolder.pm.setText(pm_shifts[i]);
        // Adding required listeners to am and pm
        viewHolder.setup();

        Log.d("OnBind", "sub1, sub2: " + am_shifts[i] + " " + pm_shifts[i]);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: submit listner entered");
                String payLoad;

                payLoad= getPayLoad(days, am_shifts,pm_shifts);

                APICall apicall = new APICall();
                apicall.execute(url,payLoad);
            }
        });
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
            // Assigning fields
            day = itemView.findViewById(R.id.title_Day);
            shift1 = itemView.findViewById(R.id.shift1);
            shift2 = itemView.findViewById(R.id.shift2);
            am = shift1.findViewById(R.id.am);
            pm = shift2.findViewById(R.id.pm);
        }

        // Adding required listeners to am and pm
        void setup(){

            am.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                }
                @Override
                // While typing new entry keep Submit disabled
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("ENABLE_BUTTON", "HERE");
                    if(!validate(am.getText().toString()))
                        submit.setEnabled(false);
                    else submit.setEnabled(true);
                }
                @Override
                // Keeping hash updated and turning off Submit if all fields empty
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
                    // Send error if not in right format, enable Submit otherwise
                    if(!hasFocus){
                        if(!validate(am.getText().toString())) {
                            am.setText(am_shifts[getAdapterPosition()]);
                            am.setError("Incorrect format: (00:00-12:00)\nPlease try again");
                        }
                        else {
                            am_shifts[getAdapterPosition()] = am.getText().toString();
                            submit.setEnabled(true);
                        }
                    }
                }
            });

            pm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                }
                @Override
                // While typing new entry keep Submit disabled
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("ENABLE_BUTTON", "HERE");
                    if(!validate(pm.getText().toString()))
                        submit.setEnabled(false);
                    else submit.setEnabled(true);
                }
                @Override
                // Keeping hash updated and turning off Submit if all fields empty
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
                        // Send error if not in right format, enable Submit otherwise
                        if(!validate(pm.getText().toString())) {
                            pm.setText(pm_shifts[getAdapterPosition()]);
                            pm.setError("Incorrect format: (12:00-23:59)\nPlease try again");
                        }
                        else {
                            pm_shifts[getAdapterPosition()] = pm.getText().toString();
                            submit.setEnabled(true);
                        }
                    }
                }
            });
        }

        // Validate time inputs
        boolean validate(String time){

            if(time.equals("")) return true;
            if (time.length() < 5 || time.charAt(time.length()-1) == '-') return false;

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

            String sub1 = time.substring(0,mid).trim(), sub2 = time.substring(mid+1).trim();


            Log.d("TIME1", "sub1 = " + sub1 + " length: " + sub1.length() + "; CHECK = " + checkTime(sub1));
            Log.d("TIME2", "sub2 = " + sub2 + " length: " + sub2.length() + "; CHECK = " + checkTime(sub2));

            //Log.d("TIME", "12 -> 23: " + "12".compareTo("23"));

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
            if(minutes.compareTo("59") > 0 || hours.compareTo("00")<0)
                return false;
            // If ~(10 < x < 23) & ~(00 < x < 09)
            if(!(hours.compareTo("10") >= 0 && hours.compareTo("23")<=0) && !(hours.compareTo("00") >= 0 && hours.compareTo("09") <= 0))
                return false;

            return true;
        }
    }

    private String getPayLoad(String[] days, String[] am_shifts, String[] pm_shifts){
        String load;
        StringBuilder sb = new StringBuilder();
        share = context.getApplicationContext().getSharedPreferences(Home.pref, 0);

        sb.append("{EmployeeID:\"");
        sb.append(share.getInt("EmployeeID",-1));
        sb.append("\", \"days\":[");

        for(int i=0; i<days.length; i++){
            String hardcoded = "1900-01-01T";
            String counter = Integer.toString(i);
            if(am_shifts[i]== null && pm_shifts[i] == null ){
                continue;
            }
            sb.append("{");
            sb.append("\"Day\":");
            sb.append(counter);
            sb.append(",");
            sb.append("\"StartTime\":");
            sb.append("\""+hardcoded);
            if(am_shifts[i].equals("")){
                sb.append(pm_shifts[i].substring(0,5));
            }else{
                sb.append(am_shifts[i].substring(0,5));
            }
            sb.append(":00\"");
            sb.append(",");
            sb.append("\"EndTime\":");
            sb.append("\""+hardcoded);
            if(pm_shifts[i].equals("")){
                sb.append(am_shifts[i].substring(6,1));
            }else{
                sb.append(pm_shifts[i].substring(6,11));
            }
            sb.append(":00\"");
            sb.append("}");
            if(i<days.length-1) {
                sb.append(",");
            }
        }

        sb.append("]}");

        load = sb.toString();

        return load;
    }
}
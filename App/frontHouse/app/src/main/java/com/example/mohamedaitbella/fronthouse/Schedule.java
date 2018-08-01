package com.example.mohamedaitbella.fronthouse;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Schedule extends Fragment {

    String url = "http://knightfinder.com/WEBAPI/GetSchedule.aspx";
    SharedPreferences share;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Schedule", "Received Notification");
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.schedule, container, false);
        Log.d("Activity2", "Started");

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        share = getContext().getApplicationContext().getSharedPreferences(Home.pref, 0);

        Home.startLoading();

        String payload = "{StoreID : \""+ share.getInt("StoreID", 0) +"\"}";

        APICall apicall = new APICall();
        JSONObject result = new JSONObject();
        try {
            result = apicall.execute(url, payload).get().getJSONObject(0);
            Log.d("SCHEDULE", result.toString());
        }catch(Exception e){
            Log.d("Schedule", e.getMessage());

        }

        Home.stopLoading();

        Gson gson = new Gson();

        Shift[] json = gson.fromJson(result.toString(), GetSchedule.class).schedules;

        // Sort all shifts by day
        Log.d("JSON", Arrays.toString(json));
        Arrays.sort(json);
        Log.d("JSON", Arrays.toString(json));

        ArrayList<Shift>[] weekShifts = new ArrayList[7];
        for(int i = 0; i < weekShifts.length; i++)
            weekShifts[i] = new ArrayList<Shift>();
        Shift[] mine = new Shift[7];


        int myStart = -1, date = 0;

        Calendar cal = Calendar.getInstance();
        if(json.length > 0){

            cal.set(Integer.parseInt(json[0].StartTime.substring(0,4))+0,
                    Integer.parseInt(json[0].StartTime.substring(5,7)) -1,
                    Integer.parseInt(json[0].StartTime.substring(8,10) )+0);

            // Get first SHIFTS for that week
            // ------------------- CHANGE LATER!! ----------------------------
            /*-------------*/  int start = cal.get(Calendar.DAY_OF_WEEK)-1;
            // ---------------------------------------------------------------
            Log.d("CAL", "start = " + start);

            // Calendar to keep track of the date the first day of the weekShifts
            date = cal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_WEEK)+1;
            Log.d("CAL", "date = " + date);


            // Sort shifts into days  and store the current employee's shifts(in order)
            weekShifts[start].add(json[0]);
            if(json[0].EmployeeID == share.getInt("EmployeeID", -1)) {
                myStart = -1;
                mine[0] = json[0];
            }

            for (int i = 1, j = start; i < json.length; i++) {

                Log.d("LOOP", "i " + i + ", j " + j);

                Log.d("LOOP", "i-1: " + json[i - 1].StartTime +
                        ", i: " + json[i].StartTime);
                Log.d("LOOP", "Saved Employee: " + share.getInt("EmployeeID", -1) + ", currentID: " + json[i].EmployeeID);

                // Increase 'j' if starting a new day
                if (!json[i - 1].StartTime.substring(0, 10).equals(json[i].StartTime.substring(0, 10))) {
                    // --------------- Have to change to -------------------------- //
                    // --------------- Should be increasing by differnce in days ---//
                    /*------------*/
                    j++;             //-------------------------//
                    // --------------------------------------------------------------//
                    Log.d("LOOP", "'j' hit");
                }

                // To be deleted after completion of task
                if (j == 7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Still haven't restricted the sending of schedules to one week");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                }
                if (json[i].EmployeeID == share.getInt("EmployeeID", 0)) {
                    mine[j] = json[i];
                    try {
                        String shifts[];

                        // For now, use '0' for Schedule and '1' for MyAvailability
                        shifts = Home.Time(new JSONObject(gson.toJson(mine[j])), 0);
                        Log.d("MINE1", shifts[0]);
                        Log.d("MINE2", shifts[1]);
                    } catch (Exception e) {
                        Log.d("SCHEDULE_catch", e.getMessage());
                    }
                    if (myStart == -1)
                        myStart = j;
                }

                weekShifts[j].add(json[i]);
            }
        }

        String am_shifts[] = new String[7];
        String pm_shifts[] = new String[7];
        String days[] = new String[7];

        // If Employee doesn't have a schedule yet
        if(myStart == -1){
            Log.d("NO_SHIFTS", "Came in");
            //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = Home.fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,
                    new MyAvailability()).commit();
            Log.d("NO_SHIFTS", "Before Commit");
            Toast.makeText(getActivity(), "NO SHIFTS SCHEDULED YET", Toast.LENGTH_LONG).show();
            Home.navigationView.setCheckedItem(R.id.nav_availability);
        }

        Log.d("TESTY", Arrays.toString(mine));
        Log.d("TESTY", "Max date: " + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //--------------- Add work shifts to respective arrays -----------------------------------
        for(int i = 0; i < mine.length; i++){

            if(myStart < 0)
                break;
            // Change to 'mine[start]' later
            if(mine[myStart] != null)
                days[i] = mine[myStart].StartTime.substring(5,7)+"/"+ (
                        ((date+i)%cal.getActualMaximum(Calendar.DAY_OF_MONTH) == 0)?
                                cal.getActualMaximum(Calendar.DAY_OF_MONTH) : (date+i)%cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            if(mine[i] == null) continue;
            try {
                String shifts[];

                // For now, use '0' for Schedule and '1' for MyAvailability
                shifts = Home.Time( new JSONObject(gson.toJson(mine[i]) ), 0);
                am_shifts[i] = shifts[0];
                pm_shifts[i] = shifts[1];
            }catch (Exception e){
                Log.d("SCHEDULE_catch", e.getMessage());
            }
        }
        //--------------------------------------------------------------------


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        // 'weekShifts' holds all shifts for week, placed into right days
        Adapter adapter = new Adapter(am_shifts, pm_shifts, days, getContext(), weekShifts, mine);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Activity2", "Recyclerview. List size = "+ am_shifts.length);



        return view;

    }

    private void fakeNews(String[] am_shifts, String[] pm_shifts){

        Log.d("fakeNews", "Started");
        for(int i = 0; i < 7; i++){
            am_shifts[i] = "9:30 am";
            pm_shifts[i] = "3:30 pm";

        }
        Log.d("fakeNews", "Finished");
    }

}


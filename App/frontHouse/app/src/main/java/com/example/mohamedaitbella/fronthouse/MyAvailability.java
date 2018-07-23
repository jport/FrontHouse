package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.json.JSONArray;

import java.util.ArrayList;

public class MyAvailability extends Fragment {

    Button submit;
    RecyclerView recyclerView;
    Adapter2 adapter;

    @Override
    public void onPause(){


        if(adapter == null)
            Log.d("WTF", "WHAT THE FUCK!!!!");
        else
            Log.d("WTF", "WE GOOD - NOT EQUAL TO NULL->"+ recyclerView.hasFocus());

        adapter.hasObservers();
        if(adapter.dialog == null)
            Log.d("WTF", "WHAT THE FUCK(2)!!!!");
        else
            Log.d("WTF", "WE GOOD - (DIALOGUE)NOT EQUAL TO NULL");
        super.onPause();

        if(adapter.hash.size() == 0)
            Log.d("WTF", "WHAT THE FUCK(3)!!!!");
        else
            Log.d("WTF", "WE GOOD - (HASH)NOT EQUAL TO NULL");
        super.onPause();

        /*
        if(adapter.dialog != null && adapter.dialog.isShowing())
            adapter.dialog.dismiss();
        */

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_availability, container, false);

        // Hide keyboard on switch
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        submit = view.findViewById(R.id.submit);

        submit.setEnabled(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("submitAvail", "Got here");
            }
        });

        // -----------------------------------------------------------------------
        String url = "http://knightfinder.com/WEBAPI/GetAvailability.aspx";

        APICall apicall = new APICall();
        JSONArray json = null;

        try {
            json = apicall.execute(url, "{EmployeeID:\"3\"}").get();
            Log.d("GET_AVAIL-", "Result: " + json.toString());
        }
        catch (Exception e){
            Log.d("GET AVAILABILITY", e.getMessage());
        }

        recyclerView = view.findViewById(R.id.recyclerview2);

        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("WTF", "WHAT?!");

                if(adapter.dialog.isShowing())
                    adapter.dialog.dismiss();
            }
        });

        adapter = new Adapter2(json);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d("Activity2", "Recyclerview");

        return view;
    }


}

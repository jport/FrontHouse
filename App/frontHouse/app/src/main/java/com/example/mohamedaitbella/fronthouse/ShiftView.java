package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
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
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONObject;

// Receives current Employee's shift and other employees of that day
// as json in extras of intent
public class ShiftView extends AppCompatActivity {

    TextView time, state, name;
    RecyclerView recyclerView;
    Adapter3 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_view);

        time = findViewById(R.id.Time);
        state = findViewById(R.id.State);
        name = findViewById(R.id.MyShift);

        // Deserialize your shift
        Shift yours = new Gson().fromJson(getIntent().getStringExtra("MyShift"), Shift.class);

        // Deserialize everyone else's shift
        Shift[] temp = new Gson().fromJson(getIntent().getStringExtra("OtherShifts"), Shift[].class), everyone = new Shift[temp.length-1];


        name.setText(yours.EmpFirstName + " " + yours.EmpLastName);
        state.setText(getIntent().getStringExtra("State"));

        try {
            String[] shifts = Home.Time(new JSONObject(getIntent().getStringExtra("MyShift")),0);
            time.setText( state.equals("AM")? shifts[0] : shifts[1] );
        }catch (Exception e){
            Log.d("ShiftView", e.getMessage());
        }

        for(int i = 0, j = 0; i < temp.length; i++) {
            if (temp[i].EmployeeID == yours.EmployeeID)
                j++;
            everyone[j++] = temp[i];
        }

        recyclerView = findViewById(R.id.recyclerview3);
        adapter = new Adapter3(everyone, state.getText().toString());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        time.setText(getIntent().getStringExtra("Time"));
        state = findViewById(R.id.State);
        state.setText(getIntent().getStringExtra("state"));

        FirebaseMessaging.getInstance().subscribeToTopic("Bernardin");

        getIncomingIntentAndSet();


    }

    private void getIncomingIntentAndSet(){

        Intent intent = getIntent();
        String str = intent.getStringExtra("am_shifts");

        if(str=="am_shifts"){
            TextView Time = findViewById(R.id.Time);
            Time.setText(str);
        }
    }

}

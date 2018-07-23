package com.example.mohamedaitbella.fronthouse;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    EditText userName, password, to, page;
    Button login, clear;
    ProgressBar load;

    @Override
    protected void onStart() {
        super.onStart();

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ) {
                    Log.d("LISTENER","Here");
                    return login.callOnClick();
                }
                return false;
            }
        });

    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseMessaging.getInstance().subscribeToTopic("Bernardin");


        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences(Home.pref, 0).edit().remove("userId").commit();
                Log.d("Dialog", "Pasted commit");
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                builder.setMessage("Current User has been removed.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        userName=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.button);
        load = findViewById(R.id.load);



    }

    public void clickMe(View view ){

        view.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOAD", "Got here");
                load.setVisibility(View.VISIBLE);
            }
        });

        Context context = getApplicationContext();

        Home.authen(userName.getText().toString(), password.getText().toString(), context, Login.this, load);

        Log.d("MyLogin", "After authen()");

        SharedPreferences share = context.getSharedPreferences(Home.pref, 0);

        if(share.contains("userId")) Log.d("Login", "got it.");
        else Log.d("Login", "DAMN");

        if(share.getInt("userId", -1) > 0) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Incorrect user/password. Please try again.");
            AlertDialog dialog = builder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    getCurrentFocus().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LOAD", "Got here");
                            load.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
            dialog.show();

        }
    }

}

class APICall extends AsyncTask<String, String, JSONArray> {

    public APICall(){

    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override       //https://stackoverflow.com/questions/2938502/sending-post-data-in-android
    protected JSONArray doInBackground(String... params){

        String urlString = params[0];   // URL being called
        String data = params[1];    // Data to post

        Log.d("INPUT CHECK", "URL:" + urlString + ", post: " + data);

        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);     // Allows input to url site
            urlConnection.setDoOutput(true);    // Allows output from url site
            urlConnection.setRequestProperty("Content-Type", "application/json");   //Self explanatory
            urlConnection.setRequestProperty("Accept", "application/json");         //"               "
            urlConnection.setRequestMethod("POST");                                 //"               "
            Log.i("message","hello world");
            //---------------- JSON Post Sequence -------------------------------------------
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data);
            wr.flush();
            //-------------------------------------------------------------------------------
            JSONArray json = new JSONArray();
            StringBuilder sb = new StringBuilder();
            int HttpResult = urlConnection.getResponseCode();   // Self explanatory

            wr.close();
            if(HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while((line = br.readLine()) != null)   //Should run once for login
                    json.put(new JSONObject(line));
                    //sb.append(line + "\n");
                br.close();
            }

            Log.i("traffic", "here");
            // Returning what is wanted from executing of the AsyncTask
            return json;
        }catch (Exception e){
            Log.d("CHECK CALL", e.getMessage());
        }

        return null;
    }
    protected void onPostExecute(JSONArray result){
        if(result != null)
            Log.d("MyDebug", result.toString());
        super.onPostExecute(result);
    }
}

class Send extends AsyncTask<String, String, Boolean>{

    final private String key = "key=AAAAjmnq9XU:APA91bF4uWqQchNv6IkWiGyn93uwUzXjh3PaKNoYxEkAO5o9GG5I2v2f9CB7aPl7g4v2NtX5uJ4Hm397pPT5rvWIXDalow7tvEJa_lpusuXwXAIaIRToxFfJuQ6_6gCwZijxuVaHAkSgwmRFe1XX8JokYNM2sILuHQ";

    Send(){}

    static public void sending(String to, String page, String token){

        Send send = new Send();
        String url = "https://fcm.googleapis.com/fcm/send";

        String payload =
                "{\"to\":\"/topics/" +to+
                        "\",\"notification\": {\"title\": \"This is the title\",\"text\": \"Did you make it?!\",\"click_action\": \""
                        +page+ "\"}}";

        send.execute(url, payload , token );
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params){

        String urlString = params[0];   // URL being called
        String data = params[1];    // Data to post
        Log.d("INPUT CHECK", "URL:" + urlString + ", post: " + data + "key: " + key );

        try{

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);     // Allows input to url site
            urlConnection.setDoOutput(true);    // Allows output from url site
            urlConnection.setRequestProperty("Content-Type", "application/json");   //Self explanatory
            urlConnection.setRequestProperty("Accept", "application/json");         //"               "
            urlConnection.setRequestProperty("Authorization", key);
            urlConnection.setRequestMethod("POST");                                 //"               "
            Log.i("message","hello world");
            //---------------- JSON Post Sequence -------------------------------------------
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            Log.i("traffic", "here");
            wr.write(data);
            wr.flush();
            //-------------------------------------------------------------------------------
            int HttpResult = urlConnection.getResponseCode();   // Self explanatory

            wr.close();
            if(HttpResult == HttpURLConnection.HTTP_OK)
                return true;
            else
                return false;
        }catch (Exception e){
            Log.d("CHECK CALL", e.getMessage());
        }
        return false;
    }

    protected void onPostExecute(Boolean result){
        if(result != null)
            Log.d("MyDebug", result.toString());
        super.onPostExecute(result);
    }
}
package com.example.mohamedaitbella.fronthouse;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {
    EditText userName, password, to, page;
    Button button, sending;

    String token;

    @Override
    protected void onStart() {
        super.onStart();
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ) {
                    Log.d("LISTENER","Here");
                    return button.callOnClick();
                }
                return false;
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("Official");

        setContentView(R.layout.activity_main);
        userName=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.password);
        button=(Button)findViewById(R.id.button);
        sending = (Button)findViewById(R.id.send);
        to = (EditText)findViewById(R.id.to);
        page = (EditText)findViewById(R.id.page);

        final Task task =
                (FirebaseInstanceId.getInstance().getInstanceId());

        final Activity main = this;
        task.addOnSuccessListener(main, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                InstanceIdResult result = (InstanceIdResult)task.getResult();
                token = result.getToken();
                Log.d("GET_ID", "" +token );
            }
        });
    }

    public void clickMe(View view ){
        Log.d("CLICKME", "Started login");
         String url = "http://knightfinder.com/WEBAPI/Login.aspx";

         APICall apiCall = new APICall();
         JSONObject result;

         try {
              result = apiCall.execute(url, "{login:\""+userName.getText().toString()+"\",password:\""+password.getText().toString()+"\"}").get().getJSONObject(0);
              Log.d("CHECK", "Result = " + result);
         }catch (Exception e){
              Log.d("Debug: API_Call", e.getMessage());
              return;
         }

         int userId = -1;

         try { userId = result.getInt("EmployeeID"); }
         catch (Exception e){
              Log.d("Debug: Get Emp ID", e.getMessage());
              return;
         }

         if(userId > 0) {
              Intent intent = new Intent(MainActivity.this, Main2Activity.class);
              intent.putExtra("userId", userId);
              startActivity(intent);
         }
         else{
              AlertDialog.Builder builder = new AlertDialog.Builder(this);

              builder.setMessage("Incorrect user/password. Please try again.");
              AlertDialog dialog = builder.create();
              dialog.show();
         }
    }

    public void sending(View view){

        Send send = new Send();
        String url = "https://fcm.googleapis.com/fcm/send";

        String payload =
                "{\"to\":\"/topics/"
                        +to.getText().toString()+
                        "\",\"notification\": {\"title\": \"This is the title\",\"text\": \"Did you make it?!\",\"click_action\": \""
                        +page.getText().toString()+
                        "\"}}";

        send.execute(url, payload , token );
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

    private String key = "key=AAAAjmnq9XU:APA91bF4uWqQchNv6IkWiGyn93uwUzXjh3PaKNoYxEkAO5o9GG5I2v2f9CB7aPl7g4v2NtX5uJ4Hm397pPT5rvWIXDalow7tvEJa_lpusuXwXAIaIRToxFfJuQ6_6gCwZijxuVaHAkSgwmRFe1XX8JokYNM2sILuHQ";

    public Send(){}

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
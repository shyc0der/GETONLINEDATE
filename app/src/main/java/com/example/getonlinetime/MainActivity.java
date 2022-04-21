package com.example.getonlinetime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    String jsonString="http://worldtimeapi.org/api/timezone/Africa/Nairobi";
     Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
       new  GetOnlineDate().execute(jsonString);
    }

    public class GetOnlineDate extends AsyncTask<String ,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.e("start","8888888888888888888888");

            try {
                HttpURLConnection urlConnection= null;

                StringBuilder result=new StringBuilder();
                Log.e("start","8888888888888888888888");
                try {
                    Log.e("start","8888888888888888888888");
                    URL url=new URL(urls[0]);
                    urlConnection= (HttpURLConnection)url.openConnection();
                    int code=urlConnection.getResponseCode();
                    Log.e("start", String.valueOf(code));
                    if(code==200){
                        InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
                        String line="";
                        while ((line=bufferedReader.readLine()) != null)
                            result.append(line);
                        in.close();
                    }
                    else{
                        return "error on fetching";
                    }
                    Log.e("******",result.toString());
                    return result.toString();



                }
                catch (MalformedURLException e){
                    Log.e("Malformed",e.getMessage());
                   return "Malformed Url";
                }
                catch (IOException e){
                    Log.e("exception",e.getMessage());
                    return  "io exception";

            }
                finally {
                    if(urlConnection !=null){
                        urlConnection.disconnect();
                    }
                }

            }
            catch (Exception e){
                Log.e("Exception",e.getMessage());
                return "null";
            }

        }

        @Override
        protected void onPostExecute(String time) {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String times=dateFormat.format(calendar.getTime());

            try {
                JSONArray arr = new JSONArray(time);
                JSONObject jObj = arr.getJSONObject(0);
                String date = jObj.getString("datetime");
                Log.e("timesonline",date);
            }catch (JSONException e){
                e.printStackTrace();
            }

            Log.e("times",times);




            try {
                String areaTime = time.substring(time.indexOf(String.valueOf(times)), time.indexOf(String.valueOf(times))+4 ).trim();


                Toast.makeText(MainActivity.this, "The actual time is" +areaTime, Toast.LENGTH_LONG).show();
            }
            catch (IndexOutOfBoundsException e){
                Toast.makeText(MainActivity.this, "Mobile time is not correct", Toast.LENGTH_LONG).show();
            }

        }
    }
}
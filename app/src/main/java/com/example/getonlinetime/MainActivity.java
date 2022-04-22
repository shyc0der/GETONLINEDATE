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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                    JSONObject object=new JSONObject(String.valueOf(result));
                    try {
                        object.put("onlineDate",object.getString("datetime"));

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.e("******object",object.getString("datetime"));
                    Log.e("******",result.toString());

                    return object.getString("datetime");



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
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String times=dateFormat.format(calendar.getTime());



            Log.e("timrs",time);
            Log.e("times",times);


            try {
                int index=16;
                String times2 =time.substring(0,index);
                Date date1 = dateFormat.parse(times2);
                Date date2 = dateFormat.parse(times);
                Log.e("dates",date1.toString());
                Log.e("dates",date2.toString());
              if(date1.compareTo(date2) < 0)
              {
                  Toast.makeText(MainActivity.this, "hello date", Toast.LENGTH_LONG).show();
              }else{
                  Toast.makeText(MainActivity.this, "correct date ", Toast.LENGTH_LONG).show();
              }



                Log.e("timrs",times2);
                if(times2 == times){
                   Toast.makeText(MainActivity.this, "The actual time is" +times2, Toast.LENGTH_LONG).show();
                    Log.e("timrs",times2);
                }
                else{
                    Toast.makeText(MainActivity.this, "wrong time" +times2, Toast.LENGTH_LONG).show();
                }
               // String areaTime = time.substring(time.indexOf(String.valueOf(times)), time.indexOf(String.valueOf(times))+4 ).trim();



            }
            catch (IndexOutOfBoundsException | ParseException e){
                Toast.makeText(MainActivity.this, "Mobile time is not correct", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
}
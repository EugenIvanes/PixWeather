package com.example.pixweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixweather.adaptor.Categoryadaptor;
import com.example.pixweather.adaptor.Dalyadaptor;
import com.example.pixweather.model.Category;
import com.example.pixweather.model.Day;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView houertemperature;
    private RecyclerView Daytemperature;
    private  City cityc;
    private GetUrlDate getUrlDate;
    public TextView city,temperature,time,fils,daytemperature;
    private ImageView refresh;
    private RelativeLayout relativeLayout;
    private ConstraintLayout primzone,secondrelativeLovout;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Categoryadaptor categoryadaptor;
    private Dalyadaptor dalyadaptor;



    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.nointernet);
        secondrelativeLovout = findViewById(R.id.second_zone);
        primzone = findViewById(R.id.prim_zone);
        daytemperature = findViewById(R.id.daytemperature);
        Daytemperature = findViewById(R.id.Dayweather);
        if (!isConnected()){
           relativeLayout.setVisibility(View.VISIBLE);
           primzone.setVisibility(View.INVISIBLE);
           secondrelativeLovout.setVisibility(View.INVISIBLE);
           Daytemperature.setVisibility(View.INVISIBLE);
        }
        else if(isConnected()){
        temperature = findViewById(R.id.Temperature);
        refresh = (ImageView) findViewById(R.id.refresh);
        city = findViewById(R.id.City);
        time = findViewById(R.id.time);
        fils = findViewById(R.id.fils);
        init();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()){
                    relativeLayout.setVisibility(View.VISIBLE);
                    primzone.setVisibility(View.INVISIBLE);
                    secondrelativeLovout.setVisibility(View.INVISIBLE);
                    Daytemperature.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                    refresh.startAnimation(animation);
                }
                else {
                    relativeLayout.setVisibility(View.GONE);
                    init();
                    primzone.setVisibility(View.VISIBLE);
                    primzone.setVisibility(View.VISIBLE);
                    secondrelativeLovout.setVisibility(View.VISIBLE);
                    Daytemperature.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                    refresh.startAnimation(animation);

            }}
        });}
    }

    public void hourdatasend(int id,String data,int temp,String description,ArrayList categoryList) {
            categoryList.add(new Category(id, data, temp, description));
            setCAtegoryRecycler(categoryList);

    }
    public void dailydatasend(int id,int Day,int Night,String description,String Daydata,ArrayList dayList){
        dayList.add(new Day(id,Day,Night,description,Daydata));
        System.out.println(Day);
        setDailyRecycler(dayList);

    }

    private  void setDailyRecycler(List<Day> dayList){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        Daytemperature.setLayoutManager(layoutManager);
        dalyadaptor = new Dalyadaptor(this,dayList);
        Daytemperature.setAdapter(dalyadaptor);
    }


    private void setCAtegoryRecycler(List<Category> categoryList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this, RecyclerView.HORIZONTAL,false);
        houertemperature = findViewById(R.id.houertemperature);
        houertemperature.setLayoutManager(layoutManager);
        categoryadaptor = new Categoryadaptor(this, categoryList);
        houertemperature.setAdapter(categoryadaptor);
    }
    public void init(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }
    }


    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permision denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MainActivity.this)
                        .removeLocationUpdates(this);
                if(locationResult!= null && locationResult.getLocations().size()>0){
                    int latestLocationIntex = locationResult.getLocations().size()-1;
                    double latitude =
                            locationResult.getLocations().get(latestLocationIntex).getLatitude();
                    double longitude =
                            locationResult.getLocations().get(latestLocationIntex).getLongitude();
                    String key = "82d6f1eff822a4d5d74bcabfb52b15fe";
                    String URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&appid="+key+"&units=metric&lang=ro";
                    String URL2="https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+key+"&units=metric&lang=ro";
                    cityc = new City();
                    cityc.execute(URL2);
                    getUrlDate = new GetUrlDate();
                    getUrlDate.execute(URL);
                }
            }
        }, Looper.getMainLooper());

    }

    public class GetUrlDate extends AsyncTask<String,String,String> {
        MainActivity mainActivity = new MainActivity();
        

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null)
                    connection.disconnect();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                Double CurentTemp = jsonObject.getJSONObject("current").getDouble("temp");
                int CurentTime = jsonObject.getJSONObject("current").getInt("dt");
                double filslike = jsonObject.getJSONObject("current").getDouble("feels_like");
                String descriptin = jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");

                temperature.setText(temperature(CurentTemp) + "°C");
                time.setText(curentTime(CurentTime));
                city.setText(cityc.salut);
                fils.setText("Se simte ca " + temperature(filslike) + "°C. " + firstUpperCase(descriptin));
                daytemperature.setText("" + temperature(jsonObject.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getDouble("max")) + "/" + temperature(jsonObject.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getDouble("min")));
                executhouertemperatur(result);
                executDaytemperature(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void executDaytemperature(String result){
            JSONObject jsonObject = null;
            List<Dalyadaptor> dalyadaptors = new ArrayList<>();
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 7;i++ ){

                int Daytemp = 0;
                int Night = 0;
                String description = "";
                String DayData = "";
                try {

                    Daytemp = temperature(jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("day"));
                    Night = temperature(jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("night"));
                    description = jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                    DayData = Daytime(jsonObject.getJSONArray("daily").getJSONObject(i).getInt("dt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dailydatasend(i,Daytemp, Night ,description,DayData,(ArrayList) dalyadaptors);
            }
        }

        public void executhouertemperatur(String result){
            JSONObject jsonObject = null;
            List<Category> categoryList = new ArrayList<>();
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < 24; i++) {

                try {
                    String data = houerTime(jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("dt"));
                    int temp = temperature(jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("temp"));
                    String description = jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                    hourdatasend(i, data, temp, description, (ArrayList) categoryList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


        public String firstUpperCase(String word){
            if(word == null) return null;
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        }
        public String curentTime(int Time){
            long epoch = Time;
            String date = new java.text.SimpleDateFormat("MMM.dd").format(new java.util.Date (epoch*1000));
            return date;
        }
        public String houerTime(int Time){
            long epoch = Time;
            String date = new java.text.SimpleDateFormat("HH.mm").format(new java.util.Date (epoch*1000));
            return date;
        }
        public String Daytime(int Time){
            long epoch = Time;
            String date = new java.text.SimpleDateFormat("EEEE").format(new java.util.Date (epoch*1000));
            return date;
        }
        public int temperature(double temp){
            double tempfortemperature = 0.50;
            if(temp % 1 > tempfortemperature && temp>0){//-7.5%1=-0.5
                temp++;
                return (int) temp;
            }
            else if(temp%1 < tempfortemperature&&temp < 0){
                temp--;
                return (int) temp;
            }else
            return (int) temp;
        }

    }





}

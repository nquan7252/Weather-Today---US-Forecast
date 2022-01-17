package com.example.weathertoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {
    TextView txtview;
    String lat, lng,name;
    TextClock textClock;
    private TextView  citylabell, maxtempp, mintempp, feelslikee, humidityy, statuss;
    Criteria criteria;
    RelativeLayout relativeLayout;
    boolean day;
    ArrayList<MainItem> mainItems,mainModels1;
    ArrayList<City> cities;
    RecyclerView recyclerView,recyclerView1;
    MainAdapter mainAdapter;
    MainAdapter1 mainAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submain);
        setTitle("");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lat = extras.getString("latitude");
            lng = extras.getString("longitude");
            name=extras.getString("name");
        }
        textClock=findViewById(R.id.textClock1);
        recyclerView = findViewById(R.id.recyview);
        recyclerView1 = findViewById(R.id.hourlyview);
        relativeLayout = (RelativeLayout) findViewById(R.id.srcview);

        maxtempp = findViewById(R.id.maxtemp);
        mintempp = findViewById(R.id.mintemp);
        feelslikee = findViewById(R.id.feelslike);
        humidityy = findViewById(R.id.humidity);
        txtview = findViewById(R.id.textView);
        citylabell = findViewById(R.id.citylabel);
        statuss = findViewById(R.id.status);
        criteria = new Criteria();
        mainItems = new ArrayList<>();
        mainModels1 = new ArrayList<>();
        cities = new ArrayList<>();
        citylabell.setText(name);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        getData(lat,lng);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(MainActivity2.this, mainItems, day);
        mainAdapter1 = new MainAdapter1(MainActivity2.this, mainModels1, day);
        recyclerView.setAdapter(mainAdapter);
        recyclerView1.setAdapter(mainAdapter1);

    }



    public void getData(String newLat, String newLong) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + newLat + "&lon=" + newLong + "&units=imperial&exclude=minutely&appid=a35ad66fb43eb8ced8c62177a84ec3a9";
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String timezone=response.getString("timezone").replaceAll(" ","_");;
                    textClock.setTimeZone(timezone);
                    textClock.setFormat12Hour("EEEE dd MMM yyyy:hh:mm:ss a");

                    SimpleDateFormat sdf1=new SimpleDateFormat("HH");
                    Date current=new Date(Integer.parseInt(response.getJSONObject("current").getString("dt"))*1000L);
                    if(Integer.parseInt(sdf1.format(current))>=6&&Integer.parseInt(sdf1.format(current))<18){
                        relativeLayout.setBackgroundResource(R.drawable.day);
                        day=true;
                    }
                    else{
                        relativeLayout.setBackgroundResource(R.drawable.night);
                        day=false;
                    }
                    if (day) {
                        txtview.setTextColor(Color.parseColor("#264653"));
                        maxtempp.setTextColor(Color.parseColor("#264653"));
                        mintempp.setTextColor(Color.parseColor("#264653"));
                        humidityy.setTextColor(Color.parseColor("#264653"));
                        feelslikee.setTextColor(Color.parseColor("#264653"));
                        statuss.setTextColor(Color.parseColor("#264653"));
                        citylabell.setTextColor(Color.parseColor("#264653"));
                        textClock.setTextColor(Color.parseColor("#264653"));
                    } else {
                        txtview.setTextColor(Color.WHITE);
                        maxtempp.setTextColor(Color.WHITE);
                        mintempp.setTextColor(Color.WHITE);
                        humidityy.setTextColor(Color.WHITE);
                        feelslikee.setTextColor(Color.WHITE);
                        statuss.setTextColor(Color.WHITE);
                        citylabell.setTextColor(Color.WHITE);
                        textClock.setTextColor(Color.WHITE);

                    }

                    txtview.setText(response.getJSONObject("current").getString("temp") + "° F");
                    maxtempp.setText("High: " + response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("max") + "° F");
                    mintempp.setText("Low: " + response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("min") + "° F");
                    humidityy.setText("Humidity: " + response.getJSONObject("current").getString("humidity") + "%");
                    feelslikee.setText("Feels like: " + response.getJSONObject("current").getString("feels_like") + "° F");
                    statuss.setText((response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description")).substring(0, 1).toUpperCase() + (response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());

                    for (int i = 0; i < response.getJSONArray("daily").length(); i++) {
                        MainItem mainItem = new MainItem();
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        int unix=Integer.parseInt(response.getJSONArray("daily").getJSONObject(i).getString("dt"));
                        Date dateFormat = new java.util.Date(Integer.parseInt(response.getJSONArray("daily").getJSONObject(i).getString("dt")) * 1000L);
                        if (i == 0) {
                            mainItem.setDayy("Today");
                        } else {
                            mainItem.setDayy(sdf.format(dateFormat));
                        }
                        mainItem.setStatus((response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(0, 1).toUpperCase() + (response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());
                        mainItem.setTemp(response.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getString("max") + "|");
                        mainItem.setTemp1( response.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getString("min"));
                        switch (response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")) {
                            case "Clear":
                                mainItem.setImg(R.drawable.sun);
                                break;
                            case "Clouds":
                                mainItem.setImg(R.drawable.cloudy);
                                break;
                            case"Atmosephere":
                                if (day) {
                                    mainItem.setImg(R.drawable.fog);
                                }   else {
                                    mainItem.setImg( R.drawable.fogn);
                                }
                                break;
                            case"Snow":
                                mainItem.setImg(R.drawable.snowing);
                                break;
                            case "Rain":
                            case "Drizzle":
                                mainItem.setImg(R.drawable.rain);
                                break;
                            case "Thunderstorm":
                                mainItem.setImg(R.drawable.storm);
                                break;
                            default:break;
                        }
                        mainItems.add(mainItem);
                    }

                    for (int i = 0; i < 25; i++) {

                        MainItem mainItem1 = new MainItem();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh a");
                        int unix=Integer.parseInt(response.getJSONArray("hourly").getJSONObject(i).getString("dt"));
                        int timechange=unix+Integer.parseInt(response.getString("timezone_offset"));
                        Date dateFormat = new java.util.Date(timechange* 1000L);
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        if (i == 0) {
                            mainItem1.setDayy("Now");
                        } else {
                            mainItem1.setDayy(sdf.format(dateFormat));
                        }
                        Log.d("hour", String.valueOf(i));
                        mainItem1.setStatus((response.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(0, 1).toUpperCase() + (response.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());
                        mainItem1.setTemp(response.getJSONArray("hourly").getJSONObject(i).getString("temp").toString());
                        switch (response.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")) {
                            case "Clear":
                                if (Integer.parseInt(sdf1.format(dateFormat)) >= 6 && Integer.parseInt(sdf1.format(dateFormat)) <= 18) {
                                    mainItem1.setImg(R.drawable.sun);
                                } else
                                    mainItem1.setImg(R.drawable.moon);
                                break;
                            case "Clouds":
                                if (Integer.parseInt(sdf1.format(dateFormat)) >= 6 && Integer.parseInt(sdf1.format(dateFormat)) <= 18) {
                                    mainItem1.setImg(R.drawable.cloudy);
                                } else
                                    mainItem1.setImg(R.drawable.cloudynight);
                                break;
                            case"Atmosephere":
                                if (day) {
                                    mainItem1.setImg(R.drawable.fog);
                                }   else {
                                    mainItem1.setImg(R.drawable.fogn);
                                }
                                break;
                            case"Snow":
                                mainItem1.setImg(R.drawable.snowing);
                                break;
                            case "Rain":
                            case "Drizzle":
                                mainItem1.setImg(R.drawable.rain);
                                break;
                            case "Thunderstorm":
                                mainItem1.setImg(R.drawable.storm);
                                break;
                            default:break;
                        }
                        mainModels1.add(mainItem1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtview.setText("error");
            }
        });
        queue.add(request);


    }
}

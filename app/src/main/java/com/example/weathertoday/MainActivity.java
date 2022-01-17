package com.example.weathertoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView txtview, citylabell, maxtempp, mintempp, feelslikee, humidityy, statuss;
    private double lat=0;
    private double lng=0;
    Location location;
    LocationManager loc;
    LocationListener locationListener;
    String bestProvider;
    Criteria criteria;
    RelativeLayout relativeLayout;
    boolean day;
    ArrayList<MainItem> mainItems,mainModels1;
    ArrayList<ArrayList<MainItem>> parentMain;
    ArrayList<City> cities;
    RecyclerView recyclerView,recyclerView1,recyclerView2;
    MainAdapter mainAdapter;
    MainAdapter1 mainAdapter1;
    MainAdapter2 mainAdapter2;
    TextClock textClock;
    ImageView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info=findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Info.class));
            }
        });
        textClock=findViewById(R.id.textClock);
        textClock.setFormat12Hour("EEEE dd MMM yyyy:hh:mm:ss a");

        recyclerView=findViewById(R.id.recyview);
        recyclerView1=findViewById(R.id.hourlyview);
        recyclerView2=findViewById(R.id.cityRecyView);
        relativeLayout=(RelativeLayout)findViewById(R.id.srcview);
        Calendar currentTime= Calendar.getInstance();
        if (currentTime.get(Calendar.HOUR_OF_DAY)>=6&&currentTime.get(Calendar.HOUR_OF_DAY)<18) {
            relativeLayout.setBackgroundResource(R.drawable.day);
            textClock.setTextColor(Color.parseColor("#264653"));
            day=true;
        }
        else{
            day=false;
            textClock.setTextColor(Color.WHITE);
            relativeLayout.setBackgroundResource(R.drawable.night);
        }
        maxtempp = findViewById(R.id.maxtemp);
        mintempp = findViewById(R.id.mintemp);
        feelslikee = findViewById(R.id.feelslike);
        humidityy = findViewById(R.id.humidity);
        txtview = findViewById(R.id.textView);
        citylabell = findViewById(R.id.citylabel);
        statuss = findViewById(R.id.status);
        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria=new Criteria();
        bestProvider = String.valueOf(loc.getBestProvider(criteria, true)).toString();
        mainItems =new ArrayList<>();
        mainModels1=new ArrayList<>();
        parentMain=new ArrayList<>();
        cities=new ArrayList<>();
        intializecity();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},100);
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        lng = location.getLongitude();
                        lat = location.getLatitude();
                    }
                };
                loc.requestLocationUpdates(bestProvider, 2000, 10, locationListener);
        }
        location = loc.getLastKnownLocation(bestProvider);
        if (location!=null) {
            lng = location.getLongitude();
            lat = location.getLatitude();
           getData(String.valueOf(lat),String.valueOf(lng));
        }
        else{
            loc.requestLocationUpdates(bestProvider,1000,0,this);
        }
        Geocoder geocoder=new Geocoder(this,Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            citylabell.setText(addresses.get(0).getLocality());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        mainAdapter=new MainAdapter(MainActivity.this, mainItems,day);
        mainAdapter1=new MainAdapter1(MainActivity.this,mainModels1,day);
        mainAdapter2=new MainAdapter2(MainActivity.this,cities,day);
        recyclerView.setAdapter(mainAdapter);
        recyclerView1.setAdapter(mainAdapter1);
        recyclerView2.setAdapter(mainAdapter2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) searchItem.getActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                recyclerView2.setVisibility(View.VISIBLE);

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerView2.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainAdapter2.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void intializecity() {
        try {

            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("uscities.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                City city = new City();
                String[] arr=line.split(",");
                city.setName(arr[0]);
                city.setCityLat(arr[1]);
                city.setCityLng(arr[2]);
                cities.add(city);
                Log.d("city", "hi");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        }

    public void getData(String newLat,String newLong){
        String url="https://api.openweathermap.org/data/2.5/onecall?lat="+newLat+"&lon="+newLong+"&units=imperial&exclude=minutely&appid=af68688154fa0ded984ff31d3127a687";
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if(day){
                        txtview.setTextColor(Color.parseColor("#264653"));
                        maxtempp.setTextColor(Color.parseColor("#264653"));
                        mintempp.setTextColor(Color.parseColor("#264653"));
                        humidityy.setTextColor(Color.parseColor("#264653"));
                        feelslikee.setTextColor(Color.parseColor("#264653"));
                        statuss.setTextColor(Color.parseColor("#264653"));
                        citylabell.setTextColor(Color.parseColor("#264653"));
                    }
                    else {
                        txtview.setTextColor(Color.WHITE);
                        maxtempp.setTextColor(Color.WHITE);
                        mintempp.setTextColor(Color.WHITE);
                        humidityy.setTextColor(Color.WHITE);
                        feelslikee.setTextColor(Color.WHITE);
                        statuss.setTextColor(Color.WHITE);
                        citylabell.setTextColor(Color.WHITE);
                    }

                    txtview.setText(response.getJSONObject("current").getString("temp")+"° F");
                    maxtempp.setText("High: "+response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("max")+"° F");
                    mintempp.setText("Low: "+response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("min")+"° F");
                    humidityy.setText("Humidity: "+response.getJSONObject("current").getString("humidity")+"%");
                    feelslikee.setText("Feels like: "+response.getJSONObject("current").getString("feels_like")+"° F");
                    statuss.setText((response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description")).substring(0,1).toUpperCase()+(response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());

                    for (int i=0;i<response.getJSONArray("daily").length();i++){
                        MainItem mainItem =new MainItem();
                        SimpleDateFormat sdf=new SimpleDateFormat("EEEE");
                        Date dateFormat=new java.util.Date(Integer.parseInt(response.getJSONArray("daily").getJSONObject(i).getString("dt"))*1000L);
                        if (i==0){
                            mainItem.setDayy("Today");
                        }
                        else {
                            mainItem.setDayy(sdf.format(dateFormat));
                        }
                        mainItem.setStatus((response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(0,1).toUpperCase()+(response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());
                        mainItem.setTemp(response.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getString("max")+"|");
                        mainItem.setTemp1(response.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getString("min"));
                        switch (response.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")){
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

                    for (int i=0;i<25;i++){

                        MainItem mainItem1 =new MainItem();
                        SimpleDateFormat sdf=new SimpleDateFormat("hh a");
                        SimpleDateFormat sdf1=new SimpleDateFormat("kk");
                        Date dateFormat=new java.util.Date(Integer.parseInt(response.getJSONArray("hourly").getJSONObject(i).getString("dt"))*1000L);
                        if (i==0){
                            mainItem1.setDayy("Now");
                        }
                        else {
                            mainItem1.setDayy(sdf.format(dateFormat));
                        }
                        Log.d("hour",String.valueOf(i));
                        mainItem1.setStatus((response.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(0,1).toUpperCase()+(response.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description")).substring(1).toLowerCase());
                        mainItem1.setTemp(response.getJSONArray("hourly").getJSONObject(i).getString("temp"));
                        mainItem1.setTemp1(null);
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
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                txtview.setText("error");
            }
        });
        queue.add(request);


    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        loc.removeUpdates(this);
        lat=location.getLatitude();
        lng=location.getLongitude();
        getData(String.valueOf(lat),String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
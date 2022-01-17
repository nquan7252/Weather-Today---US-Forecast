package com.example.weathertoday;

public class City {
    private String cityLat;
    private String cityLng;
    private String name;
    public City(String lat,String lng,String name){
        this.cityLat=lat;
        this.cityLng=lng;
        this.name=name;
    }
    public City(){}
    public String getCitylat(){
        return cityLat;
    }
    public String getCityLng(){
        return cityLng;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setCityLat(String lat){
        this.cityLat=lat;
    }
    public void setCityLng(String lng){
        this.cityLng=lng;
    }
}

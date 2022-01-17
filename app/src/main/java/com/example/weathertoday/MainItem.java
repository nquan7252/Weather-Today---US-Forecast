package com.example.weathertoday;

public class MainItem {
    private int img;
    private String temp;
    private String temp1;
    private String status;
    private String dayy;
    public MainItem(int img, String temp, String temp1, String status, String dayy){
        this.img=img;
        this.temp=temp;
        this.status=status;
        this.dayy=dayy;
        this.temp1=temp1;
    }
    public MainItem(){

    }
    public int getImg() {
        return img;
    }

    public String getStatus() {
        return status;
    }

    public String getTemp() {
        return temp;
    }

    public String getDayy() {
        return dayy;
    }

    public String getTemp1(){return temp1;}
    public void setDayy(String dayy) {
        this.dayy = dayy;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
    public void setTemp1(String temp1){
        this.temp1=temp1;
    }
}

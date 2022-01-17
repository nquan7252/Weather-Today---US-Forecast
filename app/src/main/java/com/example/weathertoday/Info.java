package com.example.weathertoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Info extends AppCompatActivity {
ImageView webimg,instaimg,fbimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("About this app");
        webimg=findViewById(R.id.website);
        instaimg=findViewById(R.id.insta);
        fbimg=findViewById(R.id.facebook);
    }
    public void goLink(View view){
        switch (view.getId()){
            case (R.id.facebook):String url = "http://www.google.com";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
                break;
            case (R.id.website):String url1 = "https://miwa.sbs/";
                Uri uriUrl1 = Uri.parse(url1);
                Intent launchBrowser1 = new Intent(Intent.ACTION_VIEW, uriUrl1);
                startActivity(launchBrowser1);
                break;
            case(R.id.insta):String url2 = "https://www.instagram.com/_mquannn_/";
                Uri uriUrl2 = Uri.parse(url2);
                Intent launchBrowser2 = new Intent(Intent.ACTION_VIEW, uriUrl2);
                startActivity(launchBrowser2);
                break;
        }
    }
}
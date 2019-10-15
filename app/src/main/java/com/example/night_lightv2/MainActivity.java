package com.example.night_lightv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> coordinatesArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] url = {"https://seung2613.github.io/street-lighting-poles/street-lighting-poles.json", "get"};
        new myAsyncTask().execute(url);
    }


    public void onClickMap(View view){
        startActivity(new Intent(this, MapsActivity.class));
    }
    public void onClickReport(View view){
        startActivity(new Intent(this, Report.class));
    }
    public void onClickSOS(View view){
        startActivity(new Intent(this, SOS.class));
    }
}

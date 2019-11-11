package com.example.night_lightv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] url = {"https://seung2613.github.io/street-lighting-poles/street-lighting-poles.json", "get"};
        new myAsyncTask().execute(url);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

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

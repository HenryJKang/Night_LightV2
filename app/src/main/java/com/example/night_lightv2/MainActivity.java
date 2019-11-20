package com.example.night_lightv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {
    public  myAsyncTask mat;
    public Button mapbtn;
    public static ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] url = {"https://seung2613.github.io/street-lighting-poles/street-lighting-poles.json", "get"};
        new myAsyncTask().execute(url);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        pb= (ProgressBar)findViewById(R.id.progressBar);
        mat= new myAsyncTask();


    }


    public void onClickMap(View view)
    {
        if(mat.getLoadingStatus()== true){
            startActivity(new Intent(this, MapsActivity.class));
        }else{
            loading();
        }

    }
    public void onClickSOS(View view){
        startActivity(new Intent(this, SOS.class));
    }
    public void loading(){
        Toast.makeText(getApplicationContext(), "Still Loading...", Toast.LENGTH_SHORT).show();
    }
}

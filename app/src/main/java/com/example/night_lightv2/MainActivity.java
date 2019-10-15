package com.example.night_lightv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

package com.example.night_lightv2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SOS extends AppCompatActivity {
    static MapsActivity maps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        (findViewById(R.id.sosCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:911"));
                startActivity(callIntent);
            }
        });


        (findViewById(R.id.sosSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone ="";

                String googleurl =  "https://www.google.com/maps/dir/?api=1&origin=&destination=";
                googleurl += maps.getCurrentLocation()[0];
                googleurl +=",";
                googleurl += maps.getCurrentLocation()[1];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone ));
                intent.putExtra("sms_body", googleurl);
                startActivity(intent);
                }



        });



    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


}

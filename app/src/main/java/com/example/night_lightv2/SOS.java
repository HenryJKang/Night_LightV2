package com.example.night_lightv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SOS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

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
                //https://www.google.com/maps/dir/?api=1&origin=&destination="Destination"
                //https://www.google.com/maps/dir/?api=1&origin=&destination="Destination"
                //https://www.google.com/maps/dir/?api=1&origin=&destination="Destination"
                //https://www.google.com/maps/dir/?api=1&origin=&destination="Destination"
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //---insert here

//                Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                // double longitude = myLocation.getLongitude();
                // double latitude = myLocation.getLatitude();

//String message = "http://maps.google.com/?q=" + {latitude} + "," + {longitude}

            }
        });


    }


}

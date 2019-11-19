package com.example.night_lightv2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SOS extends AppCompatActivity {

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
                googleurl += getCurrentLocation()[0];
                googleurl +=",";
                googleurl += getCurrentLocation()[1];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone ));
                intent.putExtra("sms_body", googleurl);
                startActivity(intent);
                }
        });

    }
    public boolean checkLocationPermission() {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public double[] getCurrentLocation() {
        double[] xy = new double[2];
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if (checkLocationPermission()) {

            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            //     System.out.println("Latitude ~~~~~~~ "+lastKnownLocation.getLatitude());
            xy[0] = lastKnownLocation.getLatitude();
            xy[1] = lastKnownLocation.getLongitude();
            //  System.out.println(       "Longitidue ~~~~~~~~~~" + lastKnownLocation.getLongitude());
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Location not given permissions Please go to your settings and allow enable location sharing",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("new location lat ", Double.toString(location.getLatitude()));
                Log.d("new location long ", Double.toString(location.getLongitude()));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        return xy;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


}

package com.example.night_lightv2;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    coordinatesArr.get(0)[0] +" "+coordinatesArr.get(0)[1]
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

//        for (int i = 0; i < 20; i++){
//
//              Log.d("jggghhhg", "      "+myAsyncTask.coordinatesArr.get(i+1)[0]);
//                LatLng asd = new LatLng(myAsyncTask.coordinatesArr.get(i+1)[0], myAsyncTask.coordinatesArr.get(i+1)[1]);
//                mMap.addMarker(new MarkerOptions().position(asd).title("marker: " + counter++));
//
//
//        }
        for (int i = 0; i < myAsyncTask.coordinatesArr.size(); i++){
                if ( null!= myAsyncTask.coordinatesArr.get(i)   && (i %1) == 0){
                  //  myAsyncTask.coordinatesArr.get(counter+2)[1] != null ||  myAsyncTask.coordinatesArr.get(counter+2)[0] != null){
                LatLng asd = new LatLng(myAsyncTask.coordinatesArr.get(i)[1], myAsyncTask.coordinatesArr.get(i)[0]);
                    int height = 35;
                    int width = 35;
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bulb);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                MarkerOptions marker = new MarkerOptions().position(asd).title("marker: " + i);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                mMap.addMarker(marker);
            } else{
            }


        }

        // Add a marker in Sydney and move the camera
        LatLng BCIT = new LatLng(49.251370, -123.002656);
        mMap.addMarker(new MarkerOptions().position(BCIT).title("Marker in BCIT"));
       mMap.moveCamera( CameraUpdateFactory.zoomTo( 15.0f ) );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(BCIT));

        //###
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    //###
    }

    public void onZoom(View v) {
        if (v.getId() == R.id.btnZoomIn)
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        else
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    public void onCurrentLocation(View v) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if(checkLocationPermission()){

                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                System.out.println("Latitude ~~~~~~~ "+lastKnownLocation.getLatitude());
                System.out.println("Longitidue ~~~~~~~~~~" + lastKnownLocation.getLongitude());
        }

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("new location lat ",Double.toString(location.getLatitude()));
                Log.d("new location long ",Double.toString(location.getLongitude()));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    //###
    public boolean checkLocationPermission()
    {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

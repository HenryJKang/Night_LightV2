package com.example.night_lightv2;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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
        int counter = 0;
//        for (int i = 0; i < 20; i++){
//
//              Log.d("jggghhhg", "      "+myAsyncTask.coordinatesArr.get(i+1)[0]);
//                LatLng asd = new LatLng(myAsyncTask.coordinatesArr.get(i+1)[0], myAsyncTask.coordinatesArr.get(i+1)[1]);
//                mMap.addMarker(new MarkerOptions().position(asd).title("marker: " + counter++));
//
//
//        }
        while (counter < 2000){
            if ( null!= myAsyncTask.coordinatesArr.get(counter+2) ||  null !=myAsyncTask.coordinatesArr.get(counter+2)  ){
                  //  myAsyncTask.coordinatesArr.get(counter+2)[1] != null ||  myAsyncTask.coordinatesArr.get(counter+2)[0] != null){
                LatLng asd = new LatLng(myAsyncTask.coordinatesArr.get(counter+2)[1], myAsyncTask.coordinatesArr.get(counter+2)[0]);
                mMap.addMarker(new MarkerOptions().position(asd).title("marker: " + counter++));
                counter++;
            } else{
                counter ++;
            }


        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        LatLng BCIT = new LatLng(49.251370, -123.002656);
        mMap.addMarker(new MarkerOptions().position(BCIT).title("Marker in BCIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

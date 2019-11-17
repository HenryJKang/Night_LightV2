package com.example.night_lightv2;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private int bulbSize = 35;
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        double range = 0.005;
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

        double [] currloc = {49.255681, -123.062841};

        System.out.println("outside double for loop ");

          addLights(currloc, 0.005);
        // Add a marker in Sydney and move the camera
        LatLng BCIT = new LatLng(49.251370, -123.002656);
        mMap.addMarker(new MarkerOptions().position(BCIT).title("Marker in BCIT"));
        mMap.moveCamera( CameraUpdateFactory.zoomTo( 15.0f ) );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(BCIT));



        mMap.getUiSettings().setZoomControlsEnabled(true);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BCIT, 6));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(getCurrentLocation()[0],getCurrentLocation()[1]) , 14.0f) );
    }
    public void addLights(double[] currloc, double range){
        for (int i = 0; i < myAsyncTask.coordinatesArr.size(); i++) {
            //   for (int i = 0; i < 111; i++) {

            if (null != myAsyncTask.coordinatesArr.get(i) && (i % 1) == 0) {
                //  myAsyncTask.coordinatesArr.get(counter+2)[1] != null ||  myAsyncTask.coordinatesArr.get(counter+2)[0] != null){

                //addBulbToMap( myAsyncTask.coordinatesArr.get(i)[1], myAsyncTask.coordinatesArr.get(i)[0]);

                if (checkRange(currloc[0] - range, currloc[0] + range,
                        currloc[1] - range , currloc[1] + range, myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1])){

                    addBulbToMap(myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1]);
                }
            }

        }

    }
    public void addLights(double[] source, double[] dest, double range){
        for (int i = 0; i < myAsyncTask.coordinatesArr.size(); i++) {
            //   for (int i = 0; i < 111; i++) {

            if (null != myAsyncTask.coordinatesArr.get(i) && (i % 1) == 0) {
                //  myAsyncTask.coordinatesArr.get(counter+2)[1] != null ||  myAsyncTask.coordinatesArr.get(counter+2)[0] != null){

                //addBulbToMap( myAsyncTask.coordinatesArr.get(i)[1], myAsyncTask.coordinatesArr.get(i)[0]);

                if (checkRange(Math.min(source[0], dest[0]) - range, Math.max(source[0], dest[0]) + range ,Math.min(source[1], dest[1]) - range , Math.max(source[1], dest[1]) + range,
                        myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1])){

                    addBulbToMap(myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1]);
                }
            }

        }

    }
    public void addBulbToMap( double x, double y){
        LatLng loc = new LatLng(x , y);
        int height = bulbSize;
        int width = bulbSize;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bulb);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions marker = new MarkerOptions().position(loc).title("marker: " + x + " : " + y);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);
    }

    public void onZoom(View v) {
        if (v.getId() == R.id.btnZoomIn)
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        else
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    public double[] getCurrentLocation(){
        double[] xy =new double[2];
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if(checkLocationPermission()){

            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
       //     System.out.println("Latitude ~~~~~~~ "+lastKnownLocation.getLatitude());
            xy[0] = lastKnownLocation.getLatitude();
            xy[1] = lastKnownLocation.getLongitude();
          //  System.out.println(       "Longitidue ~~~~~~~~~~" + lastKnownLocation.getLongitude());
        } else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Location not given permissions Please go to your settings and allow enable location sharing",
                    Toast.LENGTH_SHORT);
            toast.show();        }
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
        return xy;
    }
//    public void setLightsFromXtoY(GoogleMap gMap, double startX, double startY, double destX, double destY ){
//        if(checkRange(Math.min(startX, destX), Math.max(startX, destX), Math.min(startY, destY), Math.max(startY, destY), destX, targetY)){
//            addBulbToMap(targetX, targetY);
//        }
//    }
    public boolean checkRange(double xMin, double xMax, double yMin, double yMax, double checkX, double checkY){
   // System.out.println("============xmin" + xMin + "xmax" + xMax + "check" + checkX);
        if( xMin <= checkX  && checkX <= xMax && yMin <= checkY && checkY <= yMax ){
            System.out.println("True");
            return true;
        }
        System.out.println("false");
        return false;

    }
    public void onTestbtn(View v) {
        //This is for you Rose to test some codes out.
      System.out.println("USER LOCATION \t x:  " +  getCurrentLocation()[0] + "\t y:" + getCurrentLocation()[1]);
      addBulbToMap(getCurrentLocation()[0], getCurrentLocation()[1]);
      addBulbToMap(49.200526595816854    , -123.170110
      );
      addBulbToMap(49.200526, -123.170110);
        addBulbToMap(49.20052, -123.170110);
        addBulbToMap(49.20054, -123.170110);

        //       LatLng BCIT = new LatLng(49.200526, -123.224110);
        //mMap.addMarker(new MarkerOptions().position(BCIT).title("Marker in BCIT"));
        //addBulbToMap(49.2005265, -123.2241106);
    }
    public void onCurrentLocation(View v) {
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(getCurrentLocation()[0],getCurrentLocation()[1]) , 14.0f) );
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

    public void onSearch(View v) {
        List<Address> addressList = null;

        EditText editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        String location = editTextLocation.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "You must enter a location.", Toast.LENGTH_LONG).show();
            return;
        }

            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1) ;
                if (addressList.size() == 0){
                    Toast.makeText(this, "Not applicable address.", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address adr = addressList.get(0);

            LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            double[] dest = {adr.getLatitude(), adr.getLongitude()};
       //     getCurrentLocation();
            drawRoute(getCurrentLocation(),dest);
         //   addLights(getCurrentLocation(), dest, 0.002);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


    }
    public void drawRoute(double[] origin, double[] destination){
        String o = origin[0] + ", " + origin[1];
        String d = destination[0] + ", " + destination[1];
        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();
        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDZ9Tbo5lu86ZVcCDkdBVBWLuJU_P7JuLQ")
                .build();
        //From YVR airport to BCIT-----------------------------
       // DirectionsApiRequest req = DirectionsApi.getDirections(context, o, d);
        DirectionsApiRequest req = DirectionsApi.newRequest(context).origin(o).destination(d).mode(TravelMode.WALKING);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("getLocalizedMessage()", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(5);
            mMap.addPolyline(opts);
        }
        for (LatLng point :  path){
            addBulbToMap(point.latitude, point.longitude);
            System.out.println( point.latitude+ ":::::"+point.longitude );
        }

    }


}




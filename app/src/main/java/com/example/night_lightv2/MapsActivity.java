package com.example.night_lightv2;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import static com.example.night_lightv2.myAsyncTask.status;

import android.Manifest;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    PlacesClient placesClient;
    static final private String API_KEY = "AIzaSyDZ9Tbo5lu86ZVcCDkdBVBWLuJU_P7JuLQ";
    private GoogleMap mMap;
    private int bulbSize = 35;
    RoutesHolder routesHolder;

    List<Polyline> polylines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }
        // Create a new Places client instance
        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

//        Button button = findViewById(R.id.altBtn);
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                for(Polyline line : polylines)
//                {
//                    line.remove();
//                    polylines.remove(line);
//                }
//                polylines.clear();
//                mMap.clear();
//                path.clear();
//                if (routesLen > 1) {
//
//                    if (counter < routesLen-1) {
//                        DirectionsRoute route = myRoutes.get(++counter);
//                        Log.e("drawing ...route ",Integer.toString(counter));
//                        drawPolyline(route);
//                    } else {
//                        counter = 0;
//                        DirectionsRoute route = myRoutes.get(counter);
//                        Log.e("drawing ...route ",Integer.toString(counter));
//                        drawPolyline(route);
//                    }
//
//                } else {
//                    //TODO: make toast "no alternative route available"
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "No alternative route available",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });

//    }


    /**
     * The first loading screen.
     * Shows user current location
     * The surrounding light bulbs around the user
     * Zoomed into current location too
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Geocoder geocoder = new Geocoder(this);
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
        try {

            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        double[] currentLocation = getCurrentLocation();

        // Add a marker in Sydney and move the camera


        mMap.getUiSettings().setZoomControlsEnabled(true);
        showCurrentLocation();


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
// TODO: Get info about the selected place.
                Log.i("Places--", "Place: " + place.getName() + ", " + place.getId());
                List<Address> addressList = null;
                String location = place.getName();
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address adr = addressList.get(0);

                LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                double[] dest = {adr.getLatitude(), adr.getLongitude()};

//                drawRoute(getCurrentLocation(), dest);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                routesHolder = new RoutesHolder(location, getCurrentLocation(), addressList.get(0));
                addRouteToMap();
            }

            @Override
            public void onError(Status status) {
                Log.i("Places Err--", "An error occurred: " + status);
            }
        });


    }

    void showCurrentLocation() {
        //pre setting icon size
        int width = 50;
        int height = 50;
        int rangeAroundCurrentLocation = 200;

        //getting Current location
        double[] currentLocation = getCurrentLocation();
        addLights(currentLocation, rangeAroundCurrentLocation);

        //adding marker
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.curloc);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        LatLng loc = new LatLng(currentLocation[0], currentLocation[1]);
        MarkerOptions marker = new MarkerOptions().position(loc).title("Your Location:" + " x: " + currentLocation[0] + " y: " + currentLocation[1]);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);
        //zooming
        zoomLocation(currentLocation, 14.0f);

    }

    public void addLights(double[] currloc, int range) {
        System.out.println("help");
        HashSet<LampValue> list = myAsyncTask.lamps.getSurroundingLamps(currloc[0], currloc[1], range);
        for (LampValue lampval : list) {
            addBulbToMap(lampval.getX(), lampval.getY());

        }


    }

    public void addLights(LampValue lamp, int range) {
        addBulbToMap(lamp.getX(), lamp.getY());
    }

    public void addLights(HashSet<LampKey> route, int range) {
        System.out.println("help");
        HashSet<LampValue> list = myAsyncTask.lamps.getSurroundingLamps(route, 2);


        for (LampValue lampval : list) {
            addBulbToMap(lampval.getX(), lampval.getY());

        }


    }

    public void addLights(double x, double y, int range) {
        System.out.println("help");
        HashSet<LampValue> list = myAsyncTask.lamps.getSurroundingLamps(x, y, range);
        for (LampValue lampval : list) {
            addBulbToMap(lampval.getX(), lampval.getY());

        }


    }

    //    public void addLights(double[] source, double[] dest, double range){
//        for (int i = 0; i < myAsyncTask.coordinatesArr.size(); i++) {
//            //   for (int i = 0; i < 111; i++) {
//
//            if (null != myAsyncTask.coordinatesArr.get(i) && (i % 1) == 0) {
//                //  myAsyncTask.coordinatesArr.get(counter+2)[1] != null ||  myAsyncTask.coordinatesArr.get(counter+2)[0] != null){
//
//                //addBulbToMap( myAsyncTask.coordinatesArr.get(i)[1], myAsyncTask.coordinatesArr.get(i)[0]);
//
//                if (checkRange(Math.min(source[0], dest[0]) - range, Math.max(source[0], dest[0]) + range ,Math.min(source[1], dest[1]) - range , Math.max(source[1], dest[1]) + range,
//                        myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1])){
//
//                    addBulbToMap(myAsyncTask.coordinatesArr.get(i)[0], myAsyncTask.coordinatesArr.get(i)[1]);
//                }
//            }
//
//        }
//
//    }
    public void addBulbToMap(double x, double y) {
        LatLng loc = new LatLng(x, y);
        int height = bulbSize;
        int width = bulbSize;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bulb);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions marker = new MarkerOptions().position(loc).title("marker: " + x + " : " + y);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);
    }

    //----------
    public void addCurrentLocationToMap(double x, double y) {
        LatLng loc = new LatLng(x, y);
        //int height = bulbSize;
        //int width = bulbSize;
        //BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bulb);
        //Bitmap b = bitmapdraw.getBitmap();
        //Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions marker = new MarkerOptions().position(loc).title("marker: " + x + " : " + y);
        //marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(marker);
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


    public boolean checkRange(double xMin, double xMax, double yMin, double yMax, double checkX, double checkY) {
        // System.out.println("============xmin" + xMin + "xmax" + xMax + "check" + checkX);
        if (xMin <= checkX && checkX <= xMax && yMin <= checkY && checkY <= yMax) {
            System.out.println("True");
            return true;
        }
        System.out.println("false");
        return false;

    }

    public void onCurrentLocation(View v) {
        System.out.println("USER LOCATION \t x:  " + getCurrentLocation()[0] + "\t y:" + getCurrentLocation()[1]);
        addBulbToMap(getCurrentLocation()[0], getCurrentLocation()[1]);
        addBulbToMap(49.200526595816854, -123.170110
        );
        addBulbToMap(49.200526, -123.170110);
        addBulbToMap(49.20052, -123.170110);
        addBulbToMap(49.20054, -123.170110);

        //       LatLng BCIT = new LatLng(49.200526, -123.224110);
        //mMap.addMarker(new MarkerOptions().position(BCIT).title("Marker in BCIT"));
        //addBulbToMap(49.2005265, -123.2241106);
        mMap.clear();
    }

    public void zoomLocation(View v) {
        double x = getCurrentLocation()[0];
        double y = getCurrentLocation()[1];
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x, y), 14.0f));
    }


    public void zoomLocation(double[] loc, float zoomLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc[0], loc[1]), zoomLocation));
    }

    public void zoomLocationAnimate(LatLng loc, float zoomLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.latitude, loc.longitude), zoomLocation));

    }

    //###
    public boolean checkLocationPermission() {

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



    public void addRouteToMap() {
        mMap.clear();
        TextView noOfLamps = findViewById(R.id.numberOfLamps);
        noOfLamps.setText("");
        LatLng latLng = new LatLng(routesHolder.destination[0], routesHolder.destination[1]);
        mMap.addMarker(new MarkerOptions().position(latLng).title(routesHolder.location));
        drawPolyline(routesHolder.currentRoute);
        drawBulbsOnRoute(routesHolder.latLngArrayOfCurrentRoute);
        float zoom = 10.0f;
        zoomLocationAnimate(getMiddleOfPath(routesHolder.origin, routesHolder.destination),
                (getZoomFloat(routesHolder.origin, routesHolder.destination)));
        noOfLamps.setText("" + routesHolder.noOfLampsInCurrentRoute);
    }

    public LatLng getMiddleOfPath(double[] origin, double[] destination) {
        //gettting path logic for zooming
        double startX = origin[0];
        double startY = origin[1];
        double endX = destination[0];
        double endY = destination[1];
        double differenceX = endX - startX;
        double differenceY = endY - startY;
        LatLng middleOfPath = new LatLng((differenceX / 2) + startX, (differenceY / 2) + startY);
        return middleOfPath;

    }

    public float getZoomFloat(double[] origin, double[] destination) {
        double startX = origin[0];
        double startY = destination[0];
        double endX = origin[1];
        double endY = destination[1];
        double differenceX = endX - startX;
        double differenceY = endY - startY;
        int maximum = Math.max((int) ((differenceX) * 100000), (int) ((differenceY) * 100000));
        float zoom = 10.0f;
        System.out.println("!@#" + maximum);
        return 10.0f;
    }

    public void drawBulbsOnRoute(List<LatLng> path) {
        HashSet<LampKey> lamplist = myAsyncTask.lamps.getLampsOnRoute(path, 3);
        HashSet<LampValue> lampValueList = myAsyncTask.lamps.getSurroundingLamps(lamplist, 3);
        for (LampValue lamp : lampValueList) {
            addLights(lamp, 3);
        }
    }

    //    public void onAlternativePath(View v){
//        for(Polyline line : polylines)
//        {
//            line.remove();
//            polylines.remove(line);
//        }
//        polylines.clear();
//        mMap.clear();
//        path.clear();
//        if (routesLen > 1) {
//
//            if (counter < routesLen-1) {
//                DirectionsRoute route = myRoutes.get(++counter);
//                Log.e("drawing ...route ",Integer.toString(counter));
//                drawPolyline(route);
//            } else {
//                counter = 0;
//                DirectionsRoute route = myRoutes.get(counter);
//                Log.e("drawing ...route ",Integer.toString(counter));
//                drawPolyline(route);
//            }
//
//        } else {
//            //TODO: make toast "no alternative route available"
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "No alternative route available",
//                    Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//    }
    public void onAlternativePath(View v) {
        routesHolder.updateToNextRoute();
        addRouteToMap();

    }
    //** oLD ONALTERNATIVE PATH
//    public void onAlternativePath(View v){
//
//
//        if (routesLen > 1) {
//
//            if (counter < routesLen-1) {
//                DirectionsRoute route = myRoutes.get(++counter);
//                Log.e("drawing ...route ",Integer.toString(counter));
//                drawPolyline(route);
//            } else {
//                counter = 0;
//                DirectionsRoute route = myRoutes.get(counter);
//                Log.e("drawing ...route ",Integer.toString(counter));
//                drawPolyline(route);
//            }
//
//        } else {
//            //TODO: make toast "no alternative route available"
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "No alternative route available",
//                    Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//    }
//    public void  showRoute(){
//        mMap.clear();
//        Address adr = addressList.get(0);
//        LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());
//        mMap.clear();
//        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//        double[] dest = {adr.getLatitude(), adr.getLongitude()};
//        //     getCurrentLocation();
//        DirectionsRoute route = getDirectionRoute(getCurrentLocation(), dest);
//        drawPolyline();
//        //   addLights(getCurrentLocation(), dest, 0.002);
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        showLampsOnRoute();
//
//        //gettting path logic for zooming
//        double startX =  path.get(0).latitude;
//        double startY = path.get(1).longitude;
//        double endX = path.get(path.size()-1).latitude;
//        double endY = path.get(path.size()-1).longitude;
//        double differenceX = endX - startX;
//        double differenceY = endY - startY;
//        LatLng middleOfPath = new LatLng((differenceX / 2 ) + startX , (differenceY / 2) + startY );
//        int maximum = Math.max((int) ((differenceX) * 100000), (int) ((differenceY )* 100000)) ;
//        float zoom = 10.0f;
//        System.out.println("!@#" + maximum);
//                  zoomLocationAnimate(middleOfPath, (zoom));

    // zoomLocationAnimate(middleOfPath, (zoom *));

//        if (maximum >= 3000){
//            zoomLocationAnimate(middleOfPath, (zoom * (float)(2)));
//            System.out.println("dafs414" + maximum);
//
//        }else if (3000 > maximum && maximum >= 2500){
//            zoomLocationAnimate(middleOfPath, (zoom * (float)(2.5)));
//
//System.out.println("419");
//        }
//        else if (2000 > maximum && maximum >= 1800){
//            zoomLocationAnimate(middleOfPath, (zoom * (float)(3)));
//            System.out.println("dafs422");
//
//
//        } else if (1800 > maximum && maximum >= 1600){
//            zoomLocationAnimate(middleOfPath, (zoom * (float)(3.5)));
//
//            System.out.println("dafs428");
//
//        }
//        else if (1600 > maximum && maximum >= 1400){
//            zoomLocationAnimate(middleOfPath, (zoom * (float)(3.5)));
//
//            System.out.println("dafs434");
//
//        }
//        else if (1400 > maximum && maximum >= 1000){
//            zoomLocationAnimate(middleOfPath, zoom * 4);
//
//            System.out.println("dafs440");
//
//        } else {
//            zoomLocationAnimate(middleOfPath, zoom * (float)4.5);
//            System.out.println("dafs445");
//
//
//        }
//
//    }
//
//
//    public DirectionsRoute getDirectionRoute(double[] origin, double[] destination) {
//
//        String o = origin[0] + ", " + origin[1];
//        String d = destination[0] + ", " + destination[1];
//
//        GeoApiContext context = new GeoApiContext.Builder()
//                .apiKey("AIzaSyDZ9Tbo5lu86ZVcCDkdBVBWLuJU_P7JuLQ")
//                .build();
//        // DirectionsApiRequest req = DirectionsApi.getDirections(context, o, d);
//        DirectionsApiRequest req = DirectionsApi.newRequest(context).origin(o).destination(d).mode(TravelMode.WALKING).alternatives(true);
//        try {
//            DirectionsResult res = req.await();
//            //Loop through legs and steps to get encoded polylines of each step
//            routesLen = res.routes.length ;
//        if (res.routes != null && routesLen > 0) {
//            Log.d("routesLen---", Integer.toString(routesLen));
//            route = res.routes[locationCounter%routesLen];
//
//            for (int i = 0; i < routesLen; i++) {
//                myRoutes.add(res.routes[i]);
//            }
//            Log.e("drawing ...route",Integer.toString(0));
//        }
//    } catch (Exception ex) {
//        Log.e("getLocalizedMessage()", ex.getLocalizedMessage());
//    }
//      return route;
//    }
//    public void showLampsOnRoute(){
//        HashSet<LampKey> lamplist= myAsyncTask.lamps.getLampsOnRoute(path, 3);
//        HashSet<LampValue> lampValueList = myAsyncTask.lamps.getSurroundingLamps(lamplist,3);
//        for (LampValue lamp : lampValueList ){
//            addLights(lamp,3);
//
//        }
//    }

    public void drawPolyline(DirectionsRoute route) {
        List<LatLng> path = new ArrayList<>();
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

        if (path.size() > 0) {

            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);

            polylines.add(this.mMap.addPolyline(opts));
        }
    }

}






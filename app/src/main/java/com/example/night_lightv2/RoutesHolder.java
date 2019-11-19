package com.example.night_lightv2;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RoutesHolder {
    String location;
    double[] destination = new double[2];
    double[] origin;
    int noOfDiffRoutes;
    DirectionsResult directionResults;
    DirectionsRoute currentRoute;
    GeoApiContext context;
    int currentRouteCounter;
    int noOfLampsInCurrentRoute;
    List<LatLng> latLngArrayOfCurrentRoute;
    public RoutesHolder(String loc, double[] origin, Address adr){
        this.destination[0] = adr.getLatitude();
        this.destination[1] = adr.getLongitude();
        this.location = loc;
        this.origin = origin;

        String o = origin[0] + ", " + origin[1];
        String d = destination[0] + ", " + destination[1];


        context = new GeoApiContext.Builder().apiKey("AIzaSyDZ9Tbo5lu86ZVcCDkdBVBWLuJU_P7JuLQ").build();


        DirectionsApiRequest req = DirectionsApi.newRequest(context).origin(o).destination(d).mode(TravelMode.WALKING).alternatives(true);
        try {
            //RES IS THE LIST OF DIFFERENT RESULTS
            directionResults = req.await();
            noOfDiffRoutes = directionResults.routes.length ;
            if (directionResults.routes != null && noOfDiffRoutes > 0) {
                Log.d("routesLen---", Integer.toString(noOfDiffRoutes));
                currentRoute = directionResults.routes[0];

                Log.e("drawing ...route",Integer.toString(0));
            }
        } catch (Exception ex) {
            Log.e("getLocalizedMessage()", ex.getLocalizedMessage());
        }
        setCurrentRouteToBrightest();
        currentRoute = this.directionResults.routes[currentRouteCounter];
        noOfLampsInCurrentRoute = getNoOfLampsInRoute(currentRoute);
        latLngArrayOfCurrentRoute = getLatLngArrayOfRoute(currentRoute);
    }

    public void updateToNextRoute(){
        currentRouteCounter++;
        currentRoute = this.directionResults.routes[currentRouteCounter%noOfDiffRoutes];
        noOfLampsInCurrentRoute = getNoOfLampsInRoute(currentRoute);
        latLngArrayOfCurrentRoute = getLatLngArrayOfRoute(currentRoute);

    }


    public static int getNoOfLampsInRoute(DirectionsRoute route){
        List<LatLng> path = getLatLngArrayOfRoute(route);
        HashSet<LampKey> lamplist= myAsyncTask.lamps.getLampsOnRoute(path, 3);
        HashSet<LampValue> lampValueList = myAsyncTask.lamps.getSurroundingLamps(lamplist,3);
        return lampValueList.size();
    }
    public static List<LatLng> getLatLngArrayOfRoute(DirectionsRoute route){
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
        return path;
    }
    public void setCurrentRouteToBrightest(){
        int max = 0;
        int keepTrack = 0;
        for (int i = 0; i < noOfDiffRoutes; i++) {
            System.out.println(getNoOfLampsInRoute(directionResults.routes[i] )+ "!@#!@#!@#");

           if (max < getNoOfLampsInRoute(directionResults.routes[i])) {
               System.out.println(max);
               max = getNoOfLampsInRoute(directionResults.routes[i]);
               keepTrack = i;

           }
        }
        currentRouteCounter = keepTrack;
    }
}

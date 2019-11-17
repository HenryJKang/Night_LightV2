package com.example.night_lightv2;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class myAsyncTask extends AsyncTask<String, Void, JSONObject> {
    //<params, progress, result>
    public static Lamps lamps;

    private String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e("tag1", "dfa" + e.getMessage());
        } catch (ProtocolException e) {
            Log.e("tag2", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("tag3", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("tag4", "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    protected JSONObject doInBackground(String... urls) {

        String jsonStr = null;

        // Making a request to url and getting response
        jsonStr = makeServiceCall(urls[0]);


        try {


            JSONObject jsonObject = new JSONObject(jsonStr);


            return jsonObject;


        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
//            TextView tv = findViewById(R.id.txtView);

        try {


//for android assignment--------------------------------------
            JSONArray myObj = jsonObject.getJSONArray("items");
            lamps = new Lamps();
            double xMin = 200.0;
            double xMax = -200.0;
            double yMin = 400.0;
            double yMax = -500.0;
            for (int i=0; i<myObj.length(); i++){
                JSONObject getobj = myObj.getJSONObject(i);
                JSONObject fields = getobj.getJSONObject("fields");
                JSONObject geom = fields.getJSONObject("geom");
                JSONArray coord = geom.getJSONArray("coordinates");
                double[] coor = {(double)coord.get(1), (double)coord.get(0)};
                lamps.addLamp(coor[0], coor[1]);

                if(xMin >= coor[0]){
                    xMin = coor[0];
                }
                if(yMin >= coor[1]){
                    yMin = coor[1];
                }
                if(xMax <= coor[0]){
                    xMax = coor[0];
                }
                if(yMax <= coor[1]){
                    yMax = coor[1];
                }
              //  lamps.add(coor);
            }

            lamps.addLamp( 49.23588, -123.03337);
        System.out.println(lamps.print());
        //    Log.d("Coordinates earr ", Arrays.toString(coordinatesArr.toArray()));
//            System.out.println("\n\n\nxMin" + xMin +"\t" + "xMax" + xMax);
//            System.out.println("yMin" + yMin + "\t" + "yMax" + yMax+"\n\n\n");

         //   Log.d("heyeyeyeyeyey arr ", ""+coordinatesArr.get(0)[0] +" "+coordinatesArr.get(0)[1]);





        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
package com.example.uf2progmulti;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiThread extends AsyncTask<Void, Void, String> {
    private LatLng latLng;

    public ApiThread(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Read API results
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedReader.readLine();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String unused) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(unused);
            // Ara hem d’agafar les dades del “fill” results
            jObject = jObject.getJSONObject("results");

            // Finalment agafem el “fill” sunrise de results
            String sunrise = jObject.getString("sunrise");
            Log.i("logtest", "------>" + sunrise);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        super.onPostExecute(unused);
    }
}

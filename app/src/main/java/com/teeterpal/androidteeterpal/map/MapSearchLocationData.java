package com.teeterpal.androidteeterpal.map;

import android.os.AsyncTask;
import android.util.Log;

import com.teeterpal.androidteeterpal.data.LocationObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwu on 3/20/15.
 */
public class MapSearchLocationData extends AsyncTask<String, Void, List<LocationObject>>{
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api";
    private static final String TYPE_GEOCODE = "/geocode";
    private static final String OUT_JSON = "/json";
    private static final String LOG_TAG = MapSearchLocationData.class.getSimpleName();

    private List<LocationObject> resultList;
    public AsyncResponse response = null;

    public interface AsyncResponse{
        void onFinishFetchLocationJSON(List<LocationObject> resultList);
    }

    @Override
    protected List<LocationObject> doInBackground(String... params) {
        String address = params[0];
        if(address.trim().length() == 0){
            return null;
        }

        resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_GEOCODE + OUT_JSON);
            sb.append("?address=" + URLEncoder.encode(address, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<LocationObject>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                LocationObject locationObject = new LocationObject();
                locationObject.setAddress(predsJsonArray.getJSONObject(i).getString("formatted_address"));
                JSONObject jo = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                locationObject.setLatitude(Double.parseDouble(jo.getString("lat")));
                locationObject.setLongitude(Double.parseDouble(jo.getString("lng")));
                resultList.add(locationObject);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(List<LocationObject> resultList) {
        response.onFinishFetchLocationJSON(resultList);
    }
}

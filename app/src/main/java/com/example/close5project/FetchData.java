package com.example.close5project;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.close5project.data.Close5SQLiteHelper;
import com.example.close5project.data.ItemObject;
import com.example.close5project.data.SellerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by weiwu on 4/4/15.
 */
public class FetchData extends AsyncTask {

    public interface FetchDataListener {
        void onFinishFetchData();
    }

    private FetchDataListener listener;
    private static final String LOG_TAG = FetchData.class.getSimpleName();
    private Close5SQLiteHelper dbHelper;
    private Context context;

    public FetchData(Context context, FetchDataListener listener){
        this.context = context;
        this.listener = listener;
    }

    private void getDataFromJson (String dataJsonStr) throws JSONException{
        // Clear db
        dbHelper = new Close5SQLiteHelper(context);
        dbHelper.deleteALLItem();
        dbHelper.deleteAllSeller();

        JSONObject jsonObject = new JSONObject(dataJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject sellerJson = jsonArray.getJSONObject(i);
            SellerObject sellerObject = new SellerObject();
            sellerObject.setSellerId(sellerJson.getString("id"));
            sellerObject.setSellerName(sellerJson.getString("name"));
            sellerObject.setSellerPhoto(sellerJson.getString("photo"));

            JSONArray itemArray = sellerJson.getJSONArray("items");
            sellerObject.setSellerListings(itemArray.length() + "");
            for(int j = 0; j < itemArray.length(); j++) {
                JSONObject itemJson = itemArray.getJSONObject(j);
                ItemObject itemObject = new ItemObject();
                itemObject.setItemId(itemJson.getString("id"));
                itemObject.setSellerId(itemJson.getString("userId"));

                // save itemObject into db
                long longItemId = dbHelper.addSellerItem(itemObject);
                //Log.e("longItemId", longItemId + "");
            }
            // Save sellerObject into db
            long longSellerId = dbHelper.addSeller(sellerObject);
            //Log.e("longSellerId", longSellerId + "");
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String dataJsonStr = null;
        //HttpClient httpClient = new DefaultHttpClient();
        //HttpResponse response;

        final String URL_STRING = "https://api.close5.com/users/items/grouped?maxDistance=50&lat=37.320352&lon=-121.9045411&limit=50&skip=0";

        try{
            // Create the request to website and open the connection
            URL url = new URL(URL_STRING);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream to a string
            InputStream inputStream = urlConnection.getInputStream();
            //InputStream inputStream = entity.getContent();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                // Stream was empty.
                return null;
            }
            dataJsonStr = buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "error ", e);
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try{
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        // Get data from json
        try {
            getDataFromJson(dataJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        listener.onFinishFetchData();
    }


    public static class AsyncFileDownloader
    {
        private FetchDataListener fetchDataListener;

        public AsyncFileDownloader( FetchDataListener fetchDataListener )
        {
            this.fetchDataListener = fetchDataListener;
        }

        public void download(Context mContext)
        {
            new FetchData(mContext, fetchDataListener).execute();
        }

        // DownloadFilesTask implementation goes here...
    }
}

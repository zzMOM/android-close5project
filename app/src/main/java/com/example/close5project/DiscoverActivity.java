package com.example.close5project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.close5project.data.Close5SQLiteHelper;
import com.example.close5project.data.SellerObject;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;


public class DiscoverActivity extends ActionBarActivity implements FetchData.FetchDataListener{
    private Close5SQLiteHelper dbHelper;
    private List<SellerObject> sellerObjectList;
    private ExpandableListView listView;
    private ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        dbHelper = new Close5SQLiteHelper(this);

        initializeUniversalImageLoader();
        listView = (ExpandableListView) findViewById(R.id.list_view);
        listView.setDivider(null);

        sellerObjectList = dbHelper.getSellerList();
        Log.e("seller list size", sellerObjectList.size() + "");
        adapter = new CustomExpandableListAdapter(this, sellerObjectList);
        listView.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchData fetchData = new FetchData(getBaseContext(), this);
            fetchData.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUniversalImageLoader() {
        // Loaded images will be cached in memory and/or on disk
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new WeakMemoryCache())
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 320, null)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onFinishFetchData() {
        sellerObjectList = dbHelper.getSellerList();
        adapter = new CustomExpandableListAdapter(this, sellerObjectList);
        listView.setAdapter(adapter);
    }
}

package com.teeterpal.androidteeterpal.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.teeterpal.androidteeterpal.R;
import com.teeterpal.androidteeterpal.data.LocationObject;


public class ConfirmEventAddress extends ActionBarActivity implements
        ConfirmEventAddressMapFragment.OnConfirmAddressMapFragmentListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light);
        setContentView(R.layout.activity_confirm_event_address);

        if (savedInstanceState == null) {
            ConfirmEventAddressMapFragment fragment = new ConfirmEventAddressMapFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.event_map_container, fragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_event_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfirmAddress(LocationObject locationObject, Uri uri) {
        Intent data = new Intent();
        data.putExtra("latitude", locationObject.getLatitude());
        data.putExtra("longitude", locationObject.getLongitude());
        data.putExtra("address", locationObject.getAddress());
        data.putExtra("uri", uri.toString());
        setResult(RESULT_OK, data);
        finish();
    }

}

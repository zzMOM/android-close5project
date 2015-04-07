package com.teeterpal.androidteeterpal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teeterpal.androidteeterpal.login.ThirdPartySignInFragment;
import com.teeterpal.androidteeterpal.map.DialogFragmentArriveTime;
import com.teeterpal.androidteeterpal.map.DialogFragmentFavoriteLocation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


public class TeeterMainMap extends ActionBarActivity  implements
        DialogFragmentFavoriteLocation.OnSelectedFavoriteLocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback {

    protected GoogleApiClient mGoogleApiClient;
    private static final String LOG_TAG = TeeterMainMap.class.getSimpleName();
    private MapFragment mapFragment;
    private LocationManager locationManager;
    private String locationProvider;
    private Marker currentLocationMarker;
    private Marker searchLocationMarker;
    private GoogleMap googleMap;

    private EditText searchText;
    private Button btnCheckIn;
    private ImageButton btnDiscover, btnSearch, btnClearContent;
    private PopupWindow popupWindow;
    private ListView listView;
    private List<String> values;
    private String[] timeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teeter_main_map);

        View searchView = (View) findViewById(R.id.search_layout);
        searchText = (EditText) searchView.findViewById(R.id.search_editext);
        //btnSearch = (ImageButton) searchView.findViewById(R.id.search_location_button);
        btnClearContent = (ImageButton) searchView.findViewById(R.id.clear_content_imagebutton);

        btnCheckIn = (Button) findViewById(R.id.set_time_button);
        btnDiscover = (ImageButton) findViewById(R.id.discover_button);



        // check whether google services available
        if(!isGooglePlayServicesAvailable()){
            return;
        }

        buildGoogleApiClient();

        // Acquire a reference to the system Location Manager
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);

        // Make MyLocationButton or other default button to show in a small region
        // Need calculate method?????
        googleMap.setPadding(0, 150, 0, 180);

        // get current location and zoom in
        getCurrentLocation();

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // If ENTER, perform search
                    String str = searchText.getText().toString();
                    searchLocation(str);
                    return true;
                }
                return false;
            }
        });


        btnClearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                searchText.requestFocus();
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCheckIn.getText().equals("CHECK IN")) {
                    DialogFragment favoriteLocationFragment = DialogFragmentFavoriteLocation.newInstance();
                    favoriteLocationFragment.show(getSupportFragmentManager(), "location dialog");
                } else if (btnCheckIn.getText().equals("TIME")){
                    // showPopUpWindow(v);
                    showPickTimeDialog();
                } else {
                    btnCheckIn.setText("CHECK IN");
                }

            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeeterMainMap.this, ThirdPartySignInFragment.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teeter_main_map, menu);
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
    public void onMapReady(GoogleMap googleMap) {
        Log.e(LOG_TAG, "onMapReady");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Log.e(LOG_TAG, "onResume and connect");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        // Delete the search result before if exists
        if(searchLocationMarker != null){
            searchLocationMarker.remove();
        }
        Log.e(LOG_TAG, "onPause and disconnect");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(LOG_TAG, "onConnected");

        // If location change, call getCurrentLocation()
        Location myLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(myLastLocation != null){
            Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
            DecimalFormat df = new DecimalFormat(".##");
            double clon = Double.valueOf(df.format(currentLocation.getLongitude()));
            double clan = Double.valueOf(df.format(currentLocation.getLatitude()));
            double llon = Double.valueOf(df.format(myLastLocation.getLongitude()));
            double llan = Double.valueOf(df.format(myLastLocation.getLatitude()));
            if(clan != llan && clon != llon) {
                Log.e(LOG_TAG, "last location " + myLastLocation.toString());
                Log.e(LOG_TAG, "current location " + currentLocation.toString());
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    protected synchronized void buildGoogleApiClient() {
        Log.i(LOG_TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //createLocationRequest();
    }

    private void getCurrentLocation(){
        // Get LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        locationProvider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(locationProvider);

        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13)); // 30- zoom in
        //currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void searchLocation(String str){
        if(str.trim().length() == 0){
            return;
        }
        List<Address> addresses;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geoCoder.getFromLocationName(str, 5);
            if(addresses.size() > 0){
                Double latitude = (double) addresses.get(0).getLatitude();
                Double longitude = (double) addresses.get(0).getLongitude();
                // Create a LatLng object for the current location
                LatLng latLng = new LatLng(latitude, longitude);

                if(searchLocationMarker != null){
                    searchLocationMarker.remove();
                }
                searchLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(str));
                // Move the camera instantly to location with z zoom of 15
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Zoom in, animating the camera
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String labelStr = values.get(position);
            Toast.makeText(getApplicationContext(), values.get(position), Toast.LENGTH_SHORT).show();
        }
    };

    private void showPickTimeDialog() {
        android.support.v4.app.DialogFragment newFragment = DialogFragmentArriveTime.newInstance();
        newFragment.show(getSupportFragmentManager(), "time dialog");
    }

    public void doPositiveClick() {
        Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
        btnCheckIn.setText("CHECK IN");
    }

    public void onFinishSelectedLocation(String address) {
        searchLocation(address);
        btnCheckIn.setText("TIME");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(searchLocationMarker != null) {
            searchLocationMarker.remove();
        }
        // If CHECK IN button show "TIME", back up to "CHECK IN"
        if(btnCheckIn.getText().toString().equals("TIME") || btnCheckIn.getText().toString().equals("DONE")){
            btnCheckIn.setText("CHECK IN");
        }
    }
}

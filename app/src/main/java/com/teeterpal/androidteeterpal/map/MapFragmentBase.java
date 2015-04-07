package com.teeterpal.androidteeterpal.map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teeterpal.androidteeterpal.data.LocationObject;

import java.util.List;

/**
 * Created by weiwu on 3/31/15.
 */
public class MapFragmentBase extends Fragment implements
        MapSearchLocationData.AsyncResponse,
        GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {

    protected static final String LOG_TAG = "Map Fragment";

    protected SupportMapFragment mapFragment;
    protected LocationManager locationManager;
    protected String locationProvider;
    protected Marker currentLocationMarker;
    protected Marker searchLocationMarker;
    protected GoogleMap googleMap;
    protected LatLng latLng;
    protected LocationObject locationObject;
    private List<LocationObject> resultList;


    protected void getCurrentLocation(){
        // Get LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        locationProvider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(locationProvider);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13)); // 30- zoom in
        //currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
    }

    protected void searchLocationJSON(String address){
        if(address.trim().length() == 0){
            return;
        }
        MapSearchLocationData getLocationData = new MapSearchLocationData();
        getLocationData.response = this;
        getLocationData.execute(address);
    }


    @Override
    public void onFinishFetchLocationJSON(List<LocationObject> resultList) {
        this.resultList = resultList;

        locationObject = null;

        if(resultList.size() > 0){
            locationObject = resultList.get(0);

            latLng = new LatLng(locationObject.getLatitude(), locationObject.getLongitude());
            if(searchLocationMarker != null) {
                searchLocationMarker.remove();
            }

            String address = locationObject.getAddress();
            int index = address.indexOf(",");
            // address name
            String markerTitle = address.substring(0, index);
            // address street
            String markerSnippet = address.substring(index + 1, address.length());
            searchLocationMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(markerTitle)
                    .snippet(markerSnippet));
            // Always show marker info
            searchLocationMarker.showInfoWindow();
            // Move the camera instantly to location with z zoom of 15
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            // Zoom in, animating the camera
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);

        // Make MyLocationButton or other default button to show in a small region
        // Need calculate method?????
        googleMap.setPadding(0, 150, 0, 180);

        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // get current location and zoom in
        getCurrentLocation();

        googleMap.setOnMapLongClickListener(mapLongClickListener);
        googleMap.setOnMapClickListener(mapClickListener);

    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /* Google map long click to drip a pin*/
    GoogleMap.OnMapLongClickListener mapLongClickListener = new GoogleMap.OnMapLongClickListener(){

        @Override
        public void onMapLongClick(LatLng latLng) {
            if(searchLocationMarker != null) {
                searchLocationMarker.remove();
            }
            searchLocationMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    };

    /* Google map click to remove any searched or dropped pin*/
    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(LatLng latLng) {
            if(searchLocationMarker != null) {
                searchLocationMarker.remove();
            }
        }
    };




}

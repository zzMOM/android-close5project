package com.teeterpal.androidteeterpal.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.teeterpal.androidteeterpal.R;
import com.teeterpal.androidteeterpal.data.LocationObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ConfirmEventAddressMapFragment extends MapFragmentBase implements
        MapSearchBarFragment.MapSearchBarListener,
        GoogleMap.OnMyLocationButtonClickListener {


    private static final String LOG_TAG = ConfirmEventAddressMapFragment.class.getSimpleName();
    private Button confirmAddress;

    private OnConfirmAddressMapFragmentListener listener;

    private MapSearchBarFragment searchBarFragment;
    private Uri mapScreenShotUri;
    private File addressImageFile;


    public interface OnConfirmAddressMapFragmentListener{
        public void onConfirmAddress(LocationObject locationObject, Uri uri);
    }

    public ConfirmEventAddressMapFragment() {
        // Required empty public constructor
    }

    public static ConfirmEventAddressMapFragment newInstance(String address) {
        ConfirmEventAddressMapFragment fragment = new ConfirmEventAddressMapFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView =  inflater.inflate(R.layout.fragment_confirm_event_address_map, container, false);


        // Acquire a reference to the system Location Manager
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);

        // Make MyLocationButton or other default button to show in a small region
        // Need calculate method?????
        googleMap.setPadding(0, 150, 0, 180);

        String address = getArguments().getString("address");
        if(address.trim().length() == 0) {
            // get current location and zoom in
            getCurrentLocation();
        } else {
            searchLocationJSON(address);
        }

        // Show search bar fragment
        searchBarFragment = MapSearchBarFragment.newInstance();


        confirmAddress = (Button) fragmentView.findViewById(R.id.confirm_address_button);
        confirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapScreenShot(fragmentView);
                listener.onConfirmAddress(locationObject, mapScreenShotUri);
            }
        });

        return fragmentView;
    }

    @Override
    public void onKeyEnter(String address) {
        searchLocationJSON(address);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnConfirmAddressMapFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private void mapScreenShot(View v) {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/photo.jpg";
        Log.e("path", mPath);

        addressImageFile = new File(mPath);
        mapScreenShotUri = Uri.fromFile(addressImageFile);

        googleMapScreenShot();

    }

    private void googleMapScreenShot() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {

                // Get marker position in screen
                // Get projection of map
                Projection projection = googleMap.getProjection();
                // Get location of marker
                LatLng latLngMarker = searchLocationMarker.getPosition();
                // Get the location of the marker on the device's display (in pixels)
                Point screenPoint = projection.toScreenLocation(latLngMarker);
                Log.e("screen point", screenPoint.x + " " + screenPoint.y);

                // Get phone screen width and height
                DisplayMetrics metrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
                int screenWidth = metrics.widthPixels;
                int screenHeight = metrics.heightPixels;
                Log.e("screen", screenWidth + " " + screenHeight);

                // Bitmap cut rectangle size
                int cutWidth = screenWidth;
                int cutHeight = screenWidth * 3 / 4;
                Log.e("cut screen", cutWidth + " " + cutHeight);

                // Bitmap up left corner
                int y = screenPoint.y - cutHeight / 2;
                int x = 0;

                Log.e("x and y", x + " " + y);


                FileOutputStream stream;

                // Resize bitmap
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, x, y, cutWidth, cutHeight);

                try
                {
                    stream = new FileOutputStream(addressImageFile);

                    // Write the string to the file
                    resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    stream.flush();
                    stream.close();
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                }

            }
        };

        googleMap.snapshot(callback);

    }

}

package com.teeterpal.androidteeterpal.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.teeterpal.androidteeterpal.EventsActivity;
import com.teeterpal.androidteeterpal.R;
import com.teeterpal.androidteeterpal.TeeterMainMap;

import butterknife.InjectView;
import butterknife.OnClick;


public class MainMapFragment extends MapFragmentBase implements
        MapSearchBarFragment.MapSearchBarListener,
        DialogFragmentFavoriteLocation.OnSelectedFavoriteLocationListener{

    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String LOG_TAG = TeeterMainMap.class.getSimpleName();
    private MapSearchBarFragment searchBarFragment;


    @InjectView(R.id.set_time_button) Button btnSetTime;
    @InjectView(R.id.discover_button) Button btnDiscover;

    public MainMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recover the saved state of google service connection
        /*mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);*/

        // Set fragment action bar
        setHasOptionsMenu(true);

        //buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_main_map, container, false);


        // Acquire a reference to the system Location Manager
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Show search bar fragment
        searchBarFragment = MapSearchBarFragment.newInstance();

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main_map_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_favorite_location){
            showFavoriteLocationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /* Save the state of google service connection state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }*/

    @Override
    public void onKeyEnter(String address) {
        searchLocationJSON(address);
    }

    @OnClick(R.id.set_time_button)
    public void onSetTimeButtonClick() {
        showPickTimeDialog();
    }

    @OnClick(R.id.discover_button)
    public void onDiscoverButtonClick() {
        Intent intent = new Intent(getActivity(), EventsActivity.class);
        startActivity(intent);
    }


    private void showFavoriteLocationDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment favoriteLocationFragment = DialogFragmentFavoriteLocation.newInstance();
        favoriteLocationFragment.show(fm, "location dialog");
    }


    private void showPickTimeDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment newFragment = DialogFragmentArriveTime.newInstance();
        newFragment.show(fm, "time dialog");
    }

    public void doPositiveClick() {
        Toast.makeText(getActivity(), "DONE", Toast.LENGTH_SHORT).show();
    }

    public void onFinishSelectedLocation(String address) {
        searchLocationJSON(address);
    }



}

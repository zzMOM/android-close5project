package com.teeterpal.androidteeterpal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.teeterpal.androidteeterpal.data.EventObject;

import java.util.List;

/**
 * A placeholder fragment containing a simple view for DiscoverEvent activity.
 */
public class DiscoverEventFragment extends Fragment {
    private ListView discoverEventList;
    private ListAdapter adapter;
    private List<EventObject> eventObjectList;


    /*public DiscoverEventFragment() {
    }*/

    public static final DiscoverEventFragment newInstance(){
        DiscoverEventFragment discoverEventFragment = new DiscoverEventFragment();
        return discoverEventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover_event, container, false);

        discoverEventList = (ListView) rootView.findViewById(R.id.discover_event_list);


        adapter = new DiscoverEventAdapter(getActivity(), eventObjectList);
        discoverEventList.setAdapter(adapter);
        return rootView;
    }
}

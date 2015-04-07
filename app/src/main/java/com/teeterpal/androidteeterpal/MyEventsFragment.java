package com.teeterpal.androidteeterpal;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.makeramen.RoundedImageView;
import com.teeterpal.androidteeterpal.data.EventObject;

import java.util.List;

public class MyEventsFragment extends Fragment {
    private RoundedImageView profileRounded;
    private ImageView addMyEvent;
    private ListView myEventListView;
    private ListAdapter adapter;
    private List<EventObject> eventObjectList;

    //public MyEventsFragment(){}

    public static final MyEventsFragment newInstance(){
        MyEventsFragment myEventsFragment = new MyEventsFragment();
        return myEventsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);

        LayoutInflater headerInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = headerInflater.inflate(R.layout.my_event_header, null);
        profileRounded = ((RoundedImageView) headerView.findViewById(R.id.profile_rounded_image_view));
        //profileRounded.setOval(true);
        profileRounded.setImageBitmap(BitmapFactory.decodeResource(v.getResources(), R.drawable.teeter));
        //profileRounded.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addMyEvent = (ImageView) headerView.findViewById(R.id.add_my_event_image);
        addMyEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "addMyEvent Button click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CreateNewEventActivity.class);
                startActivity(intent);
            }
        });


        myEventListView = (ListView) v.findViewById(R.id.my_event_listview);
        adapter = new DiscoverEventAdapter(getActivity(), eventObjectList);
        myEventListView.setAdapter(adapter);
        myEventListView.addHeaderView(headerView);

        return v;
    }
}

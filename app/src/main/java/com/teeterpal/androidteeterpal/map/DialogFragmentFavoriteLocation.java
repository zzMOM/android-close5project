package com.teeterpal.androidteeterpal.map;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.teeterpal.androidteeterpal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DialogFragmentFavoriteLocation extends DialogFragment {

    private ListView favoriteList;
    private HashMap<String, String> hashMap;
    private List<String> values;
    private ArrayAdapter adapter;
    private OnSelectedFavoriteLocationListener mListener;
    private PopupWindow popupWindow;

    public static DialogFragmentFavoriteLocation newInstance() {
        DialogFragmentFavoriteLocation fragment = new DialogFragmentFavoriteLocation();
        return fragment;
    }

    public DialogFragmentFavoriteLocation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hashMap = new HashMap<String, String>();
        hashMap.put("Children's Discovery Museum", "180 Woz Way, San Jose, CA 95110");
        hashMap.put("Townsend Park", "Townsend Park San Jose, CA 95131");
        hashMap.put("Happy Hollow Park & Zoo", "1300 Senter Rd San Jose, CA 95112");
        hashMap.put("Oakland Zoo", "9777 Golf Links Rd Oakland, CA 94605");

        try {
            mListener = (OnSelectedFavoriteLocationListener) getFragmentManager().findFragmentById(R.id.content_frame);
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " Calling Fragment must implement OnSelectedFavoriteLocationListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_listview, container, false);
        favoriteList = (ListView) view.findViewById(R.id.dialog_list);
        values = new ArrayList<String>();
        values.add("Current Location");
        values.add("Townsend Park");
        values.add("Children's Discovery Museum");
        values.add("Happy Hollow Park & Zoo");
        values.add("Oakland Zoo");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        favoriteList.setAdapter(adapter);


        favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialog().dismiss();
                mListener.onFinishSelectedLocation(hashMap.get(values.get(position)));
            }
        });

        getDialog().setTitle("Favorite");
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dialog_background)));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSelectedFavoriteLocationListener {
        public void onFinishSelectedLocation(String address);
    }

}

package com.teeterpal.androidteeterpal.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.teeterpal.androidteeterpal.R;

/**
 * Created by weiwu on 3/31/15.
 */
public class MapSearchBarFragment extends Fragment {

    public interface MapSearchBarListener {
        public void onKeyEnter(String address);
    }

    private AutoCompleteTextView searchText;
    private ImageButton btnClearContent;
    private MapSearchBarListener mapSearchBarListener;

    public MapSearchBarFragment(){}

    public static MapSearchBarFragment newInstance() {
        MapSearchBarFragment fragment = new MapSearchBarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView =  inflater.inflate(R.layout.map_search_bar, container, false);
        searchText = (AutoCompleteTextView) searchView.findViewById(R.id.search_editext);
        btnClearContent = (ImageButton) searchView.findViewById(R.id.clear_content_imagebutton);

        btnClearContent.setVisibility(View.GONE);

        searchText.setAdapter(new PlacesAutoCompleteAdapter(
                getActivity().getApplicationContext()));
        DisplayMetrics displayMetrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float width = (dpWidth - 40) * displayMetrics.density;
        Log.e("map_layout_width", width + "");
        searchText.setDropDownWidth((int)width);
        searchText.setThreshold(10);//a minimum 10 characters

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // If ENTER, perform search
                    String str = searchText.getText().toString();
                    searchText.dismissDropDown();
                    mapSearchBarListener.onKeyEnter(str);
                    return true;
                }
                return false;
            }
        });

        // Set Clear content button when to show
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = searchText.getText().toString();
                if(str.length() > 0){
                    btnClearContent.setVisibility(View.VISIBLE);
                } else {
                    btnClearContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnClearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                searchText.requestFocus();
            }
        });

        return searchView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof MapSearchBarListener) {
            mapSearchBarListener = (MapSearchBarListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(
                    "Fragment must implement MapSearchBarListener");
        }
    }

    @Override
    public void onStop() {
        mapSearchBarListener = null;
        super.onStop();
    }
}

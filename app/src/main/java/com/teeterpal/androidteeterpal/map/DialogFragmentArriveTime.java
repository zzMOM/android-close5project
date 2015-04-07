package com.teeterpal.androidteeterpal.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teeterpal.androidteeterpal.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weiwu on 3/15/15.
 */
public class DialogFragmentArriveTime extends android.support.v4.app.DialogFragment{
    private List<String> values;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] timeArray;

    public static DialogFragmentArriveTime newInstance(){
        DialogFragmentArriveTime fragment = new DialogFragmentArriveTime();
        return fragment;
    }

    public DialogFragmentArriveTime(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_listview, null);
        listView = (ListView) view.findViewById(R.id.dialog_list);
        values = new ArrayList<String>();
        timeArray = getResources().getStringArray(R.array.time_list_item);
        values = Arrays.asList(timeArray);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.dialog_list_item, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(clickListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Time")
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainMapFragment)getParentFragment()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog dialog = builder.create();
        return dialog;
    }


    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String labelStr = values.get(position);
            Toast.makeText(getActivity(), values.get(position), Toast.LENGTH_SHORT).show();
        }
    };
}

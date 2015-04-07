package com.teeterpal.androidteeterpal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by weiwu on 4/2/15.
 */
public class CustomTimePickerFragment extends DialogFragment{

    @InjectView(R.id.time_picker_start_hour) Spinner startHourSpinner;
    @InjectView(R.id.time_picker_start_minute) Spinner startMinuteSpinner;
    @InjectView(R.id.time_picker_start_am_pm) Spinner startAmPmSpinner;
    @InjectView(R.id.time_picker_end_hour) Spinner endHourSpinner;
    @InjectView(R.id.time_picker_end_minute) Spinner endMinuteSpinner;
    @InjectView(R.id.time_picker_end_am_pm) Spinner endAmPmSpinner;

    public static CustomTimePickerFragment newInstance(String sh, String sm, String sa,
                                                       String eh, String em, String ea) {
        CustomTimePickerFragment timePickerFragment = new CustomTimePickerFragment();
        Bundle args = new Bundle();
        args.putString("startHour", sh);
        args.putString("startMinute", sm);
        args.putString("startAmPm", sa);
        args.putString("endHour", eh);
        args.putString("endMinute", em);
        args.putString("endAmPm", ea);
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_custom_time_picker, null);
        ButterKnife.inject(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;
    }


}

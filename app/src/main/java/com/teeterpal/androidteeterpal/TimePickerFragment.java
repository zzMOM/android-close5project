package com.teeterpal.androidteeterpal;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by weiwu on 3/17/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Time Picker result is 24 hour format, transfer to 12 hour format
        DateTimeFormatHelper timeHelper = new DateTimeFormatHelper();
        String time = timeHelper.timeFormatHour24ToHour12Integer(hourOfDay, minute);
        Log.e("TimePicker", time);
        OnTimePickerFinishListener listener = (OnTimePickerFinishListener) getActivity();
        String fragmentTag = this.getTag();
        listener.onFinishSetTime(fragmentTag, time);
    }


    public interface OnTimePickerFinishListener {
        public void onFinishSetTime(String tag, String time);
    }
}

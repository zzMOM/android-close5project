package com.teeterpal.androidteeterpal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by weiwu on 3/17/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        /*SimpleDateFormat weekDayFormat = new SimpleDateFormat("E");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String weekday = weekDayFormat.format(calendar.getTime());*/

        // Transfer selected date into string
        // Date format MM-DD-YYYY
        String date = new StringBuilder()
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" ").toString();

        Log.e("DatePicker", date);

        OnDatePickerFinishListener listener = (OnDatePickerFinishListener) getActivity();
        listener.onFinishSetDate(date);
    }

    public interface OnDatePickerFinishListener {
        public void onFinishSetDate(String date);
    }

}

package com.teeterpal.androidteeterpal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teeterpal.androidteeterpal.data.EventObject;
import com.teeterpal.androidteeterpal.data.LocationObject;
import com.teeterpal.androidteeterpal.data.TeeterParseContract;
import com.teeterpal.androidteeterpal.data.TeeterParseDataReadWrite;
import com.teeterpal.androidteeterpal.map.ConfirmEventAddress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CreateNewEventActivity extends ActionBarActivity implements
        View.OnClickListener,
        DatePickerFragment.OnDatePickerFinishListener,
        TimePickerFragment.OnTimePickerFinishListener{

    @InjectView(R.id.event_has_profile_image_view) ImageView hasProfileImage;
    @InjectView(R.id.error_message) TextView errorMessage;
    @InjectView(R.id.event_title_text) EditText eventTitle;
    @InjectView(R.id.event_keywords_text) EditText eventKeywords;
    @InjectView(R.id.event_description_text) EditText eventDescription;
    @InjectView(R.id.event_address_edit) EditText eventAddress;
    @InjectView(R.id.event_date_button) Button eventDateButton;
    @InjectView(R.id.event_start_time_button) Button eventStartTimeButton;
    @InjectView(R.id.event_end_time_button) Button eventEndTimeButton;
    @InjectView(R.id.event_age_edit_text) EditText eventAge;
    @InjectView(R.id.event_category_spinner) Spinner eventCategory;
    @InjectView(R.id.event_visibility_spinner) Spinner eventVisibility;
    private LocationObject locationObject;
    private Bitmap addressImage;

    private static final int GET_ADDRESS_REQUEST_CODE = 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        ButterKnife.inject(this);

        // Not automatically show keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Get current user info from Parse
        getParseUserInfo();

        // Event address
        eventAddress.setOnClickListener(this);

        // Event Date & Time
        eventDateButton.setOnClickListener(this);
        eventStartTimeButton.setOnClickListener(this);
        eventEndTimeButton.setOnClickListener(this);

        // Event category
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventCategory.setAdapter(categoryAdapter);
        eventCategory.setSelection(0); //default selection

        // Event age

        // Event visibility
        eventVisibility = (Spinner) findViewById(R.id.event_visibility_spinner);
        ArrayAdapter<CharSequence> visibilityAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_visibility_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        visibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventVisibility.setAdapter(visibilityAdapter);
        eventVisibility.setSelection(0); //default selection

        Button finishButton = (Button) findViewById(R.id.create_event_finish_button);
        finishButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.event_address_edit) {
            Intent intent = new Intent(this, ConfirmEventAddress.class);
            intent.putExtra("address", eventAddress.getText().toString());
            startActivityForResult(intent, GET_ADDRESS_REQUEST_CODE);
        } else if(v.getId() == R.id.event_date_button) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } else if(v.getId() == R.id.event_start_time_button) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "startTimePicker");
        } else if(v.getId() == R.id.event_end_time_button) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "endTimePicker");
        } else if(v.getId() == R.id.create_event_finish_button) {
            insertEventInfoToDb();
            //testParseData();
        }
    }

    @Override
    public void onFinishSetDate(String date) {
        eventDateButton.setText(date);
    }

    @Override
    public void onFinishSetTime(String tag, String time) {
        if(tag.equals("startTimePicker")) {
            eventStartTimeButton.setText(time);
        } else if(tag.equals("endTimePicker")) {
            eventEndTimeButton.setText(time);
        }
    }

    private void insertEventInfoToDb(){
        boolean hasProfile = true;                  // From hasProfilImage, set default true
        String title = eventTitle.getText().toString();         // TEXT
        String address = eventAddress.getText().toString().trim();     // TEXT NOT NULL
        String visibility = eventVisibility.getSelectedItem().toString();
        String category = eventCategory.getSelectedItem().toString();
        String age = eventAge.getText().toString();
        String keywords = eventKeywords.getText().toString();   // TEXT
        String description = eventDescription.getText().toString(); // TEXT

        // event dateTtime, lastModifiedDateTime, event createDateTime all long
        String date = eventDateButton.getText().toString();
        String startTime = eventStartTimeButton.getText().toString();
        String endTime = eventEndTimeButton.getText().toString();

        // Input error check. date, time address text can't empty
        if(date.equals("Pick Date") || startTime.equals("Start Time")) {
            errorMessage.setText("Please set date and start time!");
            return;
        } else if(address.length() == 0){
            errorMessage.setText("Address can't be empty!");
            return;
        } else {
            errorMessage.setText("");
        }

        String startAt = new StringBuffer().append(date).append(" ").append(startTime).toString();
        Date formattedStartAt = TimestampUtils.getDateFromString(startAt);
        String endAt = new StringBuffer().append(date).append(" ").append(endTime).toString();
        Date formattedEndAt = null;
        if(!endTime.equals("End Time")){
            formattedEndAt = TimestampUtils.getDateFromString(endAt);
        }

        Double latitude = 0.0, longitude = 0.0;
        if(locationObject != null) {
            latitude = locationObject.getLatitude();
            longitude = locationObject.getLongitude();
        }
        // Get userId
        String ownerId = ParseUser.getCurrentUser().getObjectId();

        // address image transfer to the file for parse
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        addressImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageData = stream.toByteArray();

        EventObject eventObject = new EventObject(hasProfile, ownerId, title, category, visibility, address,
                formattedStartAt, formattedEndAt, age, keywords, description, latitude, longitude, imageData);
        Log.e("EventObject", eventObject.toString());
        TeeterParseDataReadWrite.addEventObject(eventObject);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        if(requestCode == GET_ADDRESS_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                locationObject = new LocationObject();
                locationObject.setLatitude(data.getExtras().getDouble("latitude"));
                locationObject.setLongitude(data.getExtras().getDouble("longitude"));
                locationObject.setAddress(data.getExtras().getString("address"));
                Uri addressImageUri = Uri.parse(data.getExtras().getString("uri"));

                try {
                    addressImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), addressImageUri);
                    //hasProfileImage.setImageBitmap(addressImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                eventAddress.setText(locationObject.getAddress());

            }
        }
    }

    private void getParseUserInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();

    }

    public void testParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TeeterParseContract.eventEntry.EVENT_OBJECT);
        query.getInBackground("0M7SejeYaC", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    EventObject eventObject = new EventObject();
                    eventObject.setHasProfile(object.getBoolean(TeeterParseContract.eventEntry.EVENT_PROFILE_ON_OFF));
                    eventObject.setUserId(object.getString(TeeterParseContract.eventEntry.EVENT_USER_ID));
                    eventObject.setTitle(object.getString(TeeterParseContract.eventEntry.EVENT_TITLE));
                    eventObject.setCategory(object.getString(TeeterParseContract.eventEntry.EVENT_CATEGORY));
                    eventObject.setVisibility(object.getString(TeeterParseContract.eventEntry.EVENT_VISIBILITY));
                    eventObject.setAddress(object.getString(TeeterParseContract.eventEntry.EVENT_ADDRESS));
                    //Date date = object.getDate(TeeterParseContract.eventEntry.EVENT_START_AT);
                    String dateTime = "04-30-2015 10:30AM";
                    Date date = TimestampUtils.getDateFromString(dateTime);
                    eventObject.setStartAt(date);
                    //eventObject.setEndAt(object.getDate(TeeterParseContract.eventEntry.EVENT_END_AT));
                    eventObject.setAge(object.getString(TeeterParseContract.eventEntry.EVENT_AGE));
                    eventObject.setKeywords(object.getString(TeeterParseContract.eventEntry.EVENT_KEYWORDS));
                    eventObject.setDescription(object.getString(TeeterParseContract.eventEntry.EVENT_DESCRIPTION));

                    TeeterParseDataReadWrite.addEventObject(eventObject);


                } else {
                    // something went wrong
                }
            }
        });
    }
}

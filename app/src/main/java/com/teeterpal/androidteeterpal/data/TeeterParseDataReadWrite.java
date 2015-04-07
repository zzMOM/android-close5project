package com.teeterpal.androidteeterpal.data;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.eventEntry;

/**
 * Created by weiwu on 4/1/15.
 */
public class TeeterParseDataReadWrite {
    private EventObject eventObject;

    public static void addEventObject(EventObject eventObject) {
        // Save image to parse file
        ParseFile imgFile = new ParseFile("address_map.png", eventObject.getImage());
        imgFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("image file saveInBackground", "success");
                } else {
                    Log.e("image file saveInBackground", "error" + e.toString());
                }
            }
        });
        ParseObject newEventObject = new ParseObject(eventEntry.EVENT_OBJECT);
        newEventObject.put(eventEntry.EVENT_PROFILE_ON_OFF, eventObject.getHasProfile());
        newEventObject.put(eventEntry.EVENT_USER_ID, eventObject.getUserId());
        newEventObject.put(eventEntry.EVENT_TITLE, eventObject.getTitle());
        newEventObject.put(eventEntry.EVENT_CATEGORY, eventObject.getCategory());
        newEventObject.put(eventEntry.EVENT_VISIBILITY, eventObject.getVisibility());
        newEventObject.put(eventEntry.EVENT_ADDRESS, eventObject.getAddress());
        newEventObject.put(eventEntry.EVENT_START_AT, eventObject.getStartAt());
        if(eventObject.getEndAt() != null) {
            newEventObject.put(eventEntry.EVENT_END_AT, eventObject.getEndAt());
        }
        newEventObject.put(eventEntry.EVENT_AGE, eventObject.getAge());
        newEventObject.put(eventEntry.EVENT_KEYWORDS, eventObject.getKeywords());
        newEventObject.put(eventEntry.EVENT_DESCRIPTION, eventObject.getDescription());
        newEventObject.put(eventEntry.EVENT_LATITUDE, eventObject.getLatitude());
        newEventObject.put(eventEntry.EVENT_LONGITUDE, eventObject.getLongitude());
        newEventObject.put(eventEntry.EVENT_MAP_IMAGE, imgFile);
        newEventObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("newEventObject saveInBackground", "success");
                } else {
                    Log.e("newEventObject saveInBackground", "error" + e.toString());
                }
            }
        });
    }

    public static void getEventObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(eventEntry.EVENT_OBJECT);
        query.getInBackground("Qkbv7g10ZX", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    Log.e("getEventObject", "success");
                } else {
                    Log.e("getEventObject", "failed");
                }
            }
        });
    }

}

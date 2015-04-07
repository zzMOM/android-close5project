package com.teeterpal.androidteeterpal;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.teeterpal.androidteeterpal.data.EventObject;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.eventEntry;
import com.teeterpal.androidteeterpal.data.TeeterParseDataReadWrite;

import junit.framework.TestCase;

/**
 * Created by weiwu on 4/2/15.
 */
public class TeeterParseDataTest extends TestCase {

    public void testParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(eventEntry.EVENT_OBJECT);
        query.getInBackground("0M7SejeYaC", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    EventObject eventObject = new EventObject();
                    eventObject.setHasProfile(object.getBoolean(eventEntry.EVENT_PROFILE_ON_OFF));
                    eventObject.setUserId(object.getString(eventEntry.EVENT_USER_ID));
                    eventObject.setTitle(object.getString(eventEntry.EVENT_TITLE));
                    eventObject.setCategory(object.getString(eventEntry.EVENT_CATEGORY));
                    eventObject.setVisibility(object.getString(eventEntry.EVENT_VISIBILITY));
                    eventObject.setAddress(object.getString(eventEntry.EVENT_ADDRESS));
                    eventObject.setStartAt(object.getString(eventEntry.EVENT_START_AT));
                    eventObject.setEndAt(object.getString(eventEntry.EVENT_END_AT));
                    eventObject.setAge(object.getString(eventEntry.EVENT_AGE));
                    eventObject.setKeywords(object.getString(eventEntry.EVENT_KEYWORDS));
                    eventObject.setDescription(object.getString(eventEntry.EVENT_DESCRIPTION));

                    TeeterParseDataReadWrite.addEventObject(eventObject);


                } else {
                    // something went wrong
                }
            }
        });
    }
}

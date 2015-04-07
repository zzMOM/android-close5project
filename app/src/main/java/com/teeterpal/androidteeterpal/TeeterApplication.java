package com.teeterpal.androidteeterpal;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.teeterpal.androidteeterpal.data.UserInfo;

/**
 * Created by weiwu on 3/24/15.
 */
public class TeeterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(UserInfo.class);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}

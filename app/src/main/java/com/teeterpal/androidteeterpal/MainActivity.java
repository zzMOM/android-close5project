package com.teeterpal.androidteeterpal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parse.ParseUser;
import com.teeterpal.androidteeterpal.login.TeeterSignInSignUpActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Automatically open next page after 5 seconds
        // If not login, open welcome after 5 seconds
        // If already login, open teeterpal map after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*final Intent intent = new Intent(MainActivity.this, TeeterSignInSignUpActivity.class);
                MainActivity.this.startActivity(intent);*/
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    // do stuff with the user
                    Intent intent = new Intent(MainActivity.this, SingleEventActivity.class);
                    startActivity(intent);
                } else {
                    // show the signup or login screen
                    Intent intent = new Intent(MainActivity.this, TeeterSignInSignUpActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                // Prevent weclcome activity back to MainActivity
                MainActivity.this.finish();
            }
        }, 1000);

        // Downloading data when waiting
    }

    @Override
    public void onBackPressed() {
        // Block back button, back button click nothing happen
    }
}

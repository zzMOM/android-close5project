package com.example.close5project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity implements FetchData.FetchDataListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchData fetchData = new FetchData(this, this);
        fetchData.execute();
    }



    @Override
    public void onFinishFetchData() {
        Intent intent = new Intent(MainActivity.this, DiscoverActivity.class);
        startActivity(intent);
    }
}

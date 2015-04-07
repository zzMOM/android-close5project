package com.teeterpal.androidteeterpal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.teeterpal.androidteeterpal.data.TeeterParseContract;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SingleEventActivity extends ActionBarActivity {

    @InjectView(R.id.map_image_view) ImageView addressMapImage;
    @InjectView(R.id.event_date_detail) TextView dateDetail;
    @InjectView(R.id.event_age_detail) TextView ageDetail;
    @InjectView(R.id.event_keywords_detail) TextView keywordsDetail;
    @InjectView(R.id.event_description_detail) TextView descriptionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        ButterKnife.inject(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TeeterParseContract.eventEntry.EVENT_OBJECT);
        query.getInBackground("UoapDyzxfX", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    Log.e("getEventObject", "success");
                    ParseFile fileObject = (ParseFile)parseObject.get("mapImage");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Log.d("test", "We've got data in data.");
                                // use data for something
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                addressMapImage.setImageBitmap(bitmap);
                            } else {
                                Log.d("test", "There was a problem downloading the data.");
                            }
                        }
                    });
                } else {
                    Log.e("getEventObject", "failed");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_event, menu);
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
}

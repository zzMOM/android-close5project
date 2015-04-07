package com.teeterpal.androidteeterpal.map;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.makeramen.RoundedImageView;
import com.teeterpal.androidteeterpal.DrawerListAdapter;
import com.teeterpal.androidteeterpal.R;


public class MainMapActivity extends ActionBarActivity {
    private String[] mPlanetTitles;
    private ListView mDrawerList;
    private int[] drawerIcons;
    private RoundedImageView profileRounded;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        drawerIcons = new int[]{R.drawable.ic_face_grey600_36dp,
                R.drawable.ic_settings_grey600_36dp,
                R.drawable.ic_person_add_grey600_36dp,
                R.drawable.ic_people_grey600_36dp,
                R.drawable.ic_email_grey600_36dp};


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new MainMapFragment())
                    .commit();
        }

        mPlanetTitles = getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerListAdapter(this,
                R.layout.drawer_list_item, drawerIcons, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // headerView position = 0
                Toast.makeText(getApplicationContext(), mPlanetTitles[position - 1], Toast.LENGTH_SHORT).show();
                /*if(mPlanetTitles[position - 1].equals("LOGOUT")){
                    // User logout
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    // Go to signin page for user sign in
                    Intent intent = new Intent(MainMapActivity.this, TeeterSignInSignUpActivity.class);
                    startActivity(intent);
                }*/
            }
        });

        // Set ListView Header
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.logo_profile, null);
        LinearLayout profileLayout = (LinearLayout) headerView.findViewById(R.id.profile_linear_layout);
        // Set profile layout height and width
        profileLayout.setLayoutParams(new AbsListView.LayoutParams(600, 600));
        profileRounded = ((RoundedImageView) headerView.findViewById(R.id.profile_rounded_image_view));
        //profileRounded.setOval(true);
        profileRounded.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.teeter));
        profileRounded.setScaleType(ImageView.ScaleType.FIT_CENTER);
        profileRounded.setTileModeX(Shader.TileMode.REPEAT);
        profileRounded.setTileModeY(Shader.TileMode.REPEAT);

        mDrawerList.addHeaderView(headerView);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_map, menu);
        return true;
    }

}

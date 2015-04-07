package com.teeterpal.androidteeterpal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class EventsActivity extends ActionBarActivity {

    private RadioButton btnMyEvent, btnDiscover, btnFriends;
    private ViewPager viewPager;
    private RadioGroup buttonGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_radio);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonGroup = (RadioGroup) findViewById(R.id.event_button_radio_group);
        viewPager = (ViewPager) findViewById(R.id.event_viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        buttonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.my_event_button:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.discover_button:
                        viewPager.setCurrentItem(1);
                        break;
                    /*case R.id.friends_button:
                        viewPager.setCurrentItem(2);
                        break;*/
                    default:
                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        buttonGroup.check(R.id.my_event_button);
                        break;
                    case 1:
                        buttonGroup.check(R.id.discover_button);
                        break;
                    /*case 2:
                        buttonGroup.check(R.id.friends_button);
                        break;*/
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(1, true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return MyEventsFragment.newInstance();
                case 1: return DiscoverEventFragment.newInstance();
                //case 2: return DiscoverEventFragment.newInstance();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

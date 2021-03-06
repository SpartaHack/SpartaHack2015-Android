package com.mhacks.android.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.mhacks.android.ui.nav.AnnouncementsFragment;
import com.mhacks.android.ui.nav.AwardsFragment;
import com.mhacks.android.ui.nav.ConciergeFragment;
import com.mhacks.android.ui.nav.MapFragment;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.android.ui.nav.ScheduleFragment;
import com.parse.ParseUser;
import com.spartahack.android.R;

import java.util.Date;


/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
         {

    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED = "time_saved";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ParseUser mUser;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();

    private boolean mShouldSync = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // Get the DrawerLayout to set up the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Set a drawerToggle to link the toolbar with the drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Creating navigation drawer from fragment.
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mUser = ParseUser.getCurrentUser();
//
//        // find the layout
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        // the refresh listner. this would be called when the layout is pulled down
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.d("RF","FRESH");
//                // get the new data from you data source
//                // TODO : request data here
//                // our swipeRefreshLayout needs to be notified when the data is returned in order for it to stop the animation
//               // handler.post(refreshing);
//            }
//        });

        setDefaultFragment();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOULD_SYNC, mShouldSync);
        outState.putLong(TIME_SAVED, new Date().getTime());
    }

    /*
    Sets the default fragment to the CountdownFragment.
     */
    public void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AnnouncementsFragment countdownFragment = new AnnouncementsFragment();
        fragmentTransaction.replace(R.id.main_container, countdownFragment);
        fragmentTransaction.addToBackStack("Announcements").commit();

        // Set the title of the toolbar to the current page's title
        setToolbarTitle("Announcements");
    }

    // After this are functions for the Drawer
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                AnnouncementsFragment announcementsFragment = new AnnouncementsFragment();
                fragmentTransaction.replace(R.id.main_container, announcementsFragment);
                fragmentTransaction.addToBackStack("Announcements").commit();
                setToolbarTitle("Announcements");
                break;
            case 1:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                fragmentTransaction.replace(R.id.main_container, scheduleFragment);
                fragmentTransaction.addToBackStack("Schedule").commit();
                setToolbarTitle("Schedule");
                break;
            case 2:
                ConciergeFragment sponsorsFragment = new ConciergeFragment();
                fragmentTransaction.replace(R.id.main_container, sponsorsFragment);
                fragmentTransaction.addToBackStack("Concierge").commit();
                setToolbarTitle("Concierge");
                break;
            case 3:
                AwardsFragment awardsFragment = new AwardsFragment();
                fragmentTransaction.replace(R.id.main_container, awardsFragment);
                fragmentTransaction.addToBackStack("Awards").commit();
                setToolbarTitle("Awards");
                break;
            case 4:
                MapFragment mapFragment = new MapFragment();
                fragmentTransaction.replace(R.id.main_container, mapFragment);
                fragmentTransaction.addToBackStack("Map").commit();
                setToolbarTitle("Map");
                break;
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(findViewById(R.id.navigation_drawer));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onRefresh() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//                refreshLayout.setRefreshing(false);
//            }
//        }, 2500);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else if (getFragmentManager().getBackStackEntryCount() != 0) {
            FragmentManager tbname = getFragmentManager();
            String name = getFragmentManager().getBackStackEntryAt(0).getName();
            setToolbarTitle(getFragmentManager().getBackStackEntryAt(0).getName());
            getFragmentManager().popBackStack();
           // getFragmentManager().
        }
            else {
            super.onBackPressed();
        }
    }

}
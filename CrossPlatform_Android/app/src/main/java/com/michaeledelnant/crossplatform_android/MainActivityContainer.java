package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.michaeledelnant.connection.CheckDataConnection;
import com.parse.ParseUser;


public class MainActivityContainer extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                    DataListFragment.Callbacks,
                    NewDataDialogFragment.Callbacks{

    private static final String TAG = "MainActivityContainer | NavDrawer";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    protected FragmentManager mFragMgr;
    protected CheckDataConnection mCheckDataConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_container);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getString(R.string.title_section1);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mFragMgr = getSupportFragmentManager();

        ParseUser currentUser = ParseUser.getCurrentUser();
        Toast.makeText(this, "Welcome back " + currentUser.get("firstname") + " | " + currentUser.getObjectId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        mFragMgr = getSupportFragmentManager();
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                Log.i(TAG, "Datalist was selected");
                mTitle = getString(R.string.title_section1);

                DataListFragment listFragment = new DataListFragment();
                mFragMgr.beginTransaction()
                        .replace(R.id.container, listFragment, "dataListFragment")
                        .addToBackStack(null)
                        .commit();

                break;
            case 1:
                Log.i(TAG, "Logout was selected");
                mTitle = getString(R.string.title_section2);
                logOutUser();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //Custom methods
    public void logOutUser() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        Toast.makeText(this, "You have successfully logged out", Toast.LENGTH_LONG).show();

        //Send user back to login screen
        Intent intent = new Intent(this, RegPathContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);


    }

    @Override
    public void notifyNewDataItemSaved() {
        DataListFragment dataListFragment = (DataListFragment) getSupportFragmentManager().findFragmentByTag("dataListFragment");
        if(dataListFragment != null) {
            dataListFragment.getDataItems();
        }


    }
}

package com.michaeledelnant.utilities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CPM_ApplicationSettings extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "9NuwVteaUSYZbm9XklQ2nHNxBfk50u11yQeHl4mq", "iNTbX223HBG7khF0SYE4fbAsljO0JH1JJjTMWJW3");

        //Set Default Securities
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        /*Test Data*/
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

    }
}

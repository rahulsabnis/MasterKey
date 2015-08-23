package com.mistersofcode.masterkey;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by rahulsabnis on 8/23/15.
 */
public class ParseApplication extends Application {

    private String APPLICATION_ID = "oeMDj84i1tC5FaWLTf3X0InyDn3ahWIK7zM6xfVj";
    private String PARSE_CLIENT_KEY = "8kXcxnLbLBndSj2oq8Y0TUiKFXT6buQDecyMp4L4";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, PARSE_CLIENT_KEY);
    }
}

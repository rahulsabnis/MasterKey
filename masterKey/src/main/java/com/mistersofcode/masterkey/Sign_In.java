package com.mistersofcode.masterkey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.digits.sdk.android.*;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class Sign_In extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "D1OtTAWrvGD5BA7rUDwChY3fF";
    private static final String TWITTER_SECRET = "RsTZ13zDYdYxnNZ9nXiugRdDVLrF5iG79cd29bdYsJAh8KICDp";
    private String PHONE_NUMBER = "";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null)
        {
            Intent actionStartCard = new Intent("com.mistersofcode.LaunchMain");
            startActivity(actionStartCard);
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        setContentView(R.layout.activity_sign__in);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session and phone number
                PHONE_NUMBER = session.getPhoneNumber().toString();
                Toast toast = Toast.makeText(getApplicationContext(), "Using: " + PHONE_NUMBER,Toast.LENGTH_LONG);
                toast.show();
                if (PHONE_NUMBER != "" )
                {
                    ParseObject authenticatedNumbers = new ParseObject("AuthenticatedNumber");
                    authenticatedNumbers.put("phoneNumber", session.getPhoneNumber().toString());
                    authenticatedNumbers.saveInBackground();
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null)
                {
                    Intent actionStartCard = new Intent("com.mistersofcode.LaunchMain");
                    startActivity(actionStartCard);
                }
                else {
                    Intent actionStartSignIn = new Intent("com.mistersofcode.SignUp");
                    startActivity(actionStartSignIn);
                }
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign__in, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

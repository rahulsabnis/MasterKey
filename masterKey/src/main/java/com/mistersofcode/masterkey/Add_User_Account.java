package com.mistersofcode.masterkey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.*;


public class Add_User_Account extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user__account);
        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "oeMDj84i1tC5FaWLTf3X0InyDn3ahWIK7zM6xfVj", "8kXcxnLbLBndSj2oq8Y0TUiKFXT6buQDecyMp4L4");
    }

    public void signUp(View view){
        EditText userField = (EditText)findViewById(R.id.user_name);
        EditText passField = (EditText)findViewById(R.id.password);

        ParseUser user = new ParseUser();
        user.setUsername(userField.getText().toString());
        user.setPassword(passField.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Successfully created account
                    TextView textView = (TextView)findViewById(R.id.status);
                    textView.setText("Account Created. Click Sign In");
                }
                else
                {
                    // Tell user the sign in failed
                    TextView textView = (TextView)findViewById(R.id.status);
                    textView.setText("Unable create account");
                }
            }
        });

    }
    public void signIn(View view)
    {
        EditText userField = (EditText)findViewById(R.id.user_name);
        EditText passField = (EditText)findViewById(R.id.password);
        ParseUser.logInInBackground(userField.getText().toString(), passField.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null)
                {
                    Intent actionStartCard = new Intent("com.mistersofcode.LaunchMain");
                    startActivity(actionStartCard);
                }
                else
                {
                    TextView textView = (TextView)findViewById(R.id.status);
                    textView.setText("Invalid Login Credentials");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__user__account, menu);
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

package com.mistersofcode.masterkey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class OrganizationTable extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");

        final ArrayList<String> codes = new ArrayList<String>();
        final ArrayList<String> blurbs = new ArrayList<String>();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> organizationList, ParseException e) {
                if (e == null) {
                    String key = "";
                    int value = 0;
                    Log.d("Organization", "Retrieved " + organizationList.size() + " organizations");
                    for (int i = 0; i < organizationList.size(); i++) {
                        //Log.d("Organization ", organizationList.get(i).toString() + "");
                        String code = organizationList.get(i).get("Code").toString();
                        String blurb = organizationList.get(i).get("Blurb").toString();
                        Log.d("Code", "" + code);
                        Log.d("Blurb", "" + blurb);
                        codes.add(code);
                        blurbs.add(blurb);
                    }
                    addRow(codes, blurbs);
                }
                else {
                    Log.e("", "Error: " + e.getMessage());
                }
            }
        });
    }
    private void addRow(ArrayList<String> code, ArrayList<String> blurb)
    {
        Intent intent = new Intent(getApplicationContext(), CardListActivity.class);
        intent.putExtra("blurb", blurb);
        intent.putExtra("code", code);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_organization_table, menu);
//        return true;
//    }

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

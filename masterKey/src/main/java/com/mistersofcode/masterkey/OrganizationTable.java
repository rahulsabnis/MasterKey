package com.mistersofcode.masterkey;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import org.w3c.dom.Text;

import java.util.List;


public class OrganizationTable extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_table);
        TableLayout tableLayout = (TableLayout)findViewById(R.id.table);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");

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
                        addRow(code, blurb);
                    }
                }
                else {
                    Log.e("", "Error: " + e.getMessage());
                }
            }
        });
    }
    private void addRow(String code, String blurb)
    {
        TableLayout tableLayout = (TableLayout)findViewById(R.id.table);

        TextView codeView = new TextView(getApplicationContext());
        codeView.setPadding(3, 3, 3, 3);
        codeView.setText(code);

        TextView blurbView = new TextView(getApplicationContext());
        blurbView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        blurbView.setPadding(3, 3, 3, 3);
        blurbView.setText(blurb);

        TableRow row = new TableRow(getApplicationContext());
        row.addView(codeView);
        row.addView(blurbView);

        tableLayout.addView(row);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organization_table, menu);
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

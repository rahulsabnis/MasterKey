package com.mistersofcode.masterkey;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.simplify.android.sdk.Simplify;
import com.simplify.android.sdk.model.Card;
import com.simplify.android.sdk.model.SimplifyError;
import com.simplify.android.sdk.model.Token;
import com.simplify.android.sdk.view.CardEditor;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String SIMPLIFY_PUBLIC_API_KEY = "sbpb_ZTQ4ZjEyZDItOTM5Zi00NDcxLWIyMmQtOTc4YWExYTRkZWQy";

    private String SIMPLIFY_PRIVATE_API_KEY = "xKixB/deX5d7CdKAj7uWHdNAH2KtglPu76Siwzl25ax5YFFQL0ODSXAOkNtXTToq";

    private Simplify mSimplify;

    private CardEditor mCardEditor;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout layout;

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSimplify = new Simplify(SIMPLIFY_PUBLIC_API_KEY);
        layout = (RelativeLayout) findViewById(R.id.drawerRelativeLayout);
        final CardEditor editor = (CardEditor) findViewById(R.id.card_editor);
        initHamburgerMenu();
        initUI();
    }

    private void initHamburgerMenu() {
        mNavItems.add(new NavItem("Home", "Contribute to a Cause", R.drawable.ic_home_black_36dp));
        mNavItems.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_settings_black_36dp));
        mNavItems.add(new NavItem("Transaction History", "View Your Recent Donations", R.drawable.ic_launcher));
        mNavItems.add(new NavItem("Sign Out", "", R.drawable.ic_power_settings_new_black_36dp));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initUI()
    {
        mCardEditor = (CardEditor) findViewById(R.id.card_editor);

        mCardEditor.setOnChargeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Card card = mCardEditor.getCard();
                AsyncTask<?, ?, ?> task = mSimplify.createCardToken(card, new Simplify.CreateTokenListener() {
                    @Override
                    public void onSuccess(Token token) {
                        Log.i(TAG, "Created Token: " + token.getId());
                        mCardEditor.showSuccessOverlay("");
//                        Log.d("", token.getId());
                        ParseObject parseTransaction = new ParseObject("Transaction");
                        parseTransaction.put("number", token.getCard().getNumber());
                        parseTransaction.put("amount", mCardEditor.getAmount());
                        parseTransaction.saveInBackground();
                    }

                    @Override
                    public void onError(SimplifyError error) {
                        Log.e(TAG, "Error Creating Token: " + error.getMessage());
                        mCardEditor.showErrorOverlay("Unable to retrieve card token. " + error.getMessage());
                    }
                });
            }
        });

        // init reset button
        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardEditor.reset();
            }
        });
    }

    /*
* Called when a particular item from the navigation drawer
* is selected.
* */
    private void selectItemFromDrawer(int position) {
        if (position == 3)
        {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), Add_User_Account.class);
            finish();
            startActivity(intent);
        }
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    // Called when invalidateOptionsMenu() is invoked
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
//        menu.findItem(R.id.action_bar).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}

class NavItem {
    String mTitle;
    String mSubtitle;
    int mIcon;

    public NavItem(String title, String subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }
}
class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText( mNavItems.get(position).mSubtitle );
        iconView.setImageResource(mNavItems.get(position).mIcon);

        return view;
    }
}
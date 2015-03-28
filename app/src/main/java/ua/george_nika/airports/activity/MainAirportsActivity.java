package ua.george_nika.airports.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;
import ua.george_nika.airports.util.AirportAutoCompleteAdapter;


public class MainAirportsActivity extends ActionBarActivity {

    private AutoCompleteTextView airportTitle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    CharSequence mTitle,mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_airports);
        initializeVariable();
        setAdapter();
        setListeners();
    }

    private void initializeVariable(){
        airportTitle = (AutoCompleteTextView) findViewById(R.id.text_find_airport);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    private void setAdapter(){
        airportTitle.setThreshold(3);
        airportTitle.setAdapter(new AirportAutoCompleteAdapter(this));
    }


    private void setListeners(){
        Button buttonDb = (Button) findViewById(R.id.button1);
        Button buttonHttp = (Button) findViewById(R.id.button2);
        Button buttonLocation = (Button) findViewById(R.id.button3);
        Button buttonGoogleMap = (Button) findViewById(R.id.button4);
        buttonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "" + FactoryDb.getInstance().getAirportsDb().getSizeOfBase(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(v.getContext(), AirportsEditActivity.class));
            }
        });

        buttonHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(v.getContext(), MapActivity.class));
            }
        });

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(v.getContext(), LocationActivity.class));
            }
        });

        airportTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Airport airport = (Airport) adapterView.getItemAtPosition(position);
                airportTitle.setText(airport.getName_eng());
            }
        });

        buttonGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(v.getContext(), GoogleMapActivity.class));
            }
        });


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,
                R.string.main_drawer_open,  /* "open drawer" description for accessibility */
                R.string.main_drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                //invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu();
                syncState();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle.syncState();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        FactoryDb.getInstance().getAirportsDb().closeDbAfterWork();
        super.onDestroy();
    }

}

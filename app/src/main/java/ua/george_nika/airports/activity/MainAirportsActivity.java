package ua.george_nika.airports.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;
import ua.george_nika.airports.util.AirportAutoCompleteAdapter;


public class MainAirportsActivity extends ActionBarActivity {

    private AutoCompleteTextView airportTitle;
    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle mainActionBarDrawerToggle;
    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_airports);
        initializeVariables();
        setAdapters();
        setListeners();
        setToolbarForDrawerLayout();
    }

    private void initializeVariables(){
        airportTitle = (AutoCompleteTextView) findViewById(R.id.text_find_airport);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mainToolbar = (Toolbar)findViewById(R.id.main_toolbar);
    }

    private void setAdapters(){
        airportTitle.setThreshold(3);   //after how letters start
        airportTitle.setAdapter(new AirportAutoCompleteAdapter(this));
    }


    private void setListeners(){
        // todo rename button
        Button buttonDb = (Button) findViewById(R.id.button1);
        buttonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AirportsEditActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });


        Button buttonHttp = (Button) findViewById(R.id.button2);
        buttonHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(v.getContext(), MapActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonLocation = (Button) findViewById(R.id.button3);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(v.getContext(), LocationActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonGoogleMap = (Button) findViewById(R.id.button4);
        buttonGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), GoogleMapActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        airportTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Airport airport = (Airport) adapterView.getItemAtPosition(position);
                airportTitle.setText(airport.getName_eng());
            }
        });
    }

    private void setToolbarForDrawerLayout(){
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mainActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mainDrawerLayout,         /* DrawerLayout object */
                mainToolbar,
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

        mainDrawerLayout.setDrawerListener(mainActionBarDrawerToggle);

        //Set the custom mainToolbar
        if (mainToolbar != null){
            setSupportActionBar(mainToolbar);
            mainToolbar.setTitle(null);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mainActionBarDrawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mainActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mainActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mainActionBarDrawerToggle.onOptionsItemSelected(item)) {
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

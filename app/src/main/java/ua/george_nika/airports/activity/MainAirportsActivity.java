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
import android.widget.Toast;

import java.util.Date;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;
import ua.george_nika.airports.util.AirportAutoCompleteAdapter;


public class MainAirportsActivity extends ActionBarActivity {

    private Airport fromAirport;
    private Airport toAirport;
    private Date flyingDate;

    private AutoCompleteTextView fromAirportAutoComplete;
    private AutoCompleteTextView toAirportAutoComplete;

    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle mainActionBarDrawerToggle;
    private Toolbar mainToolbar;

    Button buttonWebFrom;
    Button buttonMapFrom;
    Button buttonWebTo;
    Button buttonMapTo;

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
        fromAirportAutoComplete = (AutoCompleteTextView) findViewById(R.id.text_find_airport_from);
        toAirportAutoComplete = (AutoCompleteTextView) findViewById(R.id.text_find_airport_to);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mainToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        buttonWebFrom = (Button) findViewById(R.id.button_web_from);
        buttonMapFrom = (Button) findViewById(R.id.button_map_from);
        buttonWebTo = (Button) findViewById(R.id.button_web_to);
        buttonMapTo = (Button) findViewById(R.id.button_map_to);

    }

    private void setAdapters(){
        fromAirportAutoComplete.setThreshold(3);
        fromAirportAutoComplete.setAdapter(new AirportAutoCompleteAdapter(this));
        toAirportAutoComplete.setThreshold(3);
        toAirportAutoComplete.setAdapter(new AirportAutoCompleteAdapter(this));
    }


    private void setListeners(){

        buttonWebFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromAirport!=null){
                    if (fromAirport.getWebsite().contains(".")) {
                        Intent intent = new Intent(MainAirportsActivity.this,BrowseWebSiteActivity.class);
                        intent.putExtra(BrowseWebSiteActivity.EXTRA_WEB_SITE,fromAirport.getWebsite());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"No correct web site",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"No airport",Toast.LENGTH_SHORT).show();
                }
                mainDrawerLayout.closeDrawers();
            }
        });

        buttonMapFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromAirport!=null) {
                    Intent intent = new Intent(MainAirportsActivity.this, GoogleMapActivity.class);
                    intent.putExtra(GoogleMapActivity.EXTRA_FIRST_AIRPORT, fromAirport);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"No airport",Toast.LENGTH_SHORT).show();
                }
                mainDrawerLayout.closeDrawers();
            }
        });

        buttonWebTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toAirport!=null){
                    if (toAirport.getWebsite().contains(".")){
                        Intent intent = new Intent(MainAirportsActivity.this,BrowseWebSiteActivity.class);
                        intent.putExtra(BrowseWebSiteActivity.EXTRA_WEB_SITE,toAirport.getWebsite());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"No correct web site",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"No airport",Toast.LENGTH_SHORT).show();
                }
                mainDrawerLayout.closeDrawers();
            }
        });

        buttonMapTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toAirport!=null) {
                    Intent intent = new Intent(MainAirportsActivity.this, GoogleMapActivity.class);
                    intent.putExtra(GoogleMapActivity.EXTRA_FIRST_AIRPORT, toAirport);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"No airport",Toast.LENGTH_SHORT).show();
                }
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonDb = (Button) findViewById(R.id.button_edit_airports);
        buttonDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAirportsActivity.this, AirportsEditActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonProperty = (Button) findViewById(R.id.button_property);
        buttonProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAirportsActivity.this, PropertyActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonFlightRadar24 = (Button) findViewById(R.id.button_website_flightradar24);
        buttonFlightRadar24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserActivity("www.flightradar24.com");
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonPlaneFinder = (Button) findViewById(R.id.button_website_planefinder);
        buttonPlaneFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserActivity("www.planefinder.net");
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonFlightAware = (Button) findViewById(R.id.button_website_flightaware);
        buttonFlightAware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserActivity("www.flightaware.com");
                mainDrawerLayout.closeDrawers();
            }
        });

        Button buttonHTTP = (Button) findViewById(R.id.button_http);
        buttonHTTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainAirportsActivity.this, HTTPActivity.class));
                mainDrawerLayout.closeDrawers();
            }
        });

        fromAirportAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Airport airport = (Airport) adapterView.getItemAtPosition(position);
                fromAirportAutoComplete.setText(airport.getName_eng());
                fromAirport=airport;
                setButtonsNameAndEnabled();
            }
        });

        toAirportAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Airport airport = (Airport) adapterView.getItemAtPosition(position);
                toAirportAutoComplete.setText(airport.getName_eng());
                toAirport=airport;
                setButtonsNameAndEnabled();
            }
        });
    }

    private void startBrowserActivity(String webSite){
        Intent intent = new Intent(MainAirportsActivity.this,BrowseWebSiteActivity.class);
        intent.putExtra(BrowseWebSiteActivity.EXTRA_WEB_SITE,webSite);
        startActivity(intent);

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

    private void setButtonsNameAndEnabled(){
        if (fromAirport!=null){
            buttonMapFrom.setText("Map - " + fromAirport.getName_eng());
            buttonMapFrom.setVisibility(View.VISIBLE);
            buttonWebFrom.setText("Web - " + fromAirport.getName_eng());
            buttonWebFrom.setVisibility(View.VISIBLE);;
        }
        if (toAirport!=null) {
            buttonMapTo.setText("Map - " + toAirport.getName_eng());
            buttonMapTo.setVisibility(View.VISIBLE);;
            buttonWebTo.setText("Web - " + toAirport.getName_eng());
            buttonWebTo.setVisibility(View.VISIBLE);
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("fromAirport", fromAirport);
        outState.putParcelable("toAirport", toAirport);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fromAirport=savedInstanceState.getParcelable("fromAirport");
        toAirport=savedInstanceState.getParcelable("toAirport");
        setButtonsNameAndEnabled();
    }

    @Override
    protected void onDestroy() {
        FactoryDb.getInstance().getAirportsDb().closeDbAfterWork();
        super.onDestroy();
    }

}

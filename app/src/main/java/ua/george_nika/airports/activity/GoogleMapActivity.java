package ua.george_nika.airports.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;

public class GoogleMapActivity extends ActionBarActivity {

    public static final String EXTRA_FIRST_AIRPORT = "EXTRA_FIRST_AIRPORT";
    public static final String EXTRA_SECOND_AIRPORT = "EXTRA_SECOND_AIRPORT";

    private static final String TAG = GoogleMapActivity.class.getSimpleName();

    private DrawerLayout googleMapDrawerLayout;
    private ActionBarDrawerToggle googleMapActionBarDrawerToggle;
    private Toolbar googleMapToolbar;
    private GoogleMap globalGoogleMap;
    private GoogleMapReady googleMapReady =new GoogleMapReady();

    private Airport firstAirport ;
    private Airport secondAirport ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);


        Intent intent = getIntent();
        if (intent==null) {
            Log.e(TAG, " No intent");
            return;
        }
        if (!intent.hasExtra(EXTRA_FIRST_AIRPORT)) {
            Log.e(TAG," Intent without "+EXTRA_FIRST_AIRPORT);
            return ;
        }
        firstAirport = intent.getParcelableExtra(EXTRA_FIRST_AIRPORT);

        if (intent.hasExtra(EXTRA_SECOND_AIRPORT)) {
            secondAirport = intent.getParcelableExtra(EXTRA_SECOND_AIRPORT);
        } else {
            secondAirport = firstAirport;
        }

        initializeVariables();
        setListeners();
        setToolbarForDrawerLayout();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(googleMapReady);
    }

    private void initializeVariables(){
        googleMapDrawerLayout = (DrawerLayout) findViewById(R.id.google_map_drawer_layout);
        googleMapToolbar = (Toolbar)findViewById(R.id.google_map_toolbar);
    }

    private void setListeners(){
        Button buttonSetNormalType = (Button) findViewById(R.id.button_map_type_normal);
        Button buttonSetSatelliteType = (Button) findViewById(R.id.button_map_type_satellite);
        Button buttonSetHybridType = (Button) findViewById(R.id.button_map_type_hybrid);
        Button buttonSetTerrainType = (Button) findViewById(R.id.button_map_type_terrain);
        Button buttonShowNearest  = (Button) findViewById(R.id.button_map_show_nearest_airport);

        buttonSetNormalType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMapDrawerLayout.closeDrawers();
            }
        });

        buttonSetSatelliteType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                googleMapDrawerLayout.closeDrawers();
            }
        });

        buttonSetHybridType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMapDrawerLayout.closeDrawers();
            }
        });

        buttonSetTerrainType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                googleMapDrawerLayout.closeDrawers();
            }
        });

        buttonShowNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer distanceForNearestAirports = 160;
                List<Airport> nearestAirports = FactoryDb.getInstance().getAirportsDb().getNearestAirports(firstAirport,distanceForNearestAirports);
                for (Airport airport :nearestAirports){
                    globalGoogleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.airport_marker))
                            .title(airport.getName_eng())
                            .position(new LatLng(airport.getLatitude(), airport.getLongitude())));
                }
                googleMapDrawerLayout.closeDrawers();
            }
        });
    }

    private void setToolbarForDrawerLayout(){
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        googleMapActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                googleMapDrawerLayout,         /* DrawerLayout object */
                googleMapToolbar,
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

        googleMapDrawerLayout.setDrawerListener(googleMapActionBarDrawerToggle);

        //Set the custom mainToolbar
        if (googleMapToolbar != null){
            setSupportActionBar(googleMapToolbar);
            googleMapToolbar.setTitle(null);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        googleMapActionBarDrawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        googleMapActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        googleMapActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (googleMapActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }



    //*************** Class **************

    private class GoogleMapReady implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            globalGoogleMap = googleMap;
            LatLng airportLatLng = new LatLng(firstAirport.getLatitude(), firstAirport.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airportLatLng, 12));

            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.airport_marker))
                    .title(firstAirport.getName_eng())
                    .position(airportLatLng)
            );

            if (!secondAirport.equals(firstAirport)) {
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.airport_marker))
                        .title(secondAirport.getName_eng())
                        .position(new LatLng(secondAirport.getLatitude(), secondAirport.getLongitude()))
                );
            }
        }
    }
}

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ua.george_nika.airports.R;

public class GoogleMapActivity extends ActionBarActivity {

    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String TAG = GoogleMapActivity.class.getSimpleName();

    private DrawerLayout googleMapDrawerLayout;
    private ActionBarDrawerToggle googleMapActionBarDrawerToggle;
    private Toolbar googleMapToolbar;

    private Float latitude = 0.0f;
    private Float longitude = 0.0f;

    private GoogleMapReady googleMapReady =new GoogleMapReady();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);


        Intent intent = getIntent();
        if (intent==null) {
            Log.e(TAG, " No intent");
            return;
        }
        if (!intent.hasExtra(EXTRA_LATITUDE)) {
            Log.e(TAG," Intent without "+EXTRA_LATITUDE);
            return ;
        }
        if (!intent.hasExtra(EXTRA_LONGITUDE)) {
            Log.e(TAG," Intent without "+EXTRA_LONGITUDE);
            return ;
        }

        latitude = intent.getFloatExtra(EXTRA_LATITUDE,0.0f);
        longitude = intent.getFloatExtra(EXTRA_LONGITUDE,0.0f);

        initializeVariables();
        //setListeners();
        setToolbarForDrawerLayout();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMapReady);

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

    private void initializeVariables(){
        googleMapDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        googleMapToolbar = (Toolbar)findViewById(R.id.toolbar);
    }




    //*************** Class **************

    private class GoogleMapReady implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng airportLatLng = new LatLng(latitude, longitude);

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airportLatLng, 13));

            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.images))
                    .title("Good night")
                    .snippet("The most populous city - Kharkov.")
                    .position(airportLatLng));
        }
    }
}

package ua.george_nika.airports.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;
import ua.george_nika.airports.util.AirportAutoCompleteAdapter;


public class MainAirportsActivity extends Activity {

    private AutoCompleteTextView airportTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_airports);
        initializeVariable();
        setAdapter();
        setListeners();
    }

    private void initializeVariable(){
        airportTitle = (AutoCompleteTextView) findViewById(R.id.text_find_airport);
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
               // startActivity(new Intent(v.getContext(), MapActivity.class));
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

    }


    @Override
    protected void onDestroy() {
        FactoryDb.getInstance().getAirportsDb().closeDbAfterWork();
        super.onDestroy();
    }

}

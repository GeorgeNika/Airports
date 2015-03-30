package ua.george_nika.airports.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.GlobalContextAndData;

public class PropertyActivity extends Activity{

    private SeekBar seekBarZoom;
    private SeekBar seekBarNearestKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        initializeVariable();
        setListeners();
    }

    private void initializeVariable(){
        seekBarZoom = (SeekBar)findViewById(R.id.seekBar_zoom);
        seekBarNearestKm = (SeekBar)findViewById(R.id.seekBar_nearest_airport);
        setSeekData();
    }

    private void setSeekData(){
        seekBarZoom.setMax(GlobalContextAndData.getMaxZoom());
        seekBarZoom.setProgress(GlobalContextAndData.getZoomOnMap());
        seekBarNearestKm.setMax(GlobalContextAndData.getMaxNearestKm());
        seekBarNearestKm.setProgress(GlobalContextAndData.getKmToNearestAirports());
    }

    private void setListeners(){
        Button buttonApply = (Button)findViewById(R.id.button_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalContextAndData.setZoomOnMap(seekBarZoom.getProgress());
                GlobalContextAndData.setKmToNearestAirports(seekBarNearestKm.getProgress());
                finish();
            }
        });

        Button buttonDefault = (Button)findViewById(R.id.button_default);
        buttonDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSeekData();
            }
        });

        Button buttonCancel = (Button)findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

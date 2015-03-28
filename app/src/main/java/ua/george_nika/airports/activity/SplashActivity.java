package ua.george_nika.airports.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ua.george_nika.airports.R;
import ua.george_nika.airports.database.FactoryDb;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FactoryDb.getInstance().getAirportsDb().preparedDbForWork();
            }
        }, 0);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainAirportsActivity.class));
            }
        },2000);
    }
}

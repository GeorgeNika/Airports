package ua.george_nika.airports.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.data.GlobalContextData;

public class AirportsDbInternalImplementation extends AirportsDbAbstract{

    private static InternalDb internalDb;

    private static final String DB_NAME = "airports.sqlite";
    private static final Integer DB_VERSION = 1;


    @Override
    public void preparedDbForWork() {
        internalDb = new InternalDb(GlobalContextData.getContext());
        dbForRead = internalDb.getWritableDatabase();
        dbForWrite = internalDb.getWritableDatabase();
    }

    @Override
    public void closeDbAfterWork() {
        if (internalDb!=null){
            internalDb.close();
        }
        if (dbForRead!=null){
            dbForRead.close();
        }
        if (dbForWrite!=null){
            dbForWrite.close();
        }
    }


    private class InternalDb extends SQLiteOpenHelper {

        public InternalDb(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + AIRPORTS_TABLE_NAME +" ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "iata_code TEXT, " +
                    "icao_code TEXT, " +
                    "name_rus TEXT, " +
                    "name_eng TEXT, " +
                    "city_rus TEXT, " +
                    "city_eng TEXT, " +
                    "gmt_offset  REAL, " +
                    "country_rus TEXT, " +
                    "country_eng TEXT, " +
                    "iso_code  TEXT, " +
                    "latitude  REAL, " +
                    "longitude REAL, " +
                    "website   TEXT " +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop Table "+AIRPORTS_TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop Table "+AIRPORTS_TABLE_NAME);
            onCreate(db);
        }

    }

}

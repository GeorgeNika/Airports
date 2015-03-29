package ua.george_nika.airports.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ua.george_nika.airports.data.Airport;

public abstract class AirportsDbAbstract implements AirportsDb {

    protected static final String AIRPORTS_TABLE_NAME = "airports";

    protected static SQLiteDatabase dbForRead;
    protected static SQLiteDatabase dbForWrite;

    private Airport getAirportFromCursor(Cursor cursor){
        Airport resultAirport = new Airport();
        resultAirport.setIata_code(cursor.getString(cursor.getColumnIndex("iata_code")));
        resultAirport.setIcao_code(cursor.getString(cursor.getColumnIndex("icao_code")));
        resultAirport.setName_rus(cursor.getString(cursor.getColumnIndex("name_rus")));
        resultAirport.setName_eng(cursor.getString(cursor.getColumnIndex("name_eng")));
        resultAirport.setCity_rus(cursor.getString(cursor.getColumnIndex("city_rus")));
        resultAirport.setCity_eng(cursor.getString(cursor.getColumnIndex("city_eng")));
        resultAirport.setGmt_offset(cursor.getFloat(cursor.getColumnIndex("gmt_offset")));
        resultAirport.setCountry_rus(cursor.getString(cursor.getColumnIndex("country_rus")));
        resultAirport.setCountry_eng(cursor.getString(cursor.getColumnIndex("country_eng")));
        resultAirport.setIso_code(cursor.getString(cursor.getColumnIndex("iso_code")));
        resultAirport.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
        resultAirport.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
        resultAirport.setWebsite(cursor.getString(cursor.getColumnIndex("website")));
        return resultAirport;
    }

    @Override
    public Airport getAirportFromId(Integer airportId) {
        Airport resultAirport = new Airport();
        Cursor cursor = dbForRead.query(AIRPORTS_TABLE_NAME,null," _id = ? ",
                new String[] {""+airportId},null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            resultAirport = getAirportFromCursor(cursor);
        }
        return resultAirport;
    }


    @Override
    public void addAirport(Airport addedAirport) {
        dbForWrite.insert(AIRPORTS_TABLE_NAME,null,getContentValues(addedAirport));
    }

    @Override
    public void deleteAirport(Integer airportId) {
        dbForWrite.delete(AIRPORTS_TABLE_NAME," _id = ? ", new String[] {""+airportId});
    }

    @Override
    public void editAirport(Integer airportId, Airport editedAirport) {
        dbForWrite.update(AIRPORTS_TABLE_NAME,getContentValues(editedAirport),
                " _id = ? ", new String[] {""+airportId});
    }

    private ContentValues getContentValues (Airport airport){
        ContentValues resultContentValues = new ContentValues();
        resultContentValues.put("iata_code",airport.getIata_code());
        resultContentValues.put("icao_code",airport.getIcao_code());
        resultContentValues.put("name_rus",airport.getName_rus());
        resultContentValues.put("name_eng",airport.getName_eng());
        resultContentValues.put("city_rus",airport.getCity_rus());
        resultContentValues.put("city_eng",airport.getCity_eng());
        resultContentValues.put("gmt_offset",airport.getGmt_offset());
        resultContentValues.put("country_rus",airport.getCountry_rus());
        resultContentValues.put("country_eng",airport.getCountry_eng());
        resultContentValues.put("iso_code",airport.getIso_code());
        resultContentValues.put("latitude",airport.getLatitude());
        resultContentValues.put("longitude",airport.getLongitude());
        resultContentValues.put("website",airport.getWebsite());
        return resultContentValues;
    }

    @Override
    public List<Airport> getAllAirports() {
        return null;
    }

    @Override
    public List<Airport> getSearchedAirports(String searchedString) {
        int searchedColumns = 8 ;
        String [] searchedStrings = new String[searchedColumns];
        for (int i=0; i<searchedColumns; i++){
            searchedStrings[i] = "%"+searchedString+"%";
        }
        List<Airport> resultAirports = new ArrayList<Airport>();
        Cursor cursor = dbForRead.query(AIRPORTS_TABLE_NAME, null,
                " (name_eng like ?) OR (name_rus like ?) OR " +
                        " (city_eng like ?) OR (city_rus like ?) OR " +
                        " (country_eng like ?) OR (country_rus like ?) OR " +
                        " (iata_code like ?) OR (icao_code like ?)",
                searchedStrings,null,null,null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                resultAirports.add(getAirportFromCursor(cursor));
            }
        }
        return resultAirports;
    }

    @Override
    public List<Airport> getNearestAirports(Airport airport, Integer distanceForNearestAirports) {
        String whereString = " ( "+
            "( longitude between "+(airport.getLongitude()-3)+" and "+(airport.getLongitude()+3)+" ) AND " +
            "( latitude  between "+(airport.getLatitude()-3)+" and "+(airport.getLatitude()+3)+" ) AND " +
            "( _id<>"+airport.get_id()+" ) )";

        List<Airport> resultAirports = new ArrayList<Airport>();
        Cursor cursor = dbForRead.query(AIRPORTS_TABLE_NAME, null,whereString,null,null,null,null);

        if (cursor != null){
            Airport tempAirport;
            while (cursor.moveToNext()) {
                tempAirport = getAirportFromCursor(cursor);
                if (isNearest(airport, tempAirport,distanceForNearestAirports)){
                    resultAirports.add(tempAirport);
                }
            }
        }
        return resultAirports;
    }
    private Boolean isNearest(Airport airport, Airport tempAirport,Integer distanceForNearestAirports ){
        //d=2*arcsin(sqrt((sin((Широта1-Широта2)/2))^2 + cos(Широта1)*cos(Широта2)*(sin((Долгота1-Долгота2)/2))^2))
        //расстояние  = 111,11 * корень [ (дельта д * cos ш)^2 + (дельта ш) ^2]
        Float deltaLongitude = airport.getLongitude()-tempAirport.getLongitude();
        Float deltaLatitude = airport.getLatitude()-tempAirport.getLatitude();
        Double distance = 111.11f * Math.sqrt (Math.pow(deltaLongitude*Math.cos(deltaLatitude),2)+Math.pow(deltaLatitude,2));
        if (distance<=distanceForNearestAirports) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getSizeOfBase() {
        Cursor cursor = dbForRead.query(AIRPORTS_TABLE_NAME,null,null,null,null,null,null);
        return cursor.getCount();
    }

    @Override
    public Cursor getCursorWithSearchedStrings(String searchedName, String searchedCity, String searchedCountry) {
        int searchedColumns = 6 ;
        String tempName = "";
        String tempCity = "";
        String tempCountry = "";

        String [] searchedStrings = new String[searchedColumns];
        if (!searchedName.isEmpty()){
            tempName = searchedName;
        }
        if (!searchedCity.isEmpty()){
            tempCity = searchedCity;
        }
        if (!searchedCountry.isEmpty()){
            tempCountry = searchedCountry;
        }
        searchedStrings[0] = "%"+tempName+"%";
        searchedStrings[1] = searchedStrings[0];
        searchedStrings[2] = "%"+tempCity+"%";
        searchedStrings[3] = searchedStrings[2];
        searchedStrings[4] = "%"+tempCountry+"%";
        searchedStrings[5] = searchedStrings[4];

        Cursor cursor = dbForRead.query(AIRPORTS_TABLE_NAME, null,
                " ((name_eng like ?) OR (name_rus like ?)) AND " +
                        " ((city_eng like ?) OR (city_rus like ?)) AND " +
                        " ((country_eng like ?) OR (country_rus like ?))",
                searchedStrings,null,null,null);
        return cursor;
    }

    @Override
    public String[] getFieldsNameForCursorAdapter() {
        return new String[] {"iata_code" , "name_eng", "city_eng"};
    }
}

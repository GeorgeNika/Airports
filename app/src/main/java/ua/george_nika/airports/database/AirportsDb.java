package ua.george_nika.airports.database;

import android.database.Cursor;

import java.util.List;

import ua.george_nika.airports.data.Airport;

public interface AirportsDb {

    public void preparedDbForWork();

    public void closeDbAfterWork();

    public void addAirport (Airport addedAirport);

    public void deleteAirport (Integer airportId);

    public void editAirport (Integer airportId, Airport newAirport);

    public List<Airport> getAllAirports();

    public List<Airport> getSearchedAirports(String searchedString);

    public Airport getAirportFromId(Integer airportId);

    public Cursor getCursorWithSearchedStrings(String searchedName, String searchedCity, String searchedCountry);

    public String[] getFieldsNameForCursorAdapter();

    public Integer getSizeOfBase();
}

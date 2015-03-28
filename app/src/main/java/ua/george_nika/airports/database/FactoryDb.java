package ua.george_nika.airports.database;

public class FactoryDb {

    private static FactoryDb instance = new FactoryDb();
    private static AirportsDb airportsDb;

    private FactoryDb(){
    }

    public static FactoryDb getInstance() {
        return instance;
    }

    public AirportsDb getAirportsDb (){
        if (airportsDb==null) {
            airportsDb = new AirportsDbCacheImplementation();
            //airportsDb = new AirportsDbInternalImplementation();
        }
        return airportsDb;
    }
}
